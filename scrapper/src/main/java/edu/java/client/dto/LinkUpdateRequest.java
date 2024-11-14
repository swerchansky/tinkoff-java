package edu.java.client.dto;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdateRequest {
    public URI url;
    public String description;
    public List<Long> tgChatIds;
}
