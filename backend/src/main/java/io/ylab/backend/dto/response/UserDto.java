package io.ylab.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing a user.
 *
 * <p>This class is used to transfer user-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Schema(description = "Модель данных пользователя")
@Builder
@Getter
public class UserDto {
        @Schema(description = "id пользователя")
        private Long id;
        @Schema(description = "Имя пользователя")
        private String name;
        @Schema(description = "Токен авторизации")
        @Setter
        private String token;
}
