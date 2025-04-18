package org.example.omnibecard.client;

import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "userClient", url = "${USER_SERVER_ADDRESS}")
public interface UserClient {

}

