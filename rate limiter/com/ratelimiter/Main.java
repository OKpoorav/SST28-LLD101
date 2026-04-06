package com.ratelimiter;

import com.ratelimiter.api.RemoteAPIService;
import com.ratelimiter.exception.RateLimitExceededException;
import com.ratelimiter.model.RateLimiterConfig;
import com.ratelimiter.model.Response;
import com.ratelimiter.proxy.RateLimitedAPIProxy;
import com.ratelimiter.real.RealRemoteAPIService;
import com.ratelimiter.strategy.FixedWindowStrategy;
import com.ratelimiter.strategy.LeakyBucketStrategy;
import com.ratelimiter.strategy.RateLimitStrategy;
import com.ratelimiter.strategy.SlidingWindowStrategy;
import com.ratelimiter.strategy.TokenBucketStrategy;

public class Main {

    public static void main(String[] args) {
        RateLimiterConfig config = new RateLimiterConfig(3, 10, 1);
        String endpoint = "https://api.example.com";

        System.out.println("=== FixedWindowStrategy ===");
        runStrategyTest(new FixedWindowStrategy(config), endpoint);

        System.out.println("=== SlidingWindowStrategy ===");
        runStrategyTest(new SlidingWindowStrategy(config), endpoint);

        System.out.println("=== TokenBucketStrategy ===");
        runStrategyTest(new TokenBucketStrategy(config), endpoint);

        System.out.println("=== LeakyBucketStrategy ===");
        runStrategyTest(new LeakyBucketStrategy(config), endpoint);

        System.out.println("=== Two clients, FixedWindow ===");
        RateLimiterConfig twoClientConfig = new RateLimiterConfig(2, 10, 1);
        RateLimitStrategy twoClientStrategy = new FixedWindowStrategy(twoClientConfig);
        RemoteAPIService realService = new RealRemoteAPIService(endpoint);
        RemoteAPIService proxy = new RateLimitedAPIProxy(realService, twoClientStrategy);

        String[] clients = {"client-A", "client-B", "client-A", "client-B", "client-A", "client-B"};
        for (int i = 0; i < clients.length; i++) {
            try {
                Response response = proxy.call(clients[i], "request-" + (i + 1));
                System.out.println("Call " + (i + 1) + " (" + clients[i] + ") succeeded: " + response.getBody());
            } catch (RateLimitExceededException exception) {
                System.out.println("Call " + (i + 1) + " (" + clients[i] + ") blocked: " + exception.getMessage());
            }
        }
    }

    private static void runStrategyTest(RateLimitStrategy strategy, String endpoint) {
        RemoteAPIService realService = new RealRemoteAPIService(endpoint);
        RemoteAPIService proxy = new RateLimitedAPIProxy(realService, strategy);

        for (int i = 1; i <= 5; i++) {
            try {
                Response response = proxy.call("client-A", "request-" + i);
                System.out.println("Call " + i + " succeeded: " + response.getBody());
            } catch (RateLimitExceededException exception) {
                System.out.println("Call " + i + " blocked: " + exception.getMessage());
            }
        }
        System.out.println();
    }
}
