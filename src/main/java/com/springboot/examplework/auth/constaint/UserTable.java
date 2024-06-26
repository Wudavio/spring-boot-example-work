package com.springboot.examplework.auth.constaint;

// 為切分多平台登入，將使用者資料表名稱定義為enum
public enum UserTable {
    PLATFORM_USER("platform_user");

    private String value;

    UserTable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
