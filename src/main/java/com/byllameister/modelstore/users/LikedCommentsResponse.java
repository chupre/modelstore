package com.byllameister.modelstore.users;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LikedCommentsResponse {
    List<Long> commentId;
}
