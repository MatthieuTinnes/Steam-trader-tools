package com.matthieu42.steamtradertools.model.steamapp;

import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;
import com.matthieu42.steamtradertools.model.SteamApiStatic;
import com.matthieu42.steamtradertools.model.key.SteamKey;

import javax.xml.bind.annotation.*;
import java.util.HashSet;

/**
 * Created by Matthieu on 09/03/2017.
 */

@XmlRootElement
public class SteamAppWithKey extends com.matthieu42.steamtradertools.model.steamapp.SteamApp
{
    private SteamApp steamApp;

    @XmlElementWrapper(name = "steamKeys")
    @XmlElement(name = "steamKey")
    private HashSet<SteamKey> steamKeyList;
    @XmlElement
    private int nbTotalKey;

    public SteamAppWithKey(){

    }
    public SteamAppWithKey(int id)
    {
        super(id);
        this.steamKeyList = new HashSet<>();
    }

    public void setApp(int appId) throws SteamApiException
    {
        steamApp = SteamApiStatic.steamApi.retrieve(appId);
        this.name = steamApp.getName();
    }

    public void setApp(SteamApp app)
    {
        steamApp = app;
        this.name = steamApp.getName();
    }

    public String getHeaderImage()
    {
        return steamApp.getHeaderImage();
    }


    public SteamApp getSteamApp() {
        return steamApp;
    }

    public boolean hasTradingCards()
    {
        for( String s : this.getSteamApp().getCategories())
        {
            if(s.equals("Steam Trading Cards"))
                return true;
        }
        return false;
    }
    public boolean hasAchievements()
    {
        for( String s : this.getSteamApp().getCategories())
        {
            if(s.equals("Steam Achievements"))
                return true;
        }
        return false;
    }
    public void addKey(String key)
    {
        steamKeyList.add(new SteamKey(key));
        nbTotalKey++;
    }

    public void delKey(SteamKey key)
    {
        steamKeyList.remove(key);
        nbTotalKey--;
    }
    public HashSet<SteamKey> getSteamKeyList()
    {
        return steamKeyList;
    }

    public int getNbTotalKey()
    {
        return nbTotalKey;
    }

}
