package edu.java.utils.parser;

import edu.java.utils.parser.result.linkParserResult;
import java.net.URI;

public final class EmptyLinkParser implements LinkParser {
    @Override
    public linkParserResult parseLink(URI url) {
        return null;
    }
}
