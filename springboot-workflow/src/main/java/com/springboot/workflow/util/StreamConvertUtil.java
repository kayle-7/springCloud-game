package com.springboot.workflow.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by zx
 */
public class StreamConvertUtil {

    /**
     * @description inputStream转outputStream
     * @param in    InputStream
     * @return java.io.ByteArrayOutputStream
     */
    public static ByteArrayOutputStream parse(InputStream in) throws IOException {

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

    /**
     * @description outputStream转inputStream
     * @param out   OutputStream
     * @return java.io.ByteArrayInputStream
     */
    public static ByteArrayInputStream parse(OutputStream out) {
        return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());
    }

    /**
     * @description inputStream转String
     * @param in    InputStream
     * @return java.lang.String
     */
    public static String parse_String(InputStream in) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return new String(swapStream.toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * @description OutputStream 转String.存在乱码,不使用
     * @param out   OutputStream
     * @return java.lang.String
     */
    @Deprecated
    public static String parse_String(OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }

    /**
     * @description String转inputStream
     * @param in    String
     * @return java.io.ByteArrayInputStream
     */
    private static ByteArrayInputStream parse_inputStream(String in) {
        return new ByteArrayInputStream(in.getBytes());
    }

    /**
     * @description String 转outputStream
     * @param in    String
     * @return java.io.ByteArrayOutputStream
     */
    public static ByteArrayOutputStream parse_outputStream(String in) throws IOException {
        return parse(parse_inputStream(in));
    }
}
