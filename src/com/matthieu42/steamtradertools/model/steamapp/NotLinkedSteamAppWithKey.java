package com.matthieu42.steamtradertools.model.steamapp;

/**
 * Created by Matthieu on 21/06/2017.
 */
public class NotLinkedSteamAppWithKey extends  AbstractSteamAppWithKey
{
    private AppType appType;

    public NotLinkedSteamAppWithKey(){

    }
    public NotLinkedSteamAppWithKey(String name, AppType type){
        this.name = name;
        this.appType = type;
    }

}
