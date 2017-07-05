package com.matthieu42.steamtradertools.model.key;

import com.matthieu42.steamtradertools.model.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthieu on 11/03/17.
 */
public enum KeyState
{
    FONCTIONNAL (I18n.getMessage("fonctionnal")),
    NOTSURE (I18n.getMessage("notsure"));
    private String name = "";

    KeyState(String name) {
        this.name = name;
    }
    public String toString(){
        return name;
    }

    public static List<String> toList()
    {
        List<String> list = new ArrayList<>();
        for(KeyState k:KeyState.values())
        {
            list.add(k.toString());
        }
        return list;
    }
}
