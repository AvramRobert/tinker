package com.example.tinker.handlers;

import android.os.Handler;
import android.os.Message;
import com.example.tinker.views.interfaces.DisplayView;

/**
 * Created by root on 7/28/14.
 */
public class MessageHandler extends Handler {

    DisplayView view;

    public MessageHandler(DisplayView view) {
        this.view = view;
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case 0:
                view.setError();
                break;
            case 1:
                view.populateDisplay(true);
                break;
            case 2:
                view.toggleDialog();
                break;
            case 3:
                view.dismissDialog();
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public void sendSetErrorMessage() {
        this.sendEmptyMessage(0);
    }

    public void sendPopulateDisplayMessage() {
        this.sendEmptyMessage(1);
    }

    public void sendToggleDialogMessage() {
        this.sendEmptyMessage(2);
    }

    public void sendDismissDialogMessage() {
        this.sendEmptyMessage(3);
    }

}
