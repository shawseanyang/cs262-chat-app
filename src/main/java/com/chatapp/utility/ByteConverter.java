package com.chatapp.utility;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * This class is used to convert between byte arrays and other types.
 */
public class ByteConverter {
  public static String byteArrayToString(byte[] bytes) {
    return new String(bytes);
  }

  public static byte[] stringToByteArray(String string) {
    return string.getBytes();
  }

  public static byte[] byteArrayListToByteArray(ArrayList<Byte> bytes) {
    byte[] result = new byte[bytes.size()];
    for (int i = 0; i < bytes.size(); i++) {
      result[i] = bytes.get(i);
    }
    return result;
  }

  public static byte[][] splitByteArray(byte[] array, byte separator) {
      if (array.length == 0) {
        return new byte[0][0];
      }

      int separatorCount = 0;
      for (byte b : array) {
          if (b == separator) {
              separatorCount++;
          }
      }

      byte[][] result = new byte[separatorCount + 1][];
      int index = 0;
      int startIndex = 0;
      for (int i = 0; i < array.length; i++) {
          if (array[i] == separator) {
              result[index] = Arrays.copyOfRange(array, startIndex, i);
              startIndex = i + 1;
              index++;
          }
      }
      result[index] = Arrays.copyOfRange(array, startIndex, array.length);
      return result;
  }

}
