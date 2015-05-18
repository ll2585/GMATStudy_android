package com.lukeli.gmatstudy;

import android.app.Application;

public class GlobalVars extends Application {
    GMATTesterPresenter presenter = new GMATTesterPresenter(new GMATTesterModel());

    public GMATTesterPresenter getPresenter(){
        return presenter;
    }
}