package edu.java.utils.parser.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GithubLinkParserResult implements linkParserResult {
    private String owner;
    private String repositoryName;
}
