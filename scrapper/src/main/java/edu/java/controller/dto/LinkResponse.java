package edu.java.controller.dto;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkResponse {
    public URI url;
}
