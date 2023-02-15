package com.chatapp.utility;

import org.junit.Test;

public class ByteConverterTest  {

    @Test
    public static void main(String[] args) {
        try {
            testStringToByteArrayConversion();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("ByteConverterTest finished.");
        }
    }
  
    public static void testStringToByteArrayConversion() throws Exception {
        String expectedString = "Hello this is an example message\n\t\\.";

        byte[] arr = ByteConverter.stringToByteArray(expectedString);
        String actualString = ByteConverter.byteArrayToString(arr);

        assert expectedString.equals(actualString);
    }
  }