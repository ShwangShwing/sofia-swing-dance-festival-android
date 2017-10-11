package com.sofiaswing.sofiaswingdancefestival.views.about;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class AboutPresenter implements AboutInterfaces.IPresenter {
    private final AboutInterfaces.IView view;

    public AboutPresenter(AboutInterfaces.IView view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public AboutInterfaces.IView getView() {
        return this.view;
    }

    @Override
    public void start() {

    }
}
