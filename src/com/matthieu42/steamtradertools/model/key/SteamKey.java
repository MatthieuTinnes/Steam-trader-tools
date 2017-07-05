package com.matthieu42.steamtradertools.model.key;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * Created by matthieu on 11/03/17.
 */
@XmlRootElement
public class SteamKey
{

    private StringProperty  key;
    private KeyState state;
    private KeyCurrentUse currentUse;
    private BooleanProperty used;


    private StringProperty dateAdded;

    public SteamKey()
    {
        this.key = new SimpleStringProperty("");
        this.state = KeyState.FONCTIONNAL;
        this.currentUse = KeyCurrentUse.FREE;
        this.used = new SimpleBooleanProperty(false);
        this.dateAdded = new SimpleStringProperty("");
    }
    public SteamKey(String steamKey) {
        this.key = new SimpleStringProperty(steamKey);
        this.state = KeyState.FONCTIONNAL;
        this.currentUse = KeyCurrentUse.FREE;
        this.used = new SimpleBooleanProperty(false);
        this.dateAdded = new SimpleStringProperty(LocalDateTime.now().format(ISO_LOCAL_DATE));
    }
    @XmlElement
    public String getKey() {
        return key.get();
    }
    @XmlElement
    public KeyState getState() {
        return state;
    }

    public void setState(KeyState state) {
        this.state = state;
    }

    @XmlElement
    public KeyCurrentUse getCurrentUse() {
        return currentUse;
    }

    public void setCurrentUse(KeyCurrentUse currentUse) {
        this.currentUse = currentUse;
    }

    @XmlElement
    public boolean isUsed()
    {
        return used.get();
    }

    public StringProperty keyProperty()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key.set(key);
    }

    public BooleanProperty usedProperty()
    {
        return used;
    }

    public void setUsed(boolean used)
    {
        this.used.set(used);
    }
    @XmlElement
    public String getDateAdded()
    {
        return dateAdded.get();
    }

    public void setDateAdded(String dateAdded)
    {
        this.dateAdded.set(dateAdded);
    }

}
