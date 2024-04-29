package com.foodtec.pingpong.util;

import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

/**
 * Utility class for working with {@link ResponseEntity} objects.
 */
public class ResponseEntityUtil {
    /**
     * Common strategy to determine if a response entity is successful.
     *
     * @param responseEntity the response entity
     * @return true if the response entity is successful, false otherwise
     */
    public static boolean isSuccessful(ResponseEntity<?> responseEntity) {
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    /**
     * Get the body of a response entity or throw an exception if the response entity is not successful.
     * @param responseEntity - the response entity
     * @param exceptionSupplier - the exception supplier
     * @return - the body of the response entity
     */
    public static <T> T getBodyOrThrow(ResponseEntity<T> responseEntity, Supplier<RuntimeException> exceptionSupplier) {
        if (isSuccessful(responseEntity)){
            return responseEntity.getBody();
        } else {
            throw exceptionSupplier.get();
        }
    }
}
