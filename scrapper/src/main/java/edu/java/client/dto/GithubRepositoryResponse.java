package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepositoryResponse {
    public String owner;
    @JsonProperty("name")
    public String name;
    @JsonProperty("updated_at")
    public OffsetDateTime updatedAt;

    @JsonProperty("owner")
    public void setOwner(Map<String, String> owner) {
        this.owner = owner.get("login");
    }
}
