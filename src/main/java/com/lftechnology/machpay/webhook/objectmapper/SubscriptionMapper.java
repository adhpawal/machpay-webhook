package com.lftechnology.machpay.webhook.objectmapper;

import com.lftechnology.machpay.webhook.dto.SubscriptionDto;
import com.lftechnology.machpay.webhook.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Created by adhpawal on 4/11/17.
 */
@Mapper(componentModel = "cdi")
public interface SubscriptionMapper {

    @Mapping(source = "accessKey", target = "secret")
    SubscriptionDto toDto(Subscription subscription);

    List<SubscriptionDto> toSubscriptionDtoList(List<Subscription> subscriptions);

}
