# High Seas

High Seas is a game that is designed for people to compete with each other.
High Seas uses yachts to simulate actual yachts racing around a course.  
This data must follow the extended AC35 protocol.   
The game receives data from the user via key presses on the yacht they are controlling
and every other user can visualise these actions on their instance of the application.

## Packaging the Project
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


## Getting started
To run the program the file visualiser-5.1.jar must be run through the command line:
 
On Linux use
```
java -jar visualiser-6.0.jar
```
On Windows use
```
java -jar visualiser-6.0.jar
```
On Mac OS use
```
java -jar visualiser-6.0.jar
```

## Configurations

In sprint 6 we implemented the ability to not have to launch the racemodel (server) independently of the visualiser.
Currently the config file allowing this can be seen at the root of our git repo. After running "mvn package" when working within our IDE,
it will no longer be required to run the racemodel separately.

However, to get this working when using the jars downloaded via "Builds", a copy of the config file "visualiser-config.txt" must be obtained and 
placed in the same directory as "visualiser-6.0.jar" then also have the correct relative or full path to "racemodel-6.0.jar".

We aim to get everything packaged correctly in the next sprint (7) so that this is not necessary and just "works".

## Using the Application
The user is presented with an interface with three buttons. This is the home screen.
The user can select either "Play", "View Controls" or "Quit". 
 
The "Play" option will open up the boat customisation screen with the buttons "Host New Game", 
"Tutorial" and "Back". The user can change the colour of their boat 
with the arrow buttons on the screen. The user can then use the boat displayed on the screen.
The "Host New Game" option will let the user create a race (startup a server if configured correctly) and connect to it. The "Tutorial"
option will take the user to the tutorial of the game, teaching the user the controls.
The "Back" button takes the user back to the home screen.  

The "View Controls" option will load up an image showing the controls that are used to control a boat.
Selecting "View Controls" again will hide this image.  

The "Quit" option will close the application down.

## During a Race
Once a connection is established with one of the streams the user will be displayed 
one of two views.

If the race has not yet started the user will be presented with a interface displaying the expected 
start time to the race and a count down to it. The participants will also be displayed here.

If the race has started, the user with be displayed with a top down 
view of the race. The boat the user is controlling will be in a yellow highlight.

Users will receive penalties if they pass the start line before the count down reaches 0.

The user can find out about where the next mark is located with an arrow around the highlight.
The user can follow this to complete a race. Once a race has been completed, the placings will
be shown on the screen. The user can then press ESC and select "Quit Game" to be taken back to
the home screen.

Through the application annotations about the race are displayed. Some can be 
controlled via the application.

### Penalties

#### Start Line
When a user passes the startline early,  a penalty will be set and the highlight turns red. 
This means the user does not have control of their boat anymore until the highlight changes back to yellow.

#### Out Of Boundaries
When a user sails across the boundaries defined, the user will be disqualified and be taken out of the race.
The user will have to then quit the game and go back to the home screen.