package edu.java.utils.parser;

import edu.java.utils.parser.result.LinkParserResult;
import java.net.URI;

public sealed interface LinkParser permits GithubLinkParser, StackOverflowLinkParser, EmptyLinkParser {
    LinkParserResult parseLink(URI url);

    static LinkParserResult parseLinkInfo(URI url) {
        String host = url.getHost();
        LinkParser parser;
        if (host == null) {
            return null;
        }
        if (host.contains("github")) {
            parser = new GithubLinkParser();
        } else if (host.contains("stackoverflow")) {
            parser = new StackOverflowLinkParser();
        } else {
            parser = new EmptyLinkParser();
        }
        return parser.parseLink(url);
    }
}
