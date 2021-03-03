package io.github.madhawav.gameengine.coreengine;

/**
 * Status of the game engine
 */
public enum GameEngineState {
    PRE_START, // Before calling start() of AbstractGame
    RUNNING, // Abstract Game is in a running state. This happens when the user calls resume. (at onResume of Activity)
    PAUSED, // Abstract Game is in a paused state. Call this at onPause of Activity. This is also the state of the game after the initial call to AbstractGame.start()
    FINISHED // AbstractGame has finished. (User has called finished(). Call this when the activity is disposed.
}
