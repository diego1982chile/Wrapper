package cl.forevision.wrapper.helpers;

import javafx.application.Platform;

/**
 * Created by root on 16-03-23.
 */
public class PlatformHelper {

    public static void run(Runnable treatment) {

        if(treatment == null) {
            throw new IllegalArgumentException("The treatment to perform can not be null");
        }

        if(Platform.isFxApplicationThread()) {
            treatment.run();
        }
        else {
            Platform.runLater(treatment);
        }
    }
}
