package com.kayle.server.util;

import java.io.*;

/**
 * Created by zx
 */
public class SerializeUtil {
    /**
     * Serialize the given object to a byte array.
     * @param object the object to serialize
     * @return an array of bytes representing the object in a portable fashion
     */
    public static byte[] serialize(Object object) {

        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                baos.close();
            } catch (IOException ex) {
                throw new IllegalStateException("Failed to close ByteArrayOutputStream or ObjectOutputStream", ex);
            }
        }
        return baos.toByteArray();
    }

    /**
     * Deserialize the byte array into an object.
     * @param bytes a serialized object
     * @return the result of deserializing the bytes
     */
    public static Object deserialize(byte[] bytes) {
        ObjectInputStream ois = null;
        if (bytes == null) {
            return null;
        }
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    throw new IllegalStateException("Failed to close ObjectInputStream", ex);
                }
            }
        }
    }
}