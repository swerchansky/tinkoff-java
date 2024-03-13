package edu.java.utils.parser;

import edu.java.utils.parser.result.GithubLinkParserResult;
import edu.java.utils.parser.result.LinkParserResult;
import edu.java.utils.parser.result.StackOverflowlinkParserResult;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkParserTest {

    @Test
    @DisplayName("parse github link correctly")
    public void parsesGithubLinkCorrectly() throws URISyntaxException {
        URI githubUri = new URI("https://github.com/user/repo");

        LinkParserResult result = LinkParser.parseLinkInfo(githubUri);

        assertThat(result).isInstanceOf(GithubLinkParserResult.class);
        assertThat((GithubLinkParserResult) result).isEqualTo(new GithubLinkParserResult(githubUri, "user", "repo"));
    }

    @Test
    @DisplayName("parse github link with invalid arguments")
    public void parsesGithubLinkWithInvalidArguments() throws URISyntaxException {
        URI githubUri = new URI("https://github.com/user");

        LinkParserResult result = LinkParser.parseLinkInfo(githubUri);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("parse stackoverflow link correctly")
    public void parsesStackOverflowLinkCorrectly() throws URISyntaxException {
        URI stackOverflowUri = new URI("https://stackoverflow.com/questions/123");

        LinkParserResult result = LinkParser.parseLinkInfo(stackOverflowUri);

        assertThat(result).isInstanceOf(StackOverflowlinkParserResult.class);
        assertThat((StackOverflowlinkParserResult) result).isEqualTo(new StackOverflowlinkParserResult(
            stackOverflowUri,
            123L
        ));
    }

    @Test
    @DisplayName("parse stackoverflow link with invalid arguments")
    public void parsesStackOverflowLinkWithInvalidArguments() throws URISyntaxException {
        URI stackOverflowUri = new URI("https://stackoverflow.com/questions");

        LinkParserResult result = LinkParser.parseLinkInfo(stackOverflowUri);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("parse stackoverflow link with invalid id")
    public void parsesStackOverflowLinkWithInvalidId() throws URISyntaxException {
        URI stackOverflowUri = new URI("https://stackoverflow.com/questions/id");

        LinkParserResult result = LinkParser.parseLinkInfo(stackOverflowUri);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("unknown host returns null link parser result")
    public void returnsEmptyLinkParserForUnknownHost() throws URISyntaxException {
        URI unknownUri = new URI("https://unknown.com/path");
        assertThat(LinkParser.parseLinkInfo(unknownUri)).isNull();
    }

    @Test
    @DisplayName("link without host")
    public void linkWithoutHost() throws URISyntaxException {
        URI unknownUri = new URI("unknown");
        assertThat(LinkParser.parseLinkInfo(unknownUri)).isNull();
    }
}
