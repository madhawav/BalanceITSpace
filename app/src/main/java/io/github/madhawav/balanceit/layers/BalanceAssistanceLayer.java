package io.github.madhawav.balanceit.layers;

import android.content.Context;

import io.github.madhawav.balanceit.opengleslearn.R;
import io.github.madhawav.gameengine.MathUtil;
import io.github.madhawav.gameengine.coreengine.sensors.GravitySensor;
import io.github.madhawav.gameengine.graphics.GraphicsEngine;
import io.github.madhawav.gameengine.ui.GraphicsContext;
import io.github.madhawav.gameengine.ui.Image;
import io.github.madhawav.gameengine.ui.LayeredUI;

/**
 * This layer has the "hold device balanced" label and the balance indicator cross-hair.
 */
public class BalanceAssistanceLayer extends LayeredUI {
    private final Image crossHairMarker;
    private final GravitySensor gravitySensor;
    final float uiCrossHairSize;
    private MathUtil.Vector3 camOffset;

    public BalanceAssistanceLayer(Context context, GraphicsContext graphicsContext, GravitySensor gravitySensor) {
        super(graphicsContext);
        this.gravitySensor = gravitySensor;
        this.camOffset = new MathUtil.Vector3();

        GraphicsEngine graphicsEngine = graphicsContext.getGraphicsEngine();

        final float balanceNoticeSize = context.getResources().getDimensionPixelSize(R.dimen.gameplay_balance_notice_size);
        final float balanceNoticeVerticalPlacementRatio = context.getResources().getFraction(R.fraction.gameplay_balance_notice_vertical_placement_ratio, 1, 1);
        final float balanceNoticeVerticalPlacementOffset = context.getResources().getDimensionPixelOffset(R.dimen.gameplay_balance_notice_vertical_placement_offset);
        Image image = new Image(graphicsContext, R.drawable.hold_balanced,
                (float) graphicsEngine.getViewportWidth() / 2 - balanceNoticeSize / 2,
                (float) graphicsEngine.getViewportHeight() * balanceNoticeVerticalPlacementRatio + balanceNoticeVerticalPlacementOffset,
                balanceNoticeSize, balanceNoticeSize);
        addElement(image);

        uiCrossHairSize = context.getResources().getDimensionPixelSize(R.dimen.gameplay_balance_cross_hair_size);
        crossHairMarker = new Image(graphicsContext, R.drawable.aim, -uiCrossHairSize, -uiCrossHairSize, uiCrossHairSize, uiCrossHairSize);
        addElement(crossHairMarker);
    }

    @Override
    public void onUpdate(double elapsedSec) {
        super.onUpdate(elapsedSec);
        MathUtil.Vector3 gravity = gravitySensor.getGravity().copy();
        gravity.multiply(1.0f / gravity.getLength());
        crossHairMarker.setX(camOffset.getX() - gravity.getX() * getGraphicsContext().getGraphicsEngine().getViewportWidth() - uiCrossHairSize / 2);
        crossHairMarker.setY(camOffset.getY() + gravity.getY() * getGraphicsContext().getGraphicsEngine().getViewportHeight() - uiCrossHairSize / 2);
    }

    public void setCamOffset(MathUtil.Vector3 camOffset) {
        this.camOffset = camOffset;
    }
}
