package edu.java.utils.parser;

import edu.java.utils.parser.result.LinkParserResult;
import edu.java.utils.parser.result.StackOverflowlinkParserResult;
import java.net.URI;

public final class StackOverflowLinkParser implements LinkParser {
    @Override
    public LinkParserResult parseLink(URI url) {
        String[] pathParts = url.getPath().split("/");
        return (pathParts.length >= 2 && "questions".equals(pathParts[1]))
            ? new StackOverflowlinkParserResult(Long.parseLong(pathParts[2]))
            : null;
    }
}
