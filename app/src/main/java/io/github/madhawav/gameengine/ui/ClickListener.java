package io.github.madhawav.gameengine.ui;

public interface ClickListener {
    /**
     * On click event. Return True if event is handled.
     * @param sender
     * @param x
     * @param y
     * @return
     */
    boolean onClick(AbstractUIElement sender, float x, float y);
}
