package com.byllameister.modelstore.products.interaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentUserDto {
    private Long id;
    private String username;
    private String avatarUrl;
}
