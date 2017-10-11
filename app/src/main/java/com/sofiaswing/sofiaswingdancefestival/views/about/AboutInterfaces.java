package com.sofiaswing.sofiaswingdancefestival.views.about;

/**
 * Created by shwangshwing on 10/12/17.
 */

public class AboutInterfaces {
    public interface IView {
        void setPresenter(IPresenter presenter);
    }

    public interface IPresenter {
        IView getView();
        void start();
    }
}
