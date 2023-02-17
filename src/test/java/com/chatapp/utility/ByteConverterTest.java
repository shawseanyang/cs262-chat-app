package com.chatapp.utility;

import org.junit.Test;

public class ByteConverterTest  {
    @Test
    public void convert_StringtoByteArray_ThenBackToString() throws Exception {
        String expectedString = "Hello this is an example message\n\t\\.";

        byte[] arr = ByteConverter.stringToByteArray(expectedString);
        String actualString = ByteConverter.byteArrayToString(arr);

        assert expectedString.equals(actualString);
    }
  }