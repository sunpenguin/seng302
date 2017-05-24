#RaceVision

This program is designed to visualise live data for a boat race. 
This data must follow the AC35 protocol. 
The program receives the data from a stream and displays it through 
animations to give a top down view of the race.

##Packaging the Project
To create the jar files required to run the program, 
package the program through the command line, from the project root folder:
On Linux use
```
mvn package
```
On Windows use
```
mvn package
```
On Mac OS use
```
mvn package
```


##Getting started
To run the program the file App.jar must be run through the command line:
 
On Linux use
```
java -jar App.jar
```
On Windows use
```
java -jar App.jar
```
On Mac OS use
```
java -jar App.jar
```

##Using the Application
The user is presented with an interface with three buttons.
The user can select either "Live AC35 Data Stream", "Test AC35 Data Stream" or "Mock Data Stream".
The "Live AC35 Data Stream" option will connect the application to the host 
"livedata.americascup.com" with port number 4940.
The "Test AC35 Data Stream" option will connect the application to the host 
"livedata.americascup.com" with port number 4941.
The "Mock Data Stream" option will connect the application to the loop back address (local host)
at port number 5005.

##During a Race
Once a connection is established with one of the streams the user will be displayed 
one of two views. If the race has started, the user with be displayed with a top down 
view of the race. Through the application annotations about the race are displayed. Some can be 
controlled via the application.
If the race has not yet started the user will be presented with a interface displaying the expected 
start time to the race and a count down to it. THe participants will also be displayed here.

##Running the Mock Data Stream
To connect to the "Mock Data Stream" the test mock must be run before the connecting to the stream.
This is done by running MockDataStream.jar through the command line:

On Linux use
```
java -jar MockDataStream.jar
```
On Windows use
```
java -jar MockDataStream.jar
```
On Mac OS use
```
java -jar MockDataStream.jar
```