package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GithubRepositoryResponse {
    public String owner;
    @JsonProperty("name")
    public String name;
    @JsonProperty("updated_at")
    public String updatedAt;

    @JsonProperty("owner")
    public void setOwner(Map<String, String> owner) {
        this.owner = owner.get("login");
    }
}
