package edu.java.utils.parser;

import edu.java.utils.parser.result.StackOverflowlinkParserResult;
import edu.java.utils.parser.result.linkParserResult;
import java.net.URI;

public final class StackOverflowLinkParser implements LinkParser {
    @Override
    public linkParserResult parseLink(URI url) {
        String[] pathParts = url.getPath().split("/");
        return (pathParts.length >= 2 && "questions".equals(pathParts[0]))
            ? new StackOverflowlinkParserResult(Long.parseLong(pathParts[1]))
            : null;
    }
}
