package com.pq.service;

import com.pq.dao.ChatDao;
import com.pq.utils.UtilityFunction;
import io.netty.handler.codec.mqtt.MqttQoS;


import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttAuth;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.extern.java.Log;
import rx.Single;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class MQTTService {

    //static String topic = "rpi2/temp";
    static String topic = "PQ/+";
    public static void startClient(JsonObject config) {
        System.out.println("start MQtt client called");

        MqttClient client = MqttClient.create(Vertx.vertx());

        //Connecting client
        client.connect(1883, "broker.hivemq.com", s -> {
            if (s.succeeded()) {
                System.out.println("Connected to a server");
                client.subscribe(topic, 2);

            } else {
                System.out.println("Failed to connect to a server");
                System.out.println(s.cause());
            }
        });

        //Subscribing to topic
        client.publishHandler(s -> {
            boolean found = false;

            System.out.println("There are new message in topic: " + s.topicName());
            System.out.println("Content(as string) of the message: " + s.payload().toString());
            System.out.println("QoS: " + s.qosLevel());
            JsonObject jsonObject = s.payload().toJsonObject();
            String mesgType = jsonObject.getString("mesgType");
            if (mesgType.equals("userDetails")) {
                System.out.println("User Details Captured");
                try {
                    ChatDao.checkCountUserDetails(s.topicName(),s.payload().toString())
                            .subscribe(json -> {
                                //System.out.println("Here");
                                //System.out.println(json.toString());
                                //System.out.println("Here");
                                //return Single.just(new JsonObject());
                                Long result = json.getLong("count(*)");
                                if(result == 1) {
                                    //System.out.println("At 1");
                                    try {
                                        //System.out.println("At 1");
                                        ChatDao.updateUserDetails(s.payload().toString(), s.topicName()).subscribe(
                                                data -> System.out.println("write success"),
                                                error -> {
                                                    System.out.println(error.getMessage());
                                                }
                                        );
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } else if (result == 0){
                                    //System.out.println("At 0");
                                    try {
                                        //System.out.println("At 0");
                                        ChatDao.insertUserDetails(s.topicName(), s.payload().toString()).subscribe(
                                                data -> System.out.println("write success"),
                                                error -> {
                                                    System.out.println(error.getMessage());
                                                }
                                        );
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } catch (SQLException e) {
                    System.out.println("Catch");
                    e.printStackTrace();
                }

                //return;
            }
            if (mesgType.equals("onlineStatus")) {
                System.out.println("Online Status Captured");
                //jsonObject.put("msgId",System.currentTimeMillis());
                //System.out.println("msgId is" + jsonObject.getString("msgId"));
                //update if exists else insert into history;
                try {
                    ChatDao.checkCount(s.topicName(),s.payload().toString())
                            .subscribe(json -> {
                                //System.out.println("Here");
                                //System.out.println(json.toString());
                                //System.out.println("Here");
                               //return Single.just(new JsonObject());
                                Long result = json.getLong("count(*)");
                                if(result == 1) {
                                    //System.out.println("At 1");
                                    try {
                                        //System.out.println("At 1");
                                        ChatDao.updateChat(s.payload().toString(), s.topicName()).subscribe(
                                                data -> System.out.println("write success"),
                                                error -> {
                                                    System.out.println(error.getMessage());
                                                }
                                        );
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } else if (result == 0){
                                    //System.out.println("At 0");
                                    try {
                                        //System.out.println("At 0");
                                        ChatDao.insertChat(s.topicName(), s.payload().toString()).subscribe(
                                                data -> System.out.println("write success"),
                                                error -> {
                                                    System.out.println(error.getMessage());
                                                }
                                        );
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } catch (SQLException e) {
                    System.out.println("Catch");
                    e.printStackTrace();
                }
               //return;
            }
            if (mesgType.equals("readTillStatus")) {
                System.out.println("read Till Status Captured");
                int userId = jsonObject.getInteger("userId");
                System.out.println("User id is " + userId);
                //jsonObject.put("msgId",System.currentTimeMillis());
                //System.out.println("msgId is" + jsonObject.getString("msgId"));
                //update if exists else insert into history;
                try {
                    ChatDao.checkCountReadTill(userId,mesgType,s.topicName())
                            .subscribe(json -> {
                                //System.out.println("Here");
                                //System.out.println(json.toString());
                                //System.out.println("Here");
                                //return Single.just(new JsonObject());
                                Long result = json.getLong("count(*)");
                                if(result == 1) {
                                    //System.out.println("At 1");
                                    try {
                                        ChatDao.updateChatReadTill(s.payload().toString(),userId,mesgType,s.topicName()).subscribe(
                                                data -> System.out.println("write success"),
                                                error -> {
                                                    System.out.println(error.getMessage());
                                                }
                                        );
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } else if (result == 0){
                                    //System.out.println("At 0");
                                    try {
                                        //System.out.println("At 0");
                                        ChatDao.insertChat(s.topicName(), s.payload().toString()).subscribe(
                                                data -> System.out.println("write success"),
                                                error -> {
                                                    System.out.println(error.getMessage());
                                                }
                                        );
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } catch (SQLException e) {
                    System.out.println("Catch");
                    e.printStackTrace();
                }
                // return;
            }

            if (mesgType.equals("istypingStatus")) {
                System.out.println("isTyping Status Captured");
                return;
            }
            if (mesgType.equals("message")) {
                try {
                    ChatDao.insertChat(s.topicName(), s.payload().toString())
                            .subscribe(
                                    data -> System.out.println("write success"),
                                    error -> {
                                        System.out.println(error.getMessage());
                                    }
                            );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // handle response on subscribe request
        client.subscribeCompletionHandler(h -> {
            System.out.println("Receive SUBACK from server with granted QoS : " + h.grantedQoSLevels());

            // let's publish a message to the subscribed topic
            client.publish(
                    topic,
                    Buffer.buffer("test message"),
                    MqttQoS.AT_MOST_ONCE,
                    false,
                    false,
                    s -> System.out.println("Publish sent to a server"));
        });




    }

}
