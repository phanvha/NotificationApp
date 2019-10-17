package com.map4d.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("token")
    @Expose
    private String token;
//    @SerializedName("Success")
//    @Expose
//    private Boolean Success;
//    @SerializedName("Message")
//    @Expose
//    private String Message;

    public Data(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public Data() {
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        token = token;
    }

//    public Boolean getSuccess() {
//        return Success;
//    }
//
//    public void setSuccess(Boolean success) {
//        Success = success;
//    }
//
//    public String getMessage() {
//        return Message;
//    }
//
//    public void setMessage(String message) {
//        Message = message;
//    }
}