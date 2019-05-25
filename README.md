# trustlineserver
**Prerequistes**
- JAVA 8

**Assumptions**

The available trustlines between ndoes are described in application.yml . 
The trustlines are unidirectional which means 

NodeA -> NodeB **doesn't** imply NodeB -> NodeA

Trustlines can be implicit which means

NodeA -> NodeB and NodeB -> NodeC implies NodeA -> NodeC

**Build**

From the root folder, run this command
- ./gradlew clean build

This will build the project and run the tests. The output is a single fat jar which contains the class files as well as all the dependent jars.
The jar file will be located in build/lib folder

**Test**

Tests are written using Spock framework (Groovy)

**Starting the server**

The same jar file will be used to start multiple servers and each server will be act as an independent node (Ex Alice, Bob etc)
Therefore we need to pass the a different server port and node name as arguments while starting the server.

- java -jar trustlineserver-1.0.jar --server.port=5555 --node.name=Alice

The above command will start a server on port 5555 and will be associated with Alice.

Since the servers communicate with each other, they need to know the address of other servers . This information is stored in application.yml file.
The following parts have been configured with the corresponding users 
- 5555 : Alice
- 6666 : Bob
- 7777 : Charlie

So please use those ports and node names to start servers in seperate terminals.
- java -jar trustlineserver-1.0.jar --server.port=5555 --node.name=Alice
- java -jar trustlineserver-1.0.jar --server.port=6666 --node.name=Bob
- java -jar trustlineserver-1.0.jar --server.port=7777 --node.name=Charlie

You can use different ports and node names but you would need to modify the application.yml file.

**Executing a command**

There are 2 API endpoints exposed 
- /api/transactions
- /api/trustline/transactions

The first one is meant to be invoked by a client. The second API is meant for server to server communication. Please don't invoke it directly with a client.
You can use any client that supports HTTP POST method (curl, Postman etc) to invoke the first API on a server which sends X amount of money to the other server.

Following is a curl command whcih sends 10 dollars from Alice to Bob (successfully only if there is a trust line from Alice to Bob)

curl -d '{"to":"Bob", "amount":10}' -H "Content-Type: application/json" -X POST http://localhost:5555/api/transactions

Similarly this command will send 10 dollars from Charlie to Bob (successfully only if there is a trust line from Charlie to Bob)

curl -d '{"to":"Bob", "amount":10}' -H "Content-Type: application/json" -X POST http://localhost:7777/api/transactions
