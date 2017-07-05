package com.matthieu42.steamtradertools.model.steamapp;


import com.matthieu42.steamtradertools.model.key.SteamKey;

import javax.xml.bind.annotation.*;
import java.util.HashSet;

/**
 * Created by Matthieu on 11/05/2017.
 */
@XmlRootElement
@XmlSeeAlso({LinkedSteamAppWithKey.class,NotLinkedSteamAppWithKey.class})
public abstract class AbstractSteamAppWithKey implements Comparable<AbstractSteamAppWithKey>
{
    @XmlElement
    protected String name;
    @XmlElementWrapper(name = "steamKeys")
    @XmlElement(name = "steamKey")
    private HashSet<SteamKey> steamKeyList;
    @XmlElement
    private int nbTotalKey;

    public AbstractSteamAppWithKey(){

    }
    public AbstractSteamAppWithKey(String name){
        this.name = name;
        this.steamKeyList = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    @Override
    public int compareTo(AbstractSteamAppWithKey o)
    {
        if(this.toString().compareTo(o.toString()) > 0)
            return 1;
        else if(this.toString().compareTo(o.toString()) == 0)
            return 0;
        else if(this.toString().compareTo(o.toString()) < 0)
            return -1;
        return 0;
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
