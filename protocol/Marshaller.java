package protocol;

import java.util.ArrayList;
import java.util.Arrays;

import utility.ByteConverter;

public class Marshaller {
    public static byte[] marshall(Message message) {
        // marshall the arguments first so we know how long the message will be
        byte[] marshalledArguments = marshallArguments(message.getArguments());
        // calculate the length of the entire message by adding the length of the arguments to the length of the "header" portion (everything before the arguments)
        byte[] marshalledMessage = new byte[Constants.CONTENT_POSITION + marshalledArguments.length];

        // fill in the header portion of the message
        marshalledMessage[Constants.VERSION_POSITION] = message.getVersion();
        marshalledMessage[Constants.OPERATION_POSITION] = message.getOperation().toByte();
        marshalledMessage[Constants.EXCEPTION_POSITION] = message.getException().toByte();

        // fill in the content portion of the message
        for (int i = 0; i < marshalledArguments.length; i++) {
            marshalledMessage[Constants.CONTENT_POSITION + i] = marshalledArguments[i];
        }
        
        return marshalledMessage;
    }

    public static Message unmarshall(byte[] marshalledMessage) {
        // unmarshall the header portion
        byte version =
          marshalledMessage[Constants.VERSION_POSITION];
        Operation operation =
          Operation.fromByte(marshalledMessage[Constants.OPERATION_POSITION]);
        Exception exception =
          Exception.fromByte(marshalledMessage[Constants.EXCEPTION_POSITION]);

        // extract content portion from byte[]
        int end = marshalledMessage.length;
        
        // remove message separator at the end if it exists
        if (marshalledMessage[marshalledMessage.length - 1] == Constants.MESSAGE_SEPARATOR) {
          end--;
        }

        byte[] content = Arrays.copyOfRange(marshalledMessage, Constants.CONTENT_POSITION, end);

        // unmarshall the arguments portion
        ArrayList<byte[]> arguments = unmarshallArguments(content);

        return new Message(version, operation, exception, arguments);
    }

    // marshalls arguments by escaping special characters and then concatenating them with the argument separator
    private static byte[] marshallArguments(ArrayList<byte[]> arguments) {
      // parse arguments into an ArrayList of bytes, perform escaping, then convert back to byte[]
      ArrayList<Byte> output = new ArrayList<Byte>();
      boolean first = true;
      for (byte[] argument : arguments) {
        // Only add argument separators when it isn't the first argument
        if (!first) {
          output.add(Constants.ARGUMENT_SEPARATOR);
        }
        else {
          first = false;
        }
        for (byte b : escapeRestrictedCharacters(argument)) {
          output.add(b);
        }
      }
      // Add message separator character
      output.add(Constants.MESSAGE_SEPARATOR);

      return ByteConverter.byteArrayListToByteArray(output);
    }

    // unmarshalls arguments by separating them by the the argument separator and unescaping special characters
    private static ArrayList<byte[]> unmarshallArguments(byte[] marshalledMessage) {
      ArrayList<byte[]> result = new ArrayList<byte[]>();

      // split the arguments by the argument separator and then unescape them
      byte[][] args = ByteConverter.splitByteArray(marshalledMessage, Constants.ARGUMENT_SEPARATOR);
      for (byte[] arg : args) {
        result.add(unescapeRestrictedCharacters(arg));
      }
      
      return result;
    }

    // Escapes restricted characters by adding the escape character in front of them. Ex: \t becomes \\t and \n becomes \\n
    private static byte[] escapeRestrictedCharacters(byte[] input) {
      // parse input into an ArrayList of bytes, perform escaping, then convert back to byte[]
      ArrayList<Byte> output = new ArrayList<Byte>();

      for (byte b : input) {
        // "\t" --> "\\t"
        if (b == Constants.ARGUMENT_SEPARATOR) {
          output.add(Constants.ESCAPE_CHARACTER);
          output.add(Constants.ARGUMENT_SEPARATOR_LETTER);
        }
        // "\n" --> "\\n"
        else if (b == Constants.MESSAGE_SEPARATOR) {
          output.add(Constants.ESCAPE_CHARACTER);
          output.add(Constants.MESSAGE_SEPARATOR_LETTER);
        }
        else {
          output.add(b);
        }
      }

      return ByteConverter.byteArrayListToByteArray(output);
    }

    // Unescapes restricted characters by removing the escape character in front of them. Ex: \\t becomes \t and \\n becomes \n
    private static byte[] unescapeRestrictedCharacters(byte[] input) {
      // parse input into an ArrayList of bytes, perform unescaping, then convert back to byte[]
      ArrayList<Byte> output = new ArrayList<Byte>();
      for (int i = 0; i < input.length; i++) {
        // "\\t" -> "\t"
        if (i != input.length - 1 && input[i] == Constants.ESCAPE_CHARACTER
          && input[i + 1] == Constants.ARGUMENT_SEPARATOR_LETTER) {
          output.add(Constants.ARGUMENT_SEPARATOR);
          i++;
        }
        // "\\n" -> "\n"
        else if (i != input.length - 1 && input[i] == Constants.ESCAPE_CHARACTER
        && input[i + 1] == Constants.MESSAGE_SEPARATOR_LETTER) {
          output.add(Constants.MESSAGE_SEPARATOR);
          i++;
        }
        else
          output.add(input[i]);
      }
      
      return ByteConverter.byteArrayListToByteArray(output);
    }
}