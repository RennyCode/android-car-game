# Android-Car-Game
This is first version for a moblie car game built in java withing the android studio enviorment.

# Game Logic
The user contols a single car that is headed against traffic, the car can change lanes with button presses.

The car has three lifes represented by red hearts, the score matches the life time up to the lose condition which is getting to zero hearts.

Currently the game is set to loop, on a loss hearts and score will reset, but the game will keep running.

# Code Structure

The MainActivity manage the game screen with three threads:
refreshUI - incoming traffic, game status
scoreUI - update score,
collisionUI - detect collision

Beside that it holds the GameManeger that keeps track of the parameters.
