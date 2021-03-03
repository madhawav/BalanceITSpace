package io.github.madhawav.balanceit;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import io.github.madhawav.gameengine.graphics.Texture;

public class OpenGLES20Activity extends Activity {
    private GLSurfaceView gLView;
    private BalanceITGame game;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        this.game = new BalanceITGame(this, savedInstanceState);
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
        if(Texture.textureCount > 0){
            throw new IllegalStateException("Un-freed textures");
        }
    }
}
