package edu.java.domain.dto;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {
    public long id;
    public URI link;
    public OffsetDateTime checkedDate;
}
