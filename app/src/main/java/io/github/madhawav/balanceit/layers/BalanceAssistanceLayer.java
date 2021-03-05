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
    private static final float CROSS_HAIR_MARKER_SIZE = 100.0f;
    private static final float BALANCE_NOTICE_SIZE = 400;
    private static final float BALANCE_NOTICE_VERTICAL_PLACEMENT_FACTOR = 2.0f / 3.0f;
    private static final float BALANCE_NOTICE_VERTICAL_PLACEMENT_OFFSET = -100.0f;
    private final Image crossHairMarker;
    private final GravitySensor gravitySensor;
    private MathUtil.Vector3 camOffset;

    public BalanceAssistanceLayer(GraphicsContext graphicsContext, GravitySensor gravitySensor) {
        super(graphicsContext);
        this.gravitySensor = gravitySensor;
        this.camOffset = new MathUtil.Vector3();

        GraphicsEngine graphicsEngine = graphicsContext.getGraphicsEngine();


        Image image = new Image(graphicsContext, R.drawable.hold_balanced,
                (float) graphicsEngine.getViewportWidth() / 2 - BALANCE_NOTICE_SIZE / 2,
                (float) graphicsEngine.getViewportHeight() * BALANCE_NOTICE_VERTICAL_PLACEMENT_FACTOR + BALANCE_NOTICE_VERTICAL_PLACEMENT_OFFSET,
                BALANCE_NOTICE_SIZE, BALANCE_NOTICE_SIZE);
        addElement(image);

        crossHairMarker = new Image(graphicsContext, R.drawable.aim, -CROSS_HAIR_MARKER_SIZE, -CROSS_HAIR_MARKER_SIZE, CROSS_HAIR_MARKER_SIZE, CROSS_HAIR_MARKER_SIZE);
        addElement(crossHairMarker);
    }

    @Override
    public void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        MathUtil.Vector3 gravity = gravitySensor.getGravity().copy();
        gravity.multiply(1.0f / gravity.getLength());
        crossHairMarker.setX(camOffset.getX() - gravity.getX() * getGraphicsContext().getGraphicsEngine().getViewportWidth() - CROSS_HAIR_MARKER_SIZE / 2);
        crossHairMarker.setY(camOffset.getY() + gravity.getY() * getGraphicsContext().getGraphicsEngine().getViewportHeight() - CROSS_HAIR_MARKER_SIZE / 2);
    }

    public void setCamOffset(MathUtil.Vector3 camOffset) {
        this.camOffset = camOffset;
    }
}
