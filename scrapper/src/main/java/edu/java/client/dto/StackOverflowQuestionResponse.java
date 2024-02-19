package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StackOverflowQuestionResponse {
    public String owner;
    public String title;
    public String updatedAt;

    @JsonProperty("items")
    public void setOwner(List<QuestionResponse> owner) {
        QuestionResponse questionResponse = owner.getFirst();
        this.owner = questionResponse.owner;
        this.title = questionResponse.title;
        this.updatedAt = questionResponse.updatedAt;
    }

    public static class QuestionResponse {
        public String owner;
        @JsonProperty("title")
        public String title;
        @JsonProperty("last_activity_date")
        public String updatedAt;

        @JsonProperty("owner")
        public void setOwner(Map<String, String> owner) {
            this.owner = owner.get("display_name");
        }
    }
}
