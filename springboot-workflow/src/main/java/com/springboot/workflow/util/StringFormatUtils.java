package com.springboot.workflow.util;

import org.apache.commons.lang3.StringUtils;

public class StringFormatUtils {
    public static String getOriginalAdmins(String commandAdmins) {
        if (StringUtils.isNotBlank(commandAdmins)) {
            String originalAdmins = StringUtils.trim(commandAdmins);
            if (StringUtils.startsWith(originalAdmins, ",")) {
                originalAdmins = originalAdmins.replaceFirst(",", "");
            }
            if (StringUtils.endsWith(originalAdmins, ",")) {
                originalAdmins = StringUtils.substring(originalAdmins, 0, originalAdmins.lastIndexOf(","));
            }
            return originalAdmins;
        }
        return "";
    }

    public static String getCommandAdmins(String originalAdmins) {
        if (StringUtils.isNotBlank(originalAdmins)) {
            String commandAdmins = StringUtils.trim(originalAdmins);
            if (!StringUtils.startsWith(commandAdmins, ",")) {
                commandAdmins = ",".concat(commandAdmins);
            }
            if (!StringUtils.endsWith(commandAdmins, ",")) {
                commandAdmins = commandAdmins.concat(",");
            }
            return commandAdmins;
        }
        return "";
    }
}