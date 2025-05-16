package com.example.futasbus.respone;

import com.google.gson.annotations.SerializedName;

public class ChatResponse {

    @SerializedName("Câu hỏi gần giống nhất")
    private String similarQuestion;

    @SerializedName("Câu trả lời")
    private String answer;

    @SerializedName("Độ tương đồng")
    private double similarity;

    public String getSimilarQuestion() {
        return similarQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public double getSimilarity() {
        return similarity;
    }

}
