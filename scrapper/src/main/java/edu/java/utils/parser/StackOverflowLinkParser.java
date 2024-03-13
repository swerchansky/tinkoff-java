package edu.java.utils.parser;

import edu.java.utils.parser.result.LinkParserResult;
import edu.java.utils.parser.result.StackOverflowlinkParserResult;
import java.net.URI;

public final class StackOverflowLinkParser implements LinkParser {
    @Override
    @SuppressWarnings("MagicNumber")
    public LinkParserResult parseLink(URI url) {
        String[] pathParts = url.getPath().split("/");
        if (pathParts.length >= 3 && "questions".equals(pathParts[1])) {
            String urlString = url.toString();
            String questionId = pathParts[2];
            URI newUrl = URI.create(urlString.substring(0, urlString.indexOf(questionId) + questionId.length()));
            try {
                return new StackOverflowlinkParserResult(newUrl, Long.parseLong(questionId));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
