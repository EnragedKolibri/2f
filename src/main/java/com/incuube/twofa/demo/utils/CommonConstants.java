package com.incuube.twofa.demo.utils;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
public class CommonConstants {

    public static String templatePattern = "(^[a-zA-Z0-9\\s,.!?\\-]*)(\\[[*#]{4,8}\\])([a-zA-Z0-9\\s,.!?\\-]*$)";
    public static String alphabet = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";



}
