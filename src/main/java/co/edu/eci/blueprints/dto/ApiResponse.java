package co.edu.eci.blueprints.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Envoltura genérica de respuesta para homogeneizar todos los endpoints de la API.
 *
 * <p>Formato JSON: {@code {"code": 200, "message": "execute ok", "data": { ... }}}.
 * El campo {@code code} coincide con el código de estado HTTP.</p>
 *
 * @param <T> tipo del dato de la respuesta ({@code data}).
 */
public record ApiResponse<T>(int code, String message, T data) {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(200, "execute ok", data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "created", data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> accepted(T data) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(202, "accepted", data));
    }

    public static ResponseEntity<ApiResponse<Void>> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, message, null));
    }

    public static ResponseEntity<ApiResponse<Void>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, message, null));
    }

    public static ResponseEntity<ApiResponse<Void>> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(409, message, null));
    }
}
