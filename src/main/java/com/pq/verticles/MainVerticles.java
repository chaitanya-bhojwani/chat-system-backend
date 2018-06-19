package com.pq.verticles;

import com.pq.dao.BaseDao;
import com.pq.routes.MainRouters;
import com.pq.service.MQTTService;
import com.pq.utils.ConfigurationManager;
import com.pq.utils.LogFactory;
import com.pq.utils.UtilityFunction;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.mqtt.MqttClient;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.CorsHandler;

public class MainVerticles extends AbstractVerticle{

    private static final Logger logger = LogFactory.getLogger(MainVerticles.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);

        BaseDao.createJDBCClient(config());

        ConfigurationManager.setConfig(config());

        prepareRoutes(router);
        MQTTService.startClient(config());
        HttpServerOptions options = new HttpServerOptions();
        options.setCompressionSupported(true);
        HttpServer server = vertx.createHttpServer(options);
        server.requestHandler(router::accept).listen(config().getInteger("port"));

        super.start(startFuture);
    }

    private void prepareRoutes(Router router) {

        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("X-PINGARUNER")
                .allowedHeader("Content-Type"));

        // Get consolidated body for POST, PUT and PATCH calls
        router.post().handler(BodyHandler.create());
        router.patch().handler(BodyHandler.create());
        router.put().handler(BodyHandler.create());
        router.delete().handler(BodyHandler.create());

        // Log all the API calls with request body and query params.
        logRequest(router);

        handleFailure(router);

        new MainRouters().routes(router);
    }

    private void handleFailure(Router router) {
        router.route().failureHandler(context -> {
            Throwable exception = context.failure();
            logger.error("Got Server Exception: ", exception);
            UtilityFunction.sendFailure(context.request().response(), exception, 500);
        });
    }

    private void logRequest(Router router) {
        router.route().handler(routingContext -> {
            logger.info("Started API : " + routingContext.request().method() +
                    " " + routingContext.request().path());

            if (routingContext.getBodyAsString() != null) {
                logger.info("Request body is " + routingContext.getBodyAsString());
            }

            if (routingContext.request() != null && routingContext.request().query() != null) {
                logger.info("Request query are " + routingContext.request().query());
            }
            routingContext.next();
        });
    }


}
