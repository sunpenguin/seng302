#RaceVision

This program is designed to visulaise live data for a boat race. 
This data must follow the AC35 protocol. 
The program recieve the data from a stream and displays it through 
animations to give a top down view of the race.
 
 ##Getting started
 To run the program the file App.jar must be run through the terminal.
 
 On linux use
 ```
 Blah blach
 ```
 On windows
 ```
FDSGDSFG

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
view of the race, throught the application annotations about the race are displayed. Some can be 
controlled via the apllication.
If the race has not yet started the user will be presented with a interface displaying the expected 
start time to the race and a count down to it. THe participants will also be displayed here.

##Running the Mock Data Stream
To connect to the "Mock Data Stream" the test mock must be run before the connecting to the stream.
This is done by running MockDataStream.jar through the terminal

On linux use
```
sagfdsafd
```
On windows user
```
asdsa
```