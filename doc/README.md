# High Seas

This game simulates a yacht race based on the 35th Americas Cup.

The program uses an extended version of the AC35 Streaming Data Interface to
communicate between server and clients.

## Packaging the project

The jar files required to run the program can be built by using the following maven
command in the root directory of the project:
```
mvn package
```
The API documentation (javadoc) can be built with the command:
```
mvn site
```
These commands can be executed in the modules' directories to build only that module.
The valid directories are
```
/gameX/common/
/gameX/racemodel/
/gameX/visualiser/
```

## Getting started
Playing the game requires a server and a client.

To execute a jar file, use the following terminal command, where $FILE_NAME$ is
the name of the jar file:
```
java -jar $FILE_NAME$.jar
```
On some systems jar files may also be executed by selecting them in the file browser.

## Using the game client

### During a race

## Running the Game Server

## Configuration Files