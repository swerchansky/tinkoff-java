package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StackOverflowQuestionsResponse {
    @JsonProperty("items")
    public List<QuestionResponse> questions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionResponse {
        public String owner;
        @JsonProperty("title")
        public String title;
        @JsonProperty("last_activity_date")
        public OffsetDateTime updatedAt;
        @JsonProperty("answer_count")
        public int answerCount;

        @JsonProperty("owner")
        public void setOwner(Map<String, String> owner) {
            this.owner = owner.get("display_name");
        }
    }
}
