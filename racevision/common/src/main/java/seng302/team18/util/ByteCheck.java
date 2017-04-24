package seng302.team18.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class stores the methods that are used for dealing with byte errors.
 * @author Sunguin Peng
 */
public class ByteCheck {


    public static byte[] intToByteArray(int a) {
        byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putInt(a);
        return bytes;
    }

    public static byte[] doubleToByteArray(double a) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(a);
        return bytes;
    }

    public static byte[] longToByteArray(long a) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putLong(a);
        return bytes;
    }

    public static byte[] shortToByteArray(short a) {
        byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putInt(a);
        return bytes;

    }

    /**
     * The method which converts the byte array (little endian) to integer.
     * Corrects if there are less than 4 bytes.
     * @param bytes The whole byte array in little endian
     * @param index The index where the split begins
     * @param length How long the split will be
     * @return The value that is calculated from the function.
     */
    public static int byteToIntConverter(byte[] bytes, int index, int length) {
        int finalValue;
        byte[] b = new byte[4];

        if (length < 4) {
            int tempLen = 4 - length;
            for (int i = 0; i < length; i++) {
                b[i] = bytes[index + i];
            }
            for (int i = 0; i < tempLen; i++) {
                b[length + i] = 0;
            }
            // May run into trouble with unsigned things and need to work with them
            finalValue = ByteBuffer.wrap(b, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        } else {
            finalValue = ByteBuffer.wrap(bytes, index, length).order(ByteOrder.LITTLE_ENDIAN).getInt();
        }
        return finalValue;
    }

    /**
     * The method which converts the byte array (little endian) to long.
     * Corrects if there are less than 8 bytes.
     * @param bytes The whole byte array in little endian
     * @param index The index where the split begins
     * @param length How long the split will be
     * @return The value that is calculated from the function.
     */
    public static long byteToLongConverter(byte[] bytes, int index, int length) {
        long finalValue;
        byte[] b = new byte[8];

        if (length < 8) {
            int tempLen = 8 - length;
            for (int i = 0; i < length; i++) {
                b[i] = bytes[index + i];
            }
            for (int i = 0; i < tempLen; i++) {
                b[length + i] = 0;
            }
            // May run into trouble with unsigned things and need to work with them
            finalValue = ByteBuffer.wrap(b, 0, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
        } else {
            finalValue = ByteBuffer.wrap(bytes, index, length).order(ByteOrder.LITTLE_ENDIAN).getLong();
        }
        return finalValue;
    }

    /**
     * Method to conver a byte array to little edian format.
     * @param bytes a byte array to be converted
     * @param length length of the byte array
     * @return the byte array in little edian format
     */
    public static byte[] convertToLittleEndian(byte[] bytes, int length) {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.array();
    }
}
