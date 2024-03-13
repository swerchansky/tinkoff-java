package edu.java.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveLinkRequest {
    @JsonProperty("link")
    public URI link;
}
