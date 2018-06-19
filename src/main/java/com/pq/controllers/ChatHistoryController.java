package com.pq.controllers;

import com.pq.service.ChatHistoryService;
import com.pq.utils.UtilityFunction;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.core.json.JsonArray;
import rx.Single;

import java.sql.SQLException;
import java.util.List;


public class ChatHistoryController {

    public static void getChatHistory(RoutingContext context){

        String topic_id = context.request().getParam("topic_id");
        System.out.println("topic_id: "+ topic_id);

        ChatHistoryService.getChatHistory(topic_id)
                .subscribe(
                        data -> UtilityFunction.sendSuccess(context.response(), data),
                        error -> {
                            context.fail(new Exception("Internal server error"));
                            System.out.println(error.getMessage());
                        }
                );
    }

    public static void getUserDetails(RoutingContext context){

        String topic_id = context.request().getParam("topic_id");
        System.out.println("topic_id: "+ topic_id);

        ChatHistoryService.getUserDetails(topic_id)
                .subscribe(
                        data -> UtilityFunction.sendSuccess(context.response(), data),
                        error -> {
                            context.fail(new Exception("Internal server error"));
                            System.out.println(error.getMessage());
                        }
                );
    }

    public static void getAllUsers(RoutingContext context){

        System.out.println("Giving all Users");

        ChatHistoryService.getAllUsers()
                .subscribe(
                        data -> UtilityFunction.sendSuccess(context.response(), data),
                        error -> {
                            context.fail(new Exception("Internal server error"));
                            System.out.println(error.getMessage());
                        }
                );
    }

}
