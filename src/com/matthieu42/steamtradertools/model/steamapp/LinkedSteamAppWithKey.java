package com.matthieu42.steamtradertools.model.steamapp;

import javafx.scene.image.Image;

import javax.xml.bind.annotation.*;

/**
 * Created by Matthieu on 09/03/2017.
 */

@XmlRootElement
public class LinkedSteamAppWithKey extends AbstractSteamAppWithKey
{
    @XmlElement
    protected int id;

    @XmlElement
    private boolean achievement;
    @XmlElement
    private boolean tradingCards;
    @XmlElement
    private String headerImage;
    @XmlElement
    private double price;


    public LinkedSteamAppWithKey(){

    }
    public LinkedSteamAppWithKey(String name, int id, boolean achievement, boolean tradingCards, String headerImage, double price)
    {
        super(name);
        this.id = id;
        this.achievement = achievement;
        this.tradingCards = tradingCards;
        this.headerImage = headerImage;
    }


    public String getHeaderImage()
    {
        return headerImage;
    }


    public boolean hasTradingCards()
    {
        return tradingCards;
    }
    public boolean hasAchievements()
    {
        return achievement;
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

    public double getPrice()
    {
        return this.price;
    }
}
