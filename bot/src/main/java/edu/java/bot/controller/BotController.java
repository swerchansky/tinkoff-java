package edu.java.bot.controller;

import edu.java.bot.controller.dto.ApiErrorResponse;
import edu.java.bot.controller.dto.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface BotController {
    @Operation(responses = {
        @ApiResponse(responseCode = "200", description = "Обновление обработано"),
        @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    @PostMapping(value = "/update")
    void update(
        @RequestBody
        @Parameter(name = "LinkUpdateRequest", required = true)
        LinkUpdateRequest request
    );
}
