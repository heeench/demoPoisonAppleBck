package ru.TavernOfTravels.demo.response;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ResponseBody
public class ResponseHandler {
    private ResponseHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseEntity<Object> responseBuilder (
            String message, HttpStatus httpStatus, Object responseObject
    ) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", message);
        response.put("httpStatus", httpStatus);
        response.put("data", responseObject);

        return new ResponseEntity<>(response, httpStatus);
    }
}
