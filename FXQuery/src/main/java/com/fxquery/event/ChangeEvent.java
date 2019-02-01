package com.fxquery.event;

public interface ChangeEvent {

    public static final int HIDE = 0x000;
    public static final int SHOW = 0x001;


    public void change(int event);

}
