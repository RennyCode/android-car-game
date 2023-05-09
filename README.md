# Android-Car-Game
This is project contain two versions for a moblie car game built in java withing the android studio enviorment.


# V1
## Game Logic
The user contols a single car that is headed against traffic, the car can change lanes with button presses.

The car has three lifes represented by red hearts, the score matches the life time up to the lose condition which is getting to zero hearts.

Currently the game is set to loop, on a loss hearts and score will reset, but the game will keep running.

## Code Structure

The MainActivity manage the game screen with three threads:
refreshUI - incoming traffic, game status
scoreUI - update score,
collisionUI - detect collision

Beside that it holds the GameManeger that keeps track of the parameters.


# V2
## Code Structure
The code consist of multiple  activities, the MainActivity is a modified version from V1, while the rest are newly add.
We start from a MenuActivity that sets the game settings and possible transition to MainActivity which is the gameplay and endgameActivity which is the score board.

In the menu the user can toggle between gameplay with buttons/sensors and with the speed of the game slow/fast.

Once the user loses the game it will tansition to endgameActivity which holds 2 Fragments thas splits the screen verticly, the first fragment hold a list of the top 10 scores and the dates there where recored (contains some defualt data), the latest game will be added on meeting the score critiria.
the seconde fragment is a map provided by the google maps API and on a click on a certiane item of the list the map will move accordinly the the place which the game score was recorded.


## Game Logic
The game foundation stays the same but with the following extantions:
the user's object is controled by buttons/sensors and positioned on one of the 5 lanes accordinly, obstecals come towards the user and on a collision detection affects the user, a fuel tank would refill an empty heart
