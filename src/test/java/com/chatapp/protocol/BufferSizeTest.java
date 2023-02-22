package com.chatapp.protocol;

import java.util.ArrayList;

import com.chatapp.client.CommandParser;
import com.chatapp.client.commands.Command;
import com.chatapp.client.commands.CreateAccountCommand;
import com.chatapp.client.commands.DeleteAccountCommand;
import com.chatapp.client.commands.ListAccountsCommand;
import com.chatapp.client.commands.LogInCommand;
import com.chatapp.client.commands.LogOutCommand;
import com.chatapp.client.commands.SendMessageCommand;
import com.chatapp.utility.ByteConverter;

public class BufferSizeTest {
    
    public static void main(String[] args) {
        String[] commands = new String[] {
            "connect localhost",
            "create_account a",
            "list_accounts *",
            "login a",
            "logout",
            "delete_account a",
            "send a 1",
            "send a This is a long message!"
        };

        for (String command : commands) {
            Operation op;
            ArrayList<byte[]> arguments = new ArrayList<byte[]>();

            Command c = CommandParser.parse(command);
            if (c instanceof CreateAccountCommand) {
                op = Operation.CREATE_ACCOUNT;
                arguments.add(ByteConverter.stringToByteArray(((CreateAccountCommand) c).getUsername()));
            } else if (c instanceof ListAccountsCommand) {
                op = Operation.LIST_ACCOUNTS;
                arguments.add(ByteConverter.stringToByteArray(((ListAccountsCommand) c).getPattern()));
            } else if (c instanceof LogInCommand) {
                op = Operation.LOG_IN;
                arguments.add(ByteConverter.stringToByteArray(((LogInCommand) c).getUsername()));
            } else if (c instanceof LogOutCommand) {
                op = Operation.LOG_OUT;
            } else if (c instanceof DeleteAccountCommand) {
                op = Operation.DELETE_ACCOUNT;
                arguments.add(ByteConverter.stringToByteArray(((DeleteAccountCommand) c).getUsername()));
            } else if (c instanceof SendMessageCommand) {
                op = Operation.SEND_MESSAGE;
                arguments.add(ByteConverter.stringToByteArray(((SendMessageCommand) c).getRecipient()));
                arguments.add(ByteConverter.stringToByteArray(((SendMessageCommand) c).getMessage()));
            } else {
                System.out.println("Skipping " + command);
                continue;
            }

            Message message = new Message(Constants.CURRENT_VERSION, op, com.chatapp.protocol.Exception.NONE, arguments);
            System.out.println(command + ": " + Marshaller.marshall(message).length + " bytes");
        }
    }

}
