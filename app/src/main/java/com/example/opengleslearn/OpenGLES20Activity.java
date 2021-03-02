package com.example.opengleslearn;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class OpenGLES20Activity extends Activity {
    private GLSurfaceView gLView;
    private MyMultisceneGame game;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        this.game = new MyMultisceneGame(this, savedInstanceState);
        this.gLView = this.game.getSurfaceView();
        setContentView(gLView);

        this.game.start(); // Puts game to paused state
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.game.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.game.pause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.game.finish();
    }
}
