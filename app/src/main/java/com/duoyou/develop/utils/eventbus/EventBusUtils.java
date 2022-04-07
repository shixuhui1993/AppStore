package com.duoyou.develop.utils.eventbus;

import android.os.Handler;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {
    public static void register(Object object) {
        boolean registered = EventBus.getDefault().isRegistered(object);
        if (!registered) {
            EventBus.getDefault().register(object);
        }
    }

    public static void unRegister(Object object) {
        if (object != null) {
            EventBus.getDefault().unregister(object);
        }
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }

    public static void postDelayed(Object object, int delay) {
        Handler handler = new Handler();
        handler.postDelayed(() -> EventBus.getDefault().post(object), delay);

    }
}
