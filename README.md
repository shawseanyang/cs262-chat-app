# cs262-chat-app

## Getting started
1. Install Maven, one of the Java build systems. On Mac, use homebrew: `brew install maven`.
> The `pom.xml` file in the project defines the Maven dependencies, which includes JUnit for unit/integration testing. Make sure Maven is installed by running `mvn -v`.
2. Clone this repository: `git clone`
3. Navigate to the root folder
4. Download the dependencies: `mvn clean install`
5. Compile the code: `mvn compile`

## Running

Server: run `mvn exec:java -Dexec.mainClass="com.chatapp.server.ChatServer"`

Client: run `mvn exec:java -Dexec.mainClass="com.chatapp.client.Client"`

After the Maven build output ends, the client will start waiting for user input on the command line. You may start inputting commands.

> The server must run before the client can perform 'connect'

## Testing
Open the project in VSCode and go to the testing tab. For unit tests, click on the green arrow to run them all. For integration tests, please run them one at a time by manually clicking on the green arrow next to each test.

## Client commands
- connect \<ip\>
- create_account \<account_name\>
- list_accounts \<wildcard_query\>
- login \<account_name\>
- logout
- delete_account \<account_name\>
- send \<recipient\> \<message\>

## Sample usage
```
connect 12.345.67.89
create_account andy
create_account bessie
list_accounts *e*i*
login andy
send bessie Here's to a (hopefully) functioning message service!
logout
login bessie
send andy Wow I'm surprised that worked!
```

# Documentation
## Protocol
### Sockets
The server and client communicate over TCP sockets. The server listens on port 8080. The client connects to the server on port 8080. The client and server communicate using a custom protocol. 
### Requests and Responses
For the most part, the client sends requests to the server, and the server sends responses to the client (the one exception to this is DistributeMessages(), which delivers undelivered messages to the user upon login). The client sends requests by writing a line to the socket. The server sends responses by writing a line to the socket. The client and server use a custom protocol to encode requests and responses.

### Exceptions
Responses will contain an exception variable that indicates which error occured in the case that the request was unsuccessful. Requests must also contain exceptions but the exception is effectively `null` (in our code, it is defined as NONE). The client will print the exception message to the console.

### Client
The client sends requests to the server and receives responses from the server. The client is responsible for parsing user input from the command line and sending the appropriate request to the server. The client delegates responsibility for handling and printing the response to a helper class called `ClientHandler`.

The single-threaded nature of the client means that the client can only send one request at a time. The client waits for a response before sending another request. The client checks for messages sent to the user upon completion of a command. This is because client cannot immediately print incoming messages because the thread is blocked on waiting for user input from the command line.

### Server
The server can be split into two parts:
1. The `Server` class, which is responsible for listening for new client connections and creating a new thread for each client connection.
2. The `UserHandler` class, which is responsible for handling a single client connection. The `UserHandler` class is responsible for parsing requests from the client and sending responses to the client.
3. The `User` class, which is responsible for storing user information such as the username, socket, and undelivered messages.

The server is multi-threaded, meaning that it can handle multiple client connections at the same time. The server creates a new thread for each client connection. The server is responsible for sending messages to the correct user. The server is also responsible for sending messages to the correct user when the user logs in.