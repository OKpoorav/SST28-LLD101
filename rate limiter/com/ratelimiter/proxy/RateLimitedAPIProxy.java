package com.ratelimiter.proxy;

import com.ratelimiter.api.RemoteAPIService;
import com.ratelimiter.exception.RateLimitExceededException;
import com.ratelimiter.model.Response;
import com.ratelimiter.strategy.RateLimitStrategy;

public class RateLimitedAPIProxy implements RemoteAPIService {

    private RemoteAPIService realService;
    private RateLimitStrategy strategy;

    public RateLimitedAPIProxy(RemoteAPIService realService, RateLimitStrategy strategy) {
        this.realService = realService;
        this.strategy = strategy;
    }

    @Override
    public Response call(String clientId, String request) {
        if (!strategy.isAllowed(clientId)) {
            throw new RateLimitExceededException("Rate limit exceeded for client " + clientId + ". Try again later. Status: 429");
        }
        return realService.call(clientId, request);
    }
}
