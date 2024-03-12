package edu.java.utils.parser;

import edu.java.utils.parser.result.GithubLinkParserResult;
import edu.java.utils.parser.result.linkParserResult;
import java.net.URI;

public final class GithubLinkParser implements LinkParser {
    @Override
    public linkParserResult parseLink(URI url) {
        String[] pathParts = url.getPath().split("/");
        return pathParts.length >= 3 ? new GithubLinkParserResult(pathParts[1], pathParts[2]) : null;
    }
}
