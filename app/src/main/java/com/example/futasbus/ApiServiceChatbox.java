package com.example.futasbus;

import com.example.futasbus.model.ChatMessage;
import com.example.futasbus.respone.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServiceChatbox {

    @POST("chat")
    Call<ChatResponse> sendMessage(@Body ChatMessage request);

}
