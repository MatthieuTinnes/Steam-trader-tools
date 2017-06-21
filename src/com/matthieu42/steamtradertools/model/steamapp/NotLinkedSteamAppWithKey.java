package com.matthieu42.steamtradertools.model.steamapp;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Matthieu on 21/06/2017.
 */
@XmlRootElement
public class NotLinkedSteamAppWithKey extends AbstractSteamAppWithKey
{
    public NotLinkedSteamAppWithKey(){

    }
    public NotLinkedSteamAppWithKey(String name){
        super(name);
    }

}
