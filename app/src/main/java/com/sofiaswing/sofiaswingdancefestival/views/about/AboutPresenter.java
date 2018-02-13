package com.sofiaswing.sofiaswingdancefestival.views.about;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class AboutPresenter implements AboutInterfaces.IPresenter {
    private AboutInterfaces.IView view;

    public AboutPresenter() {

    }

    @Override
    public void setView(AboutInterfaces.IView view) {
        this.view = view;
    }

    @Override
    public void start() {

    }
}
