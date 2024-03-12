package edu.java.utils.parser;

import edu.java.utils.parser.result.GithubLinkParserResult;
import edu.java.utils.parser.result.LinkParserResult;
import java.net.URI;

public final class GithubLinkParser implements LinkParser {
    @Override
    @SuppressWarnings("MagicNumber")
    public LinkParserResult parseLink(URI url) {
        String[] pathParts = url.getPath().split("/");
        return pathParts.length >= 3 ? new GithubLinkParserResult(pathParts[1], pathParts[2]) : null;
    }
}
