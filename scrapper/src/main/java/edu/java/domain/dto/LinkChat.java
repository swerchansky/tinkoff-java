package edu.java.domain.dto;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkChat {
    private URI url;
    private long chatId;
    private Integer starCount;
    private Integer answerCount;
    private OffsetDateTime updatedDate;
    private OffsetDateTime checkedDate;
}
