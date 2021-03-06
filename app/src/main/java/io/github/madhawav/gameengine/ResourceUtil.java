package io.github.madhawav.gameengine;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceUtil {
    private ResourceUtil() {
    }

    /**
     * Read a text from android resource
     * @param context Android context
     * @param resourceId Resource id
     * @return Text from the resource
     */
    public static String readTextFileFromRawResource(final Context context, final int resourceId) {
        final InputStream inputStream = context.getResources().openRawResource(
                resourceId);
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        final BufferedReader bufferedReader = new BufferedReader(
                inputStreamReader);

        String nextLine;
        final StringBuilder body = new StringBuilder();

        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error in reading resource");
        }

        return body.toString();
    }
}
