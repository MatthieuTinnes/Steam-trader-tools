package com.matthieu42.steamtradertools.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Matthieu on 11/05/2017.
 */
@XmlRootElement
@XmlType(name = "matthieu42.SteamApp")
public abstract class SteamApp implements Comparable<SteamApp>
{
    @XmlElement
    protected String name;
    @XmlElement
    protected int id;

    public SteamApp(){

    }
    public SteamApp(int id){
        this.id = id;
        this.name = "";
    }

    public String getName() {
        return name;
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

    @Override
    public String toString()
    {
        return this.name;
    }

    @Override
    public int compareTo(SteamApp o)
    {
        if(this.toString().compareTo(o.toString()) > 0)
            return 1;
        else if(this.toString().compareTo(o.toString()) == 0)
            return 0;
        else if(this.toString().compareTo(o.toString()) < 0)
            return -1;
        return 0;
    }

    public int getId()
    {
        return this.id;
    }
}
