package com.puchd.puplanner;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetDayService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return (new WidgetDayListProvider(this.getApplicationContext(), intent));
    }
}
