package edu.java.utils.parser;

import edu.java.utils.parser.result.GithubLinkParserResult;
import edu.java.utils.parser.result.StackOverflowlinkParserResult;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkParserTest {

    @Test
    public void parsesGithubLinkCorrectly() throws URISyntaxException {
        URI githubUri = new URI("https://github.com/user/repo");
        assertThat(LinkParser.parseLinkInfo(githubUri)).isInstanceOf(GithubLinkParserResult.class);
    }

    @Test
    public void parsesStackOverflowLinkCorrectly() throws URISyntaxException {
        URI stackOverflowUri = new URI("https://stackoverflow.com/questions/123");
        assertThat(LinkParser.parseLinkInfo(stackOverflowUri)).isInstanceOf(StackOverflowlinkParserResult.class);
    }

    @Test
    public void returnsEmptyLinkParserForUnknownHost() throws URISyntaxException {
        URI unknownUri = new URI("https://unknown.com/path");
        assertThat(LinkParser.parseLinkInfo(unknownUri)).isNull();
    }
}
