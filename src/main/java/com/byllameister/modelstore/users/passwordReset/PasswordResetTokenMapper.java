package com.byllameister.modelstore.users.passwordReset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenMapper {
    @Mapping(target = "userId", source = "user.id")
    PasswordResetTokenResponse toDto(PasswordResetToken passwordResetToken);
}
