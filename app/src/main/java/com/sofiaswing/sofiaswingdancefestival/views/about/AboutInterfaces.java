package com.sofiaswing.sofiaswingdancefestival.views.about;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class AboutInterfaces {
    public interface IView {

    }

    public interface IPresenter {
        void setView(IView view);
        void start();
    }
}
