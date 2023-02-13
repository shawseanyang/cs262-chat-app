package tests;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.Arrays;

import protocol.*;

public class MarshallerTest {
  public static void main(String[] args) {
    try {
      testEscaping();
      testMarshall();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.out.println("MarshallerTest finished.");
    }
  }

  // Tests only the escaping functions
  public static void testEscaping() throws Exception {
    byte version = Constants.CURRENT_VERSION;
    Operation operation = Operation.CREATE_ACCOUNT;
    protocol.Exception exception = protocol.Exception.NONE;

    // define an account name that has all the special characters to be escaped
    String username = "weird\taccount\nname\\huh";
    byte[] usernameBytes = username.getBytes();
    ArrayList<byte[]> args = new ArrayList<byte[]>();
    args.add(usernameBytes);

    // set up marshalled messages
    byte[] expectedMarshalledUsername = "weird\\taccount\\nname\\huh".getBytes();
    byte[] actualMarshalledMessage = Marshaller.marshall(new Message(version, operation, exception, args));
    byte[] actualMarshalledUsername = Arrays.copyOfRange(actualMarshalledMessage, Constants.CONTENT_POSITION, actualMarshalledMessage.length-1);

    // set up unmarshalled messages
    byte[] expectedUnmarshalledUsername = usernameBytes;
    byte[] actualUnmarshalledUsername = Marshaller.unmarshall(actualMarshalledMessage).getArguments().get(0);

    assert Arrays.equals(expectedMarshalledUsername, actualMarshalledUsername);
    assert Arrays.equals(expectedUnmarshalledUsername, actualUnmarshalledUsername);
  }

  // Tests end-to-end
  public static void testMarshall() throws Exception {
    byte version = Constants.CURRENT_VERSION;
    Operation operation = Operation.CREATE_ACCOUNT;
    protocol.Exception exception = protocol.Exception.NONE;

    // define the content, convert it to bytes, then grab the length
    String username = "username1";
    byte[] usernameBytes = username.getBytes();

    // create a message variable
    ArrayList<byte[]> args = new ArrayList<byte[]>();
    args.add(usernameBytes);
    Message message = new Message(version, operation, exception, args);

    // marshall and unmarshall
    byte[] marshalledMessage = Marshaller.marshall(message);
    Message unmarshalledMessage = Marshaller.unmarshall(marshalledMessage);

    // compare initial inputs to final outputs
    assert version == unmarshalledMessage.getVersion();
    assert operation == unmarshalledMessage.getOperation();
    assert exception == unmarshalledMessage.getException();
    assert Arrays.equals(usernameBytes,unmarshalledMessage.getArguments().get(0));
  }
}
