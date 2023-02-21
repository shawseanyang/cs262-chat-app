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
