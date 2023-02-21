package com.chatapp.protocol;

import java.util.ArrayList;

import com.chatapp.utility.ByteConverter;

/*
 * This class is used to represent a message sent between the client and server.
 */
public class Message {
    private byte version;
    private Operation operation;
    private Exception exception;
    private ArrayList<byte[]> arguments;

    public Message(byte version, Operation operation, Exception exception, ArrayList<byte[]> arguments) throws IllegalArgumentException {
        this.version = version;
        this.operation = operation;
        this.exception = exception;
        this.arguments = arguments;
    }

    public byte getVersion() {
        return version;
    }
    public Operation getOperation() {
        return operation;
    }
    public Exception getException() {
        return exception;
    }
    public ArrayList<byte[]> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "Ver=" + version + " Exc=" + exception + " " + operation + " " + ArrayListByteArraysToString(arguments);
    }

    /* 
     * Utility function to convert an ArrayList of byte arrays to a string
     * @param args the ArrayList of byte arrays to convert
     * @return the string representation of the ArrayList of byte arrays
    */
    private static String ArrayListByteArraysToString(ArrayList<byte[]> args) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (byte[] arg : args) {
            if (first) {
                first = false;
            } else {
                result.append(' ');
            }
            result.append(ByteConverter.byteArrayToString(arg));
        }
        return result.toString();
    }
}
