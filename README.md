# Android-Car-Game
This is project contain two versions for a moblie car game built in java withing the android studio enviorment.


## V1
### Game Logic
The user contols a single car that is headed against traffic, the car can change lanes with button presses.

The car has three lifes represented by red hearts, the score matches the life time up to the lose condition which is getting to zero hearts.

Currently the game is set to loop, on a loss hearts and score will reset, but the game will keep running.

### Code Structure

The MainActivity manage the game screen with three threads:
refreshUI - incoming traffic, game status
scoreUI - update score,
collisionUI - detect collision

Beside that it holds the GameManeger that keeps track of the parameters.


## V2
### Code Structure
The code consists of multiple activities; the main activity is a modified version of V1, while the rest are newly added.
We start with a menu activity that sets the game settings and a possible transition to the main activity, which is the gameplay, and the endgame activity, which is the score board.

In the menu, the user can toggle between gameplay with buttons or sensors and the speed of the game slow or fast.

Once the user loses the game, it will transition to the endgame. Activity, which holds 2 fragments that split the screen vertically, holds a list of the top 10 scores and the dates there were recored (contains some valid data), and the latest game will be added on meeting the score criteria.
The second fragment is a map provided by the Google Maps API, and on a click on a certain item of the list, the map will move according to the place where the game score was recorded.


### Game Logic
The game foundation stays the same, but with the following exceptions:
The user's object is controlled by buttons or sensors and positioned on one of the 5 lanes. Objects come towards the user, and collision detection affects the user. A fuel tank would refill an empty heart, and a car would remove one.
Once hit, the user shall turn invulnerable for a short time.
