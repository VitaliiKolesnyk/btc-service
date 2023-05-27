package com.api.controller;

import com.api.client.CoinApiClient;
import com.api.exception.SubscriberAlreadySubscribedException;
import com.api.mapper.ApiResponseToExchangeRateMapper;
import com.api.model.ApiResponse;
import com.api.model.Response;
import com.api.model.Subscriber;
import com.api.service.EmailService;
import com.api.service.FileService;
import com.api.util.EmailValidator;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "GSES2 BTC application",
                version = "1.0.0"
        )
)
@RestController
@RequestMapping("/api")
public class BtcController {

    private final CoinApiClient coinApiClient;

    private final FileService fileService;

    private final EmailService emailService;

    private final ApiResponseToExchangeRateMapper mapper;

    private final EmailValidator emailValidator;

    public BtcController(CoinApiClient coinApiClient, FileService fileService, EmailService emailService, ApiResponseToExchangeRateMapper mapper, EmailValidator emailValidator) {
        this.coinApiClient = coinApiClient;
        this.fileService = fileService;
        this.emailService = emailService;
        this.mapper = mapper;
        this.emailValidator = emailValidator;
    }

    @Operation(summary = "Get current BTC to UAH rate")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "BTC to UAH rate received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @GetMapping("/rate")
    public ApiResponse getBtcToUahRate() {
        return coinApiClient.getBitcoinPrice();
    }

    @Operation(summary = "Add email to subscriber email distibution list")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email added"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Email already present in subscription list")
    })
    @PostMapping(value = "/subscription", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> subscribe(@Parameter(name = "email", description = "Email address, which should be subscribed", required = true)
                                            @RequestBody MultiValueMap<String, String> formData) throws SubscriberAlreadySubscribedException, IOException {
        String email = formData.getFirst("email");

        emailValidator.validate(email);

        fileService.addSubscriber(new Subscriber(email));

        return ResponseEntity.ok(Response.EMAIL_ADDED.getResponse());
    }

    @Operation(summary = "Send emails to subscribers")
    @PostMapping(value = "/sendEmails")
    public ResponseEntity<String> sendEmails() {
        ApiResponse apiResponse = getBtcToUahRate();
        List<Subscriber> subscribers = fileService.getSubscribers();

        emailService.sendAll(subscribers, mapper.map(apiResponse));

        return ResponseEntity.ok(Response.EMAILS_SENT.getResponse());
    }
}
