package com.example.futasbus.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.futasbus.Adapter.ChatAdapter;
import com.example.futasbus.ApiChatbox;
import com.example.futasbus.R;
import com.example.futasbus.model.ChatMessage;
import com.example.futasbus.respone.ChatResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private EditText editTextMessage;
    private Button buttonSend;
    private RecyclerView recyclerViewChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        editTextMessage = view.findViewById(R.id.editTextMessage);
        buttonSend = view.findViewById(R.id.buttonSend);
        recyclerViewChat = view.findViewById(R.id.recyclerViewMessages);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        recyclerViewChat.setAdapter(chatAdapter);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonSend.setOnClickListener(v -> {
            String userMessage = editTextMessage.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                messageList.add(new ChatMessage(userMessage, true));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerViewChat.scrollToPosition(messageList.size() - 1);
                editTextMessage.setText("");

                callBotAPI(userMessage);
            }
        });

        return view;
    }

    private void callBotAPI(String message) {
        ChatMessage chatMessage = new ChatMessage(message);

        ApiChatbox.getApiServiceChatbox().sendMessage(chatMessage).enqueue(new retrofit2.Callback<ChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String botReply = response.body().getAnswer();
                    messageList.add(new ChatMessage(botReply, false));
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewChat.scrollToPosition(messageList.size() - 1);
                } else {
                    messageList.add(new ChatMessage("Lỗi phản hồi từ máy chủ", false));
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                messageList.add(new ChatMessage("Không thể kết nối đến máy chủ", false));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
            }
        });
    }

}
