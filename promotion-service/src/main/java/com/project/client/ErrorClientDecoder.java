package com.project.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.ExceptionMessage;
import com.project.exception.ProductNotFoundException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
public class ErrorClientDecoder implements ErrorDecoder {

    private static final ErrorDecoder errorDecoder = new Default();

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String invokeMethod, Response response) {
        try {
            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                ExceptionMessage message = mapper.readValue(Util.toString(response.body().asReader()), ExceptionMessage.class);
                return new ProductNotFoundException(message.getMessage());
            }

        } catch (IOException e) {
            log.info("Something goes wrong with Feign Util", e);
        }

        return errorDecoder.decode(invokeMethod, response);
    }

}
