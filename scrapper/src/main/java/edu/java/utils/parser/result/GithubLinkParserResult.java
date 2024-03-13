package edu.java.utils.parser.result;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GithubLinkParserResult implements LinkParserResult {
    private URI url;
    private String owner;
    private String repositoryName;
}
