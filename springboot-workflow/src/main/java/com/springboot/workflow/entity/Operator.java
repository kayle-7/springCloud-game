package com.springboot.workflow.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by zx
 */
@Data
public class Operator implements Serializable{
    private static final String separator = ":";

    @NotNull(message="operator id is null.")
    String id;
    String name;

    public String toString() {
        if (StringUtils.isEmpty(getName())) {
            return getId();
        }
        return String.format("%s%s%s", getId(), ":", getName());
    }

    public static Operator getSystem() {
        return new Operator("0", "system");
    }

    public static Operator valueOf(String value) {
        if (value == null) {
            return null;
        }
        int index = value.indexOf(":");
        if (index < 0) {
            return new Operator(value, "");
        }
        return new Operator(value.substring(0, index), value.substring(index + ":".length()));
    }

    public String canonicalEngName() {
        if (Pattern.matches("^[a-zA-Z0-9_]+[/(]{1}[\\u4E00-\\u9FA5a-zA-Z]+[/)]{1}", this.name)) {
            return this.name.split("\\(")[0];
        }
        return this.name;
    }

    public Operator(String id, String name) {
        this.id = id; this.name = name;
    }

    public Operator() {
    }
}