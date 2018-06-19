package com.pq.routes;

import com.pq.controllers.ChatHistoryController;
import com.pq.controllers.HealthCheckController;
import com.pq.service.ChatHistoryService;
import io.vertx.rxjava.ext.web.Router;

public class MainRouters {
    public void routes(Router router) {

        router.get("/health_check").handler(HealthCheckController::healthCheck);
        router.get("/user/chat_history").handler(ChatHistoryController::getChatHistory);
        router.get("/user/details").handler(ChatHistoryController::getUserDetails);
        router.get("/user/details/allUsers").handler(ChatHistoryController::getAllUsers);
    }
}
