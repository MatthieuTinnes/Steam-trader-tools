package com.matthieu42.steamtradertools.model;

import com.github.goive.steamapi.exceptions.SteamApiException;
import javafx.concurrent.Task;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Matthieu on 06/03/2017.
 */
@XmlRootElement(name = "SteamAppList")
@XmlAccessorType(XmlAccessType.FIELD)
public class AllAppList
{
    private Map<Integer, String> appList;

    public AllAppList()
    {

    }

    public Task<Void> init() throws InterruptedException, SteamApiException
    {
            return new Task<Void>()
            {

                @Override
                public Void call() throws InterruptedException, SteamApiException
                {
                    appList = SteamApiStatic.steamApi.listApps();
                    return null;
                }

            };

    }

    public Collection<String> getAppNameList()
    {
        return appList.values();
    }

    public void saveToXml()
    {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(this.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File file = new File("steamAppList.xml");
            marshaller.marshal(this,file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void loadFromXml() throws JAXBException
    {
        File file = new File("steamAppList.xml");
        JAXBContext context = JAXBContext.newInstance(this.getClass());
        Unmarshaller um;
        um = context.createUnmarshaller();
        AllAppList loadedList = (AllAppList) um.unmarshal(file);
        this.appList = loadedList.appList;
    }
}
