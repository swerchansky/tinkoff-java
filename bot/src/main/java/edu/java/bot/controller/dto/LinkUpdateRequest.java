package edu.java.bot.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdateRequest {
    @JsonProperty("url")
    public URI url;
    @JsonProperty("description")
    public String description;
    @JsonProperty("tgChatIds")
    public List<Long> tgChatIds;
}
