package edu.java.utils.parser.result;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StackOverflowlinkParserResult implements LinkParserResult {
    private URI url;
    private Long id;
}
