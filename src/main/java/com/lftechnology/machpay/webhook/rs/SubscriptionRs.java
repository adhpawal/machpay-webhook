package com.lftechnology.machpay.webhook.rs;

import com.lftechnology.machpay.common.pojo.Paging;
import com.lftechnology.machpay.common.pojo.SuccessResponse;
import com.lftechnology.machpay.webhook.annotation.AuthenticatedCompany;
import com.lftechnology.machpay.webhook.annotation.Secured;
import com.lftechnology.machpay.webhook.dto.SubscriptionDto;
import com.lftechnology.machpay.webhook.dto.SubscriptionPauseDto;
import com.lftechnology.machpay.webhook.entity.Subscription;
import com.lftechnology.machpay.webhook.objectmapper.SubscriptionMapper;
import com.lftechnology.machpay.webhook.pojo.Company;
import com.lftechnology.machpay.webhook.service.SubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by adhpawal on 4/30/17.
 */
@Secured
@Path("v1/subscriptions")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Subscription", produces = "application/json", consumes = "application/json")
public class SubscriptionRs {

    @Inject
    @AuthenticatedCompany
    private Company company;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private SubscriptionMapper subscriptionMapper;

    @GET
    @Path("/{companyId}")
    @ApiOperation(value = "Get list of Registered Webhooks")
    public Response list(@PathParam("companyId") Long compnayId) {
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResults(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(compnayId)));
        successResponse.setPaging(new Paging(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(compnayId)).size()));
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }


    @GET
    @Path("/")
    @ApiOperation(value = "Get list of Registered Webhooks")
    public Response getAll() {
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResults(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(company.getCompanyId())));
        successResponse.setPaging(new Paging(subscriptionMapper.toSubscriptionDtoList(subscriptionService.findByCompanyId(company.getCompanyId())).size()));
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @GET
    @Path("/companies/{companyId}/subscriptions/{id}")
    @ApiOperation(value = "Get list of Registered Webhooks")
    public Response get(@PathParam("id") String subscriptionId, @PathParam("companyId") Long companyId) {
        return Response.status(Response.Status.OK).entity(this.subscriptionMapper.toDto(subscriptionService.findByCompanyIdAndSubscriptionId(companyId, subscriptionId))).build();
    }


    @POST
    @Path("/")
    @ApiOperation(value = "Create Webhook Subscription")
    public Response create(@Valid SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionService.save(company.getCompanyId(), subscriptionDto);
        return Response.status(Response.Status.OK).entity(this.subscriptionMapper.toDto(subscription)).build();
    }

    @POST
    @Path("/{id}")
    @ApiOperation(value = "Pause Webhook Subscription")
    public Response pause(@PathParam("id") String subscriptionId, @Valid SubscriptionPauseDto subscriptionPauseDto) {
        return Response.status(Response.Status.OK).entity(this.subscriptionMapper.toDto(subscriptionService.pause(subscriptionId, subscriptionPauseDto.isPause()))).build();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Remove Webhook Subscription")
    public Response remove(@PathParam("id") String subscriptionId) {
        subscriptionService.remove(subscriptionId);
        return Response.status(Response.Status.OK).entity("").build();
    }
}


