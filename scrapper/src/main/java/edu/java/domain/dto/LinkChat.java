package edu.java.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkChat {
    private Link link;
    private Chat chat;
}
