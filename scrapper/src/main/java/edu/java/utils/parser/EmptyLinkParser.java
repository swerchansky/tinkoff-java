package edu.java.utils.parser;

import edu.java.utils.parser.result.LinkParserResult;
import java.net.URI;

public final class EmptyLinkParser implements LinkParser {
    @Override
    public LinkParserResult parseLink(URI url) {
        return null;
    }
}
