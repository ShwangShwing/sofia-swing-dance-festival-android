package com.sofiaswing.sofiaswingdancefestival.ui;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by shwangshwing on 10/17/17.
 */

public class ToastPopupCreator implements UiInterfaces.IPopupCreator {
    @Override
    public void popup(Context ctx, String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }
}
