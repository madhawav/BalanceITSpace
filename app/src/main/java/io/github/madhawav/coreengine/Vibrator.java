package io.github.madhawav.coreengine;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;

public class Vibrator {
    private final android.os.Vibrator vibrator;
    public Vibrator(Context context){
        vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    public boolean isAvailable(){
        return vibrator != null;
    }
    public void vibrate(int millis){
        if(vibrator == null)
            throw new UnsupportedOperationException("Vibrator not available");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{
            vibrator.vibrate(millis);
        }
    }
}
