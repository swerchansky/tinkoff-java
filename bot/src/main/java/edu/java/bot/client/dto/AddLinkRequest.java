package edu.java.bot.client.dto;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class AddLinkRequest {
    public URI link;
}