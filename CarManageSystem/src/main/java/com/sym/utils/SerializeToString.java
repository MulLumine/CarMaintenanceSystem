package com.sym.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Component
public class SerializeToString {
    public static String serializeToStringByBase64(Object obj) {
        byte[] bytes = obj.toString().getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

}
