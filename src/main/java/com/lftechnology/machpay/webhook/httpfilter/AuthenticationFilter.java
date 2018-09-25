package com.lftechnology.machpay.webhook.httpfilter;

import com.lftechnology.machpay.common.exception.ApiException;
import com.lftechnology.machpay.common.exception.ObjectNotFoundException;
import com.lftechnology.machpay.common.exception.UnAuthorizedException;
import com.lftechnology.machpay.common.pojo.ErrorMessageWrapper;
import com.lftechnology.machpay.webhook.annotation.AuthenticatedCompany;
import com.lftechnology.machpay.webhook.annotation.Secured;
import com.lftechnology.machpay.webhook.constants.CommonConstant;
import com.lftechnology.machpay.webhook.pojo.Company;
import com.lftechnology.machpay.webhook.service.ConfigService;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.StringReader;

/**
 * Authentication filter.
 *
 * @author Achyut Pokhrel <achyutpokhrel@lftechnology.com>
 * @author prkandel
 * @author adhpawal
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    @AuthenticatedCompany
    Event<Company> companyEvent;

    @Inject
    private ConfigService configService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String clientId = requestContext.getHeaderString(CommonConstant.HTTP_CLIENT_ID);
        String clientSecret = requestContext.getHeaderString(CommonConstant.HTTP_CLIENT_SECRET);
        Company company = findCompany(clientId, clientSecret);
        companyEvent.fire(company);
    }

    private Company findCompany(String clientId, String clientSecret) {
        String url = configService.getTenantIdentifierUrl();
        Client client = ClientBuilder.newClient();
        Builder request = client.target(url).request();
        request.header(CommonConstant.HTTP_CLIENT_ID, clientId).header(CommonConstant.HTTP_CLIENT_SECRET, clientSecret);
        Response response = request.get(Response.class);
        return handleResponse(response);
    }

    private Company handleResponse(Response response) {
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Long companyId;
            try (JsonReader reader = Json.createReader(new StringReader(response.readEntity(new GenericType<String>() {
            })))) {
                JsonObject jsonObject = reader.readObject();
                companyId = Long.valueOf(jsonObject.getInt("companyId"));
            }
            return new Company(companyId);
        } else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            ErrorMessageWrapper errorMessageWrapper = response.readEntity(ErrorMessageWrapper.class);
            throw new UnAuthorizedException(errorMessageWrapper.getMessage());
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            ErrorMessageWrapper errorMessageWrapper = response.readEntity(ErrorMessageWrapper.class);
            throw new ObjectNotFoundException(errorMessageWrapper.getMessage());
        } else {
            throw new ApiException();
        }
    }
}
