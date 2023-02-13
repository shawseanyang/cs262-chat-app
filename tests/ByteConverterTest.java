package tests;

import java.util.UUID;

import utility.ByteConverter;

public class ByteConverterTest  {
    public static void main(String[] args) {
        try {
            testUUIDConversion();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("ByteConverterTest finished.");
        }
    }
  
    public static void testUUIDConversion() throws Exception {
        UUID expectedUUID = UUID.randomUUID();

        byte[] byteUUID = ByteConverter.UUIDToByteArray(expectedUUID);
        UUID actualUUID = ByteConverter.byteArrayToUUID(byteUUID);

        assert expectedUUID.equals(actualUUID);
    }
  
    public static void testStringToByteArrayConversion() throws Exception {
        String expectedString = "Hello this is an example message\n\t\\.";

        byte[] arr = ByteConverter.stringToByteArray(expectedString);
        String actualString = ByteConverter.byteArrayToString(arr);

        assert expectedString.equals(actualString);
    }
  }