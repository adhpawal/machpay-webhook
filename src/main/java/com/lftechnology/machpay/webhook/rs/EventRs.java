package com.lftechnology.machpay.webhook.rs;

import com.lftechnology.machpay.common.pojo.Paging;
import com.lftechnology.machpay.common.pojo.SuccessResponse;
import com.lftechnology.machpay.webhook.annotation.AuthenticatedCompany;
import com.lftechnology.machpay.webhook.objectmapper.SubscriptionMapper;
import com.lftechnology.machpay.webhook.pojo.Company;
import com.lftechnology.machpay.webhook.service.SubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by adhpawal on 9/4/17.
 */
@Path("/v1/events")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Subscription", produces = "application/json", consumes = "application/json")
public class EventRs {

    @Inject
    @AuthenticatedCompany
    private Company company;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private SubscriptionMapper subscriptionMapper;

    @GET
    @Path("/")
    @ApiOperation(value = "Get list of Recent Events")
    public Response list() {
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResults(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(company.getCompanyId())));
        successResponse.setPaging(new Paging(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(company.getCompanyId())).size()));
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get Event Detail")
    public Response detail(@PathParam("id") String id) {
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResults(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(company.getCompanyId())));
        successResponse.setPaging(new Paging(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(company.getCompanyId())).size()));
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }
}
