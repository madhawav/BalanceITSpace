package io.github.madhawav.balanceit;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import io.github.madhawav.gameengine.graphics.Texture;

/**
 * Android activity hosting the game
 */
public class GameActivity extends Activity {
    private BalanceITGame game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.game = new BalanceITGame(this);
        if (!this.game.getGravitySensor().isSupported()) {
            Toast.makeText(this, "Error: Gravity sensor not available!", Toast.LENGTH_LONG).show();
            finish();
        }
        setContentView(this.game.getSurfaceView());

        this.game.start(); // Puts the game engine to paused state
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.game.resume(); // Resumes the game engine
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.game.pause(); // Pauses the game engine
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.game.finish();
        if (Texture.textureCount > 0) {
            throw new IllegalStateException("Un-freed textures");
        }
    }
}
