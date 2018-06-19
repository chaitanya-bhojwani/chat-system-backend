package com.pq.service;

import com.pq.dao.ChatDao;
import io.vertx.core.json.JsonObject;
import rx.Single;
import java.util.List;
import java.util.stream.Collectors;

public class ChatHistoryService {

    public static Single<List<JsonObject>> getChatHistory(String topic_id) {
        return ChatDao.getChatHistory(topic_id);
    }
    public static Single<List<JsonObject>> getUserDetails(String topic_id) {
        return ChatDao.getUserDetails(topic_id);
    }
    public static Single<List<JsonObject>> getAllUsers() {
        return ChatDao.getAllUsers();
    }
}
