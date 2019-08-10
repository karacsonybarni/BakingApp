package com.udacity.bakingapp.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class IngredientsRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(this.getApplicationContext());
    }
}
