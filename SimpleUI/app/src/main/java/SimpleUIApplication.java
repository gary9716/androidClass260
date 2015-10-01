import android.app.Application;

import com.parse.Parse;

/**
 * Created by ggm on 10/1/15.
 */
public class SimpleUIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "N5ytfpTopfdCCmIqCgZZk5PDjkUiGudm1UaygmOv",
                "TH9KM8xYSN4nFD7GIxGDrppNsXVN1exKrm0v6KuA");
        // Enable Local Datastore.

    }
}
