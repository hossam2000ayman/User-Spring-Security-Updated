package com.example.springsecurityjwt.entity.enums;

import lombok.Getter;

@Getter
public enum Permissions {
    USER_READ("user::read"), USER_DELETE("user::delete"), USER_UPDATE("user:update"), ADMIN_CREATE("admin::create"), ADMIN_READ("admin::read"), ADMIN_DELETE("admin::delete"), ADMIN_UPDATE("admin::update");


    private final String permission;


    Permissions(String permission) {
        this.permission = permission;
    }
}
