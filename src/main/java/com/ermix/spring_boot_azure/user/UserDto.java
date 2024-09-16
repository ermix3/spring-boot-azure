package com.ermix.spring_boot_azure.user;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(Long id) implements Serializable {
}