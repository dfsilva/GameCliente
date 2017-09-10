package com.anhanguera.gamecliente;

import android.app.Application;
import android.content.SharedPreferences;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Created by diego on 09/09/17.
 */

public class GameApp extends Application {

    private static String GAME_APP_KEY = "game_app_key";
    private static String USUARIO_AUTENTICADO_KEY = "usuario_autenticado_key";

    private static GameApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public SharedPreferences getDefaultSharedPreferences(){
        return instance.getBaseContext().getSharedPreferences(GAME_APP_KEY, MODE_PRIVATE);
    }

    public SharedPreferences.Editor getDefaultSharedPreferencesEdit(){
        return getDefaultSharedPreferences().edit();
    }

    public void setUsuarioAutenticado(String usuario){
        getDefaultSharedPreferencesEdit().putString(USUARIO_AUTENTICADO_KEY, usuario).commit();
    }

    public String getUsuarioAutenticado(){
        return getDefaultSharedPreferences().getString(USUARIO_AUTENTICADO_KEY,"");
    }

    public boolean isUsuarioAutenticado(){
        return getDefaultSharedPreferences().contains(USUARIO_AUTENTICADO_KEY);
    }

    public void logoff(){
        getDefaultSharedPreferencesEdit().remove(USUARIO_AUTENTICADO_KEY).commit();
    }

    public static GameApp gameApp(){
        return instance;
    }

    private Channel channel;

    public Channel getChannel(){
        if(channel == null){
            channel = ManagedChannelBuilder.forAddress("10.0.2.2", 50051)
                    .usePlaintext(true).build();
        }
        return channel;
    }

}
