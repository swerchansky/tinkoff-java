package edu.java.controller;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.ApiErrorResponse;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface ScrapperController {
    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    @PostMapping(value = "/tg-chat/{id}")
    void addChat(@PathVariable long id);

    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удален"),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Чат не существует", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    @DeleteMapping(value = "/tg-chat/{id}")
    void deleteChat(@PathVariable long id);

    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "ССылки успешно получены", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ListLinksResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    @GetMapping(value = "/links")
    ListLinksResponse getLinks(@RequestHeader(value = "Tg-Chat-Id") long id);

    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = LinkResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    @PostMapping(value = "/links")
    LinkResponse addLink(
        @RequestHeader(value = "Tg-Chat-Id")
        long id,
        @RequestBody
        AddLinkRequest link
    );

    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Сылка успешно добавлена", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = LinkResponse.class))
        }),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        }),
        @ApiResponse(responseCode = "404", description = "Ссылка не найдена", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    @DeleteMapping(value = "/links")
    LinkResponse deleteLink(
        @RequestHeader(value = "Tg-Chat-Id")
        long id,
        @RequestBody
        RemoveLinkRequest link
    );
}
