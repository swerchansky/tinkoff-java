package edu.java.client.dto;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class LinkUpdateRequest {
    public long id;
    public URI url;
    public String description;
    public List<Long> tgChatIds;
}
