package edu.java.bot.client.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ListLinksResponse {
    public List<LinkResponse> links;
    public int size;
}
