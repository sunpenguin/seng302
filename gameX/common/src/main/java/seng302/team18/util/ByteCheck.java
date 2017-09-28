package seng302.team18.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class stores the methods that are used for dealing with byte errors.
 */
public class ByteCheck {

    /**
     * Convert an int to a byte array of length 4. Endianness is little endian
     *
     * @param value value to convert
     * @return converted byte array
     */
    public static byte[] intToByteArray(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(value);

        byte[] result = new byte[4];
        System.arraycopy(buffer.array(), 0, result, 0, 4);

        for (int i = 0; i < result.length / 2; i++) {
            byte temp = result[i];
            result[i] = result[result.length - i - 1];
            result[result.length - i - 1] = temp;
        }

        return result;
    }


    /**
     * Convert a short to a byte array of length 2. Endianness is little endian
     *
     * @param value value to convert
     * @return converted byte array
     */
    public static byte[] shortToByteArray(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.putShort(value);

        byte[] result = new byte[2];
        System.arraycopy(buffer.array(), 0, result, 0, 2);

        for (int i = 0; i < result.length / 2; i++) {
            byte temp = result[i];
            result[i] = result[result.length - i - 1];
            result[result.length - i - 1] = temp;
        }

        return result;
    }


    /**
     * Convert a int to a byte array of length Short.BYTES. Endianness is little endian
     *
     * @param value to convert
     * @return unsigned short byte array
     */
    public static byte[] intToUShort(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.putChar((char) value).array();
    }


    public static byte[] getCurrentTime6Bytes() {
        return longTo6ByteArray(System.currentTimeMillis());
    }



    public static byte[] longTo6ByteArray(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        byte[] valueBytes = new byte[6];
        System.arraycopy(buffer.array(), 2, valueBytes, 0, 6);

        for (int i = 0; i < valueBytes.length / 2; i++) {
            byte temp = valueBytes[i];
            valueBytes[i] = valueBytes[valueBytes.length - i - 1];
            valueBytes[valueBytes.length - i - 1] = temp;
        }

        return valueBytes;
    }


    /**
     * The method which converts the byte array (little endian) to integer.
     * Corrects if there are less than 4 bytes.
     *
     * @param bytes  The whole byte array in little endian
     * @param index  The index where the split begins
     * @param length How long the split will be
     * @return The value that is calculated from the function.
     */
    public static int byteToShort(byte[] bytes, int index, int length) {
        int finalValue;
        byte[] b = new byte[2];

        if (length < 2) {
            int tempLen = 2 - length;
            for (int i = 0; i < length; i++) {
                b[i] = bytes[index + i];
            }
            for (int i = 0; i < tempLen; i++) {
                b[length + i] = 0;
            }
            // May run into trouble with unsigned things and need to work with them
            finalValue = ByteBuffer.wrap(b, 0, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
        } else {
            finalValue = ByteBuffer.wrap(bytes, index, length).order(ByteOrder.LITTLE_ENDIAN).getShort();
        }
        return finalValue;
    }


    /**
     * The method which converts the byte array (little endian) to integer.
     * Corrects if there are less than 4 bytes.
     *
     * @param bytes  The whole byte array in little endian
     * @param index  The index where the split begins
     * @param length How long the split will be
     * @return The value that is calculated from the function.
     */
    public static int byteToInt(byte[] bytes, int index, int length) {
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
     *
     * @param bytes  The whole byte array in little endian
     * @param index  The index where the split begins
     * @param length How long the split will be
     * @return The value that is calculated from the function.
     */
    public static long byteToLong(byte[] bytes, int index, int length) {
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
     * Method to convert a byte array to little endian format.
     *
     * @param bytes  a byte array to be converted
     * @param length length of the byte array
     * @return the byte array in little endian format
     */
    public static byte[] littleEndian(byte[] bytes, int length) {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.array();
    }
}
