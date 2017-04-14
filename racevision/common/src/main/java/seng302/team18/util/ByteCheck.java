package seng302.team18.util;

import java.nio.ByteBuffer;

/**
 * This class stores the methods that are used for dealing with byte errors.
 * @author Sunguin Peng
 */
public class ByteCheck {

    /**
     * The method which converts the byte array to integer.
     * Corrects if there are less than 4 bytes.
     * @param bytes The whole byte array
     * @param index The index where the split begins
     * @param length How long the split will be
     * @return The value that is calculated from the function.
     */
    public static int ByteToIntConverter(byte[] bytes, int index, int length) {
        int finalValue;
        byte[] b = new byte[4];

        if (length < 4) {
            int tempLen = 4 - length;
            for (int i = 0; i < tempLen; i++) {
                b[i] = 0;
            }
            for (int i = 0; i < length; i++) {
                b[tempLen + i] = bytes[index + i];
            }
            finalValue = ByteBuffer.wrap(b, 0, 4).getInt();
        } else {
            finalValue = ByteBuffer.wrap(bytes, index, length).getInt();
        }
        return finalValue;
    }
}
