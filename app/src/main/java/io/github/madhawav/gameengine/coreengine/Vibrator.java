package io.github.madhawav.gameengine.coreengine;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;

/**
 * Class to access Android Vibrator
 */
public class Vibrator {
    private final android.os.Vibrator vibrator;

    public Vibrator(Context context) {
        vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * Returns true if the vibrator is supported
     *
     * @return True if the vibrator is supported. Otherwise false.
     */
    public boolean isSupported() {
        return vibrator != null;
    }

    /**
     * Vibrate the phone for a specified duration in seconds
     *
     * @param secs Duration in seconds to vibrate
     */
    public void vibrate(float secs) {
        if (vibrator == null)
            throw new UnsupportedOperationException("Vibrator not available");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot((int) (secs * 1000), VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate((int) (secs * 1000));
        }
    }
}
