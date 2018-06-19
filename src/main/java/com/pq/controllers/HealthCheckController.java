package com.pq.controllers;

import com.pq.utils.UtilityFunction;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

public class HealthCheckController {

    public static void healthCheck(RoutingContext context) {
        UtilityFunction.sendSuccess(context.response(), new JsonObject());
    }

}

