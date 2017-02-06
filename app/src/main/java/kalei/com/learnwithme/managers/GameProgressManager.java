package kalei.com.learnwithme.managers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.content.Context;

/**
 * Created by risaki on 2/5/17. this class should be keeping track of what game tiles are visible etc. dynamically showing the number of questions needed to see
 * the image  which is currently 9.
 */

public class GameProgressManager {

    // Singleton instance
    private static GameProgressManager sInstance;

    public static GameProgressManager getInstance() {
        if (sInstance == null) {
            synchronized (GameProgressManager.class) {
                if (sInstance == null) {
                    sInstance = new GameProgressManager();
                }
            }
        }

        return sInstance;
    }
}
