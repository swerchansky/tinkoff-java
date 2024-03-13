package edu.java.utils.parser;

import edu.java.utils.parser.result.GithubLinkParserResult;
import edu.java.utils.parser.result.LinkParserResult;
import java.net.URI;

public final class GithubLinkParser implements LinkParser {
    @Override
    @SuppressWarnings("MagicNumber")
    public LinkParserResult parseLink(URI url) {
        String[] pathParts = url.getPath().split("/");
        if (pathParts.length >= 3) {
            String urlString = url.toString();
            URI newUrl = URI.create(urlString.substring(0, urlString.indexOf(pathParts[2]) + pathParts[2].length()));
            return new GithubLinkParserResult(newUrl, pathParts[1], pathParts[2]);
        }
        return null;
    }
}
