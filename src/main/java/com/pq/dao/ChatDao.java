package com.pq.dao;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import rx.Single;

import java.sql.*;
import java.util.List;

public class ChatDao {

    public static Single<List<JsonObject>> getChatHistory(String topic_id) {
        String query = "select * from history " +
                "where topic_id = ?" +
                "order by created_at asc";
        JsonArray param = new JsonArray()
                .add(topic_id);

        return BaseDao.find(query, param);
    }

    public static Single<List<JsonObject>> getUserDetails(String topic_id) {
        String query = "select * from user_details " +
                "where topic_id = ?" +
                "order by created_at asc";
        JsonArray param = new JsonArray()
                .add(topic_id);

        return BaseDao.find(query, param);
    }

    public static Single<List<JsonObject>> getAllUsers() {
        String query = "select * from user_details ";
        JsonArray param = new JsonArray();

        return BaseDao.find(query, param);
    }

    public static Single<UpdateResult> insertChat(String topic, String message) throws SQLException {
        JsonArray jsonArray = new JsonArray()
                .add(topic)
                .add(message);
        String query = "insert into history (topic_id,message) values (?,?)";
        return BaseDao.create(query, jsonArray);
    }

    public static Single<UpdateResult> insertUserDetails(String topic, String message) throws SQLException {
        JsonArray jsonArray = new JsonArray()
                .add(topic)
                .add(message);
        String query = "insert into user_details (topic_id,message) values (?,?)";
        return BaseDao.create(query, jsonArray);
    }

    public static Single<JsonObject> checkCount(String topic, String message) throws SQLException {
        JsonArray jsonArray = new JsonArray()
                .add(topic);
        String query = "select count(*) from history where topic_id = ?";
        return BaseDao.findOne(query, jsonArray);
    }

    public static Single<JsonObject> checkCountUserDetails(String topic, String message) throws SQLException {
        JsonArray jsonArray = new JsonArray()
                .add(topic);
        String query = "select count(*) from user_details where topic_id = ?";
        return BaseDao.findOne(query, jsonArray);
    }

    public static Single<JsonObject> checkCountReadTill(int userId,String mesgType, String topic) throws SQLException {
        //System.out.println("Check Count");
        //System.out.println("topic - " + topic);
        JsonArray jsonArray = new JsonArray()
                .add(userId)
                .add(mesgType)
                .add(topic);
        String query = "select count(*) from history where json_extract(message,'$.userId') = ? and json_extract(message,'$.mesgType') = ? and topic_id = ?";
        return BaseDao.findOne(query, jsonArray);
    }

    public static Single<UpdateResult> updateChat(String message, String topic) throws SQLException {
        //System.out.println("Update Chat");
        //System.out.println("topic - " + topic);
        JsonArray jsonArray = new JsonArray()
                .add(message)
                .add(topic);
        String query = "update history set message = ?, created_at = current_timestamp() where topic_id = ?";
        return BaseDao.create(query, jsonArray);
    }

    public static Single<UpdateResult> updateUserDetails(String message, String topic) throws SQLException {
        //System.out.println("Update Chat");
        //System.out.println("topic - " + topic);
        JsonArray jsonArray = new JsonArray()
                .add(message)
                .add(topic);
        String query = "update user_details set message = ?, created_at = current_timestamp() where topic_id = ?";
        return BaseDao.create(query, jsonArray);
    }

    public static Single<UpdateResult> updateChatReadTill(String message,int userId,String mesgType, String topic) throws SQLException {
        //System.out.println("Update Chat");
        //System.out.println("topic - " + topic);
        JsonArray jsonArray = new JsonArray()
                .add(message)
                .add(userId)
                .add(mesgType)
                .add(topic);
        String query = "update history set message = ?, created_at = current_timestamp() where json_extract(message,'$.userId') = ? and json_extract(message,'$.mesgType') = ? and topic_id = ?";
        return BaseDao.create(query, jsonArray);
    }
}
