package io.github.madhawav.gameengine.ui;

public interface ClickListener {
    /**
     * On click event. Return True if event is handled.
     *
     * @param sender UI element notifying the event
     * @param x      Click position x relative to senders position
     * @param y      Click position y relative to senders position
     * @return True if event is handled. Otherwise return false.
     */
    boolean onClick(AbstractUIElement sender, float x, float y);
}
