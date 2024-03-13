package edu.java.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListLinksResponse {
    public List<LinkResponse> links;
    public int size;
}
