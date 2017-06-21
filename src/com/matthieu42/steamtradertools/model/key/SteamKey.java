package com.matthieu42.steamtradertools.model.key;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * Created by matthieu on 11/03/17.
 */
@XmlRootElement
public class SteamKey
{

    private StringProperty  key;
    private StringProperty  state;
    private StringProperty currentUse;
    private BooleanProperty used;
    private final StringProperty dateAdded;

    public SteamKey()
    {
        this.key = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.currentUse = new SimpleStringProperty("");
        this.used = new SimpleBooleanProperty(false);
        this.dateAdded = new SimpleStringProperty(LocalDateTime.now().format(ISO_LOCAL_DATE));
    }
    public SteamKey(String steamKey) {
        this.key = new SimpleStringProperty(steamKey);
        this.state = new SimpleStringProperty("");
        this.currentUse = new SimpleStringProperty("");
        this.used = new SimpleBooleanProperty(false);
        this.dateAdded = new SimpleStringProperty(LocalDateTime.now().format(ISO_LOCAL_DATE));
    }
    @XmlElement
    public String getKey() {
        return key.get();
    }
    @XmlElement
    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state = new SimpleStringProperty(state);
    }
    @XmlElement
    public String getCurrentUse() {
        return currentUse.get();
    }

    public void setCurrentUse(String currentUse) {
        this.currentUse = new SimpleStringProperty(currentUse);
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

    public StringProperty stateProperty()
    {
        return state;
    }

    public StringProperty currentUseProperty()
    {
        return currentUse;
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

}
