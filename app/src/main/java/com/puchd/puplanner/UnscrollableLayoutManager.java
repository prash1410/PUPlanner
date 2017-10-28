package com.puchd.puplanner;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;


public class UnscrollableLayoutManager extends LinearLayoutManager {
    public UnscrollableLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}