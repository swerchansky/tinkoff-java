package edu.java.controller;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.ApiErrorResponse;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface ScrapperController {
    @Operation(
            operationId = "tgChatPost",
            summary = "Зарегистрировать чат",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)
                            )
                    })
            }
    )
    @RequestMapping(
            value = "/tg-chat/{id}",
            method = POST,
            produces = APPLICATION_JSON_VALUE
    )
    void addChat(
            @PathVariable
            @Parameter(name = "id", required = true)
            long id
    );

    @Operation(
            operationId = "tgChatDelete",
            summary = "Удалить чат",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Чат успешно удален"),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "Чат не существует", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)
                            )
                    })
            }
    )
    @RequestMapping(
            value = "/tg-chat/{id}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE
    )
    void deleteChat(
            @PathVariable
            @Parameter(name = "id", required = true)
            long id
    );

    @Operation(
            operationId = "linksGet",
            summary = "Получить все отслеживаемые ссылки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ССылки успешно получены", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ListLinksResponse.class)
                            )
                    }),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)
                            )
                    })
            }
    )
    @RequestMapping(
            value = "/links",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    ListLinksResponse getLinks(
            @RequestHeader(value = "Tg-Chat-Id")
            @Parameter(name = "Tg-Chat-Id", required = true)
            long id
    );

    @Operation(
            operationId = "linksPost",
            summary = "Добавить отслеживание ссылки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LinkResponse.class)
                            )
                    }),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)
                            )
                    })
            }
    )
    @RequestMapping(
            value = "/links",
            method = POST,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    LinkResponse addLink(
            @RequestHeader(value = "Tg-Chat-Id")
            @Parameter(name = "Tg-Chat-Id", required = true)
            long id,
            @RequestBody
            @Parameter(required = true, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AddLinkRequest.class)
                    )
            })
            AddLinkRequest link
    );

    @Operation(
            operationId = "linksDelete",
            summary = "Убрать отслеживание ссылки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сылка успешно добавлена", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LinkResponse.class)
                            )
                    }),
                    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)
                            )
                    }),
                    @ApiResponse(responseCode = "404", description = "Ссылка не найдена", content = {
                            @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)
                            )
                    })
            }
    )
    @RequestMapping(
            value = "/links",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE
    )
    LinkResponse deleteLink(
            @RequestHeader(value = "Tg-Chat-Id")
            @Parameter(name = "Tg-Chat-Id", required = true)
            long id,
            @RequestBody
            @Parameter(required = true, content = {
                    @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoveLinkRequest.class)
                    )
            })
            RemoveLinkRequest link
    );
}
