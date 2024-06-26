package com.springboot.examplework.core.traits;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

@Component
public class ResponseServiceTrait {
    private static final Map<String, Map<String, String>> codes = new HashMap<>();
    private Map<String, Object> result = new HashMap<>();

    static {
        Map<String, String> commonCodes = new HashMap<>();
        commonCodes.put("SERVER_ERROR", "0101");
        commonCodes.put("API_URL_NOT_FOUNT", "0102");
        commonCodes.put("OPERATE_FAILED", "0103");
        commonCodes.put("DATA_NOT_FOUND", "0104");
        commonCodes.put("REQUEST_FORMAT_ERROR", "0105");
        commonCodes.put("DATABASE_CONNECT_ERROR", "0106");
        commonCodes.put("DATABASE_OPERATE_ERROR", "0107");
        codes.put("common", commonCodes);

        Map<String, String> authCodes = new HashMap<>();
        authCodes.put("UNAUTHORIZED", "0201");
        authCodes.put("ACCOUNT_NOT_FOUND", "0202");
        authCodes.put("ACCOUNT_REGISTERED", "0203");
        authCodes.put("TOKEN_EXPIRED", "0204");
        codes.put("auth", authCodes);

        Map<String, String> platformUsersCodes = new HashMap<>();
        authCodes.put("Duplicate_Account", "0301");
        codes.put("platformUsers", platformUsersCodes);
    }

    public String getErrorCode(String category, String code) {
        return codes.getOrDefault(category, Collections.emptyMap()).getOrDefault(code, "0000");
    }

    public Map<String, Object> responseStatusData(boolean success, String message, String errorCode) {
        this.result.put("success", success);
        this.result.put("message", message);
        this.result.put("errorCode", errorCode);
        return result;
    }

    public Map<String, Object> responseStatusData(boolean success) {
        this.result.put("success", success);
        this.result.put("message", "操作成功!");
        return result;
    }

    public Map<String, Object> responseStatusData(boolean success, long id) {
        this.result.put("success", success);
        this.result.put("message", "操作成功!");
        this.result.put("data", id);
        return result;
    }

    public Map<String, Object> responseStatusData(boolean success, Object object) {
        this.result.put("success", success);
        this.result.put("message", "操作成功!");
        this.result.put("object", object);
        return result;
    }
}
