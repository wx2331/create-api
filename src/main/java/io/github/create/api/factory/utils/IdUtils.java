package io.github.create.api.factory.utils;

import java.util.UUID;

public class IdUtils {
    public static String simpleUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
