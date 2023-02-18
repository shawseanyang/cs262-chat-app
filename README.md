# cs262-chat-app

Run the server using ./server.sh

Run the client using ./client.sh

> The server must run before the client can perform 'connect'

## Client commands
- connect <ip>
- create_account <account_name>
- list_accounts <wildcard_query>
- login <account_name>
- logout
- delete_account <account_name>
- send <recipient> <message>

## Sample usage
... cs262-chat-app % ./client.sh

connect 12.345.67.89

create_account andy

create_account bessie

list_accounts \*e\*i\*

login andy

send bessie Here's to a (hopefully) functioning message service!

logout

login bessie

send andy Wow I'm surprised that worked!