package com.roshan.learnings.FileUploadAppServer;

public class Adaptor {
    public static UserDto convertToDto (User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .mobile(user.getMobile())
                .username(user.getUsername())
                .build();
    }
}
