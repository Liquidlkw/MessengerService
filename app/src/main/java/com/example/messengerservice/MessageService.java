package com.example.messengerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

public class MessageService extends Service {
    private static final String TAG = "MessageService";
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    Log.i(TAG, "Server handleMessage: " + msg.getData().getString("msg"));
                    //拿到客户端 Messenger ,利用它给客户端发消息
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, 100);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "嗯 服务端收到收到");
                    replyMessage.setData(bundle);

                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

            }
        }
    }
}