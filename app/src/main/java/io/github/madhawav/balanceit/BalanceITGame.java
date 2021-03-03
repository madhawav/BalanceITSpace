package io.github.madhawav.balanceit;

import android.content.Context;

import io.github.madhawav.balanceit.gameplay.GameParameters;
import io.github.madhawav.balanceit.opengleslearn.R;
import io.github.madhawav.balanceit.scenes.GamePlayScene;

import io.github.madhawav.gameengine.multiscene.AbstractMultiSceneGame;
import io.github.madhawav.gameengine.multiscene.AbstractScene;
import io.github.madhawav.gameengine.ResourceUtil;
import io.github.madhawav.gameengine.coreengine.GameDescription;
import io.github.madhawav.gameengine.graphics.AbstractShader;
import io.github.madhawav.gameengine.graphics.BasicShader;
import io.github.madhawav.gameengine.graphics.GraphicsEngine;
import io.github.madhawav.gameengine.graphics.GraphicsEngineDescription;
import io.github.madhawav.gameengine.graphics.SpriteEngine;

/**
 * The entry point of the game. This is a multi-scene game. (E.g. Game-play scene, GameOver scene)
 */
public class BalanceITGame extends AbstractMultiSceneGame {
    private GraphicsEngine graphicsEngine;
    private SpriteEngine spriteEngine;

    public BalanceITGame(Context context) {
        super(context, new GameDescription(30, true,  720.0f/1280.0f, true));

        AbstractShader shader = new BasicShader(
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_vs),
                ResourceUtil.readTextFileFromRawResource(context, R.raw.shader_fs));

        this.graphicsEngine = new GraphicsEngine(context, new GraphicsEngineDescription(shader));
        this.spriteEngine = new SpriteEngine(this.graphicsEngine);

        // Register to receive lifecycle events
        registerModule(this.graphicsEngine);
        registerModule(shader);
        registerModule(this.spriteEngine);
    }

    public GraphicsEngine getGraphicsEngine() {
        return graphicsEngine;
    }

    public SpriteEngine getSpriteEngine() {
        return spriteEngine;
    }

    /**
     * Return the initial scene.
     * @return
     */
    @Override
    public AbstractScene getInitialScene() {
        return new GamePlayScene(new GameParameters());
    }
}


