package com.matthieu42.steamtradertools.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthieu on 12/03/17.
 */

public enum KeyCurrentUse
{
    RESERVED (I18n.getMessage("reservedfortrade")),
    KEEP (I18n.getMessage("keep")),
    INGIVEAWAY (I18n.getMessage("ingiveaway"));

    private String name = "";

    KeyCurrentUse(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }

    public static List<String> toList()
    {
        List<String> list = new ArrayList<String>();
        for(KeyCurrentUse k:KeyCurrentUse.values())
        {
            list.add(k.toString());
        }
        return list;
    }
}
