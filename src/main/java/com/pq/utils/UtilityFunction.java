package com.pq.utils;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Single;

public class UtilityFunction {

    private static final Logger logger = LogFactory.getLogger(UtilityFunction.class);

    public static void sendFailure(HttpServerResponse httpServerResponse, Object reason, int statusCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("status", "FAILURE");
        jsonObject.put("error", reason);
        httpServerResponse.putHeader("content-type", "application/json; charset=utf-8");
        httpServerResponse.putHeader("charset", "UTF-8");
        httpServerResponse.setStatusCode(statusCode);
        logger.info("response = " + jsonObject);
        httpServerResponse.end(jsonObject.toString());
    }

    public static void sendSuccess(HttpServerResponse httpServerResponse, Object data) {
        JsonObject response = new JsonObject();
        response.put("status", "SUCCESS");
        response.put("data", data);
        httpServerResponse.putHeader("content-type", "application/json; charset=utf-8");
        httpServerResponse.putHeader("charset", "UTF-8");
        httpServerResponse.setStatusCode(HttpResponseStatus.OK.code());
        logger.info("response = " + response);
        httpServerResponse.end(response.toString());
    }

    public static void sendSuccess(RoutingContext context, Single<JsonObject> response) {
        response.subscribe(json -> sendSuccess(context.response(), json), context::fail);
    }
}
