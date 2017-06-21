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
public class LinkedSteamAppWithKey extends AbstractSteamAppWithKey
{
    private SteamApp steamApp;
    @XmlElement
    protected int id;


    public LinkedSteamAppWithKey(){

    }
    public LinkedSteamAppWithKey(int id)
    {
        this.id = id;
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
    public String getSteamLink()
    {
        return "https://steamdb.info/app/" + this.getId();
    }
    public String getSteamDBLink()
    {
        return "http://store.steampowered.com/app/"+ this.getId();
    }
    public String getItadLink()
    {
        char appName[] = this.getName().toCharArray();
        StringBuilder itadName = new StringBuilder();
        for(int i=0; i < this.getName().length(); i++)
        {
            if(Character.isAlphabetic(appName[i]) || Character.getNumericValue(appName[i]) == 0)
                itadName.append(appName[i]);
            else if(Character.isDigit(appName[i]))
            {
                int num = Character.getNumericValue(appName[i]);
                if(num < 4)
                {
                    for(int x = 0; x < num; x++)
                    {
                        itadName.append("i");
                    }
                }
                else if(num == 4)
                    itadName.append("iv");
                else if(num == 5)
                    itadName.append("v");
                else if(num < 9)
                {
                    itadName.append("v");
                    for(int y = 0; y < num - 5; y++)
                    {
                        itadName.append("i");
                    }
                }
                else if(num == 9)
                    itadName.append("ix");
            }
        }
        return "https://isthereanydeal.com/#/page:game/info?plain=" + itadName.toString();
    }
    public int getId()
    {
        return this.id;
    }

}
