package edu.java.domain.dto;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {
    private URI url;
    private OffsetDateTime updatedDate;
    private OffsetDateTime checkedDate;
}
