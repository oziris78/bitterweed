package com.twistral.utils;

import com.twistral.main.Bitterweed;
import com.twistral.toriafx.TScene;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.paint.Paint;


public abstract class TTScene<T extends Application, R extends Parent> extends TScene<Bitterweed, R> {

    public TTScene(Bitterweed app, R root) {
        super(app, root);
    }

    public TTScene(Bitterweed app, R root, Paint fill) {
        super(app, root, fill);
    }

}
