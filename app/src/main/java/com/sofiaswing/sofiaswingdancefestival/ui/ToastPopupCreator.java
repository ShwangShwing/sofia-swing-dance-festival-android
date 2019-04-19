package com.sofiaswing.sofiaswingdancefestival.ui;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by shwangshwing on 10/17/17.
 */

public class ToastPopupCreator implements UiInterfaces.IPopupCreator {
    private final Context context;

    public ToastPopupCreator(Context context) {
        this.context = context;
    }

    @Override
    public void popup(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
