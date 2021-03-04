package io.github.madhawav.balanceit.layers;

import io.github.madhawav.balanceit.opengleslearn.R;
import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.coreengine.sensors.GravitySensor;
import io.github.madhawav.gameengine.graphics.GraphicsEngine;
import io.github.madhawav.gameengine.ui.GraphicsContext;
import io.github.madhawav.gameengine.ui.Image;
import io.github.madhawav.gameengine.ui.LayeredUI;

/**
 * This layer has the balance device to start game label and the balance indicator cross-hair.
 */
public class BalanceAssistanceLayer extends LayeredUI {
    private final Image crossHairMarker;
    private final GravitySensor gravitySensor;
    private MathUtil.Vector3 camOffset;

    public BalanceAssistanceLayer(GraphicsContext graphicsContext, GravitySensor gravitySensor) {
        super(graphicsContext);
        this.gravitySensor = gravitySensor;
        this.camOffset = new MathUtil.Vector3();

        GraphicsEngine graphicsEngine = graphicsContext.getGraphicsEngine();


        Image image = new Image(graphicsContext, R.drawable.hold_balanced, (float) graphicsEngine.getViewportWidth()/2-200, (float)graphicsEngine.getViewportHeight() * 2/3 - 100, 400, 400);
        addElement(image);

        crossHairMarker = new Image(graphicsContext, R.drawable.aim, -100, -100, 100, 100);
        addElement(crossHairMarker);
    }

    @Override
    public void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        MathUtil.Vector3 gravity = gravitySensor.getGravity().clone();
        gravity.multiply(1.0f/gravity.getLength());
        crossHairMarker.setX((float) camOffset.getX() - gravity.getX() * getGraphicsContext().getGraphicsEngine().getViewportWidth() - 50);
        crossHairMarker.setY((float) camOffset.getY() + gravity.getY() * getGraphicsContext().getGraphicsEngine().getViewportHeight() - 50);
    }

    public void setCamOffset(MathUtil.Vector3 camOffset) {
        this.camOffset = camOffset;
    }
}
