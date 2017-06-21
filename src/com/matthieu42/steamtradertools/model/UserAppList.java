package com.matthieu42.steamtradertools.model;

import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;
import com.matthieu42.steamtradertools.model.steamapp.SteamAppWithKey;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.util.TreeSet;
import java.util.prefs.Preferences;

/**
 * Created by Matthieu on 07/03/2017.
 */

@XmlRootElement(name = "UserAppList")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAppList
{
    @XmlElementWrapper(name = "steamApps")
    @XmlElement(name = "steamApp")
    private TreeSet<SteamAppWithKey> appList;
    @XmlElement(name = "nbTotalKey")
    private int nbTotalKey;


    UserAppList()
    {
        appList = new TreeSet<>();
        nbTotalKey = 0;
    }

    public void addApp(SteamAppWithKey app)
    {
        this.appList.add(app);
    }

    public void delApp(SteamAppWithKey app)
    {
        this.appList.remove(app);
    }

    public TreeSet<SteamAppWithKey> getAppList()
    {
        return appList;
    }

    public void updateNbTotalKey()
    {
        int total = 0;
        for(SteamAppWithKey app : this.appList)
        {
            total += app.getNbTotalKey();
        }
        this.nbTotalKey = total;
    }


    public int getNbTotalKey()
    {
        return nbTotalKey;
    }

    public int getNbTotalApp()
    {
        return appList.size();
    }

    private void setAppList(TreeSet<SteamAppWithKey> appList) {
        this.appList = appList;
    }

    public void loadFromXml(File file) throws JAXBException, SteamApiException
    {
        if(file != null)
        {
                JAXBContext context = JAXBContext.newInstance(this.getClass());
                Unmarshaller um;
                um = context.createUnmarshaller();
                Preferences prefs = Preferences.userNodeForPackage(com.matthieu42.steamtradertools.controller.AppController.class);
                prefs.put(PreferencesKeys.SAVE_PATH.toString(),file.getAbsolutePath());
                UserAppList loadedList = (UserAppList) um.unmarshal(file);
                for(SteamAppWithKey a : loadedList.getAppList())
                {
                    a.setApp(a.getId());
                }
                this.setAppList(loadedList.getAppList());
                this.nbTotalKey = loadedList.nbTotalKey;


        }
    }
    public void saveToXml(File file) throws JAXBException
    {
        JAXBContext context;
        context = JAXBContext.newInstance(this.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(this, file);

    }

    public void importFromCsv(File csv) throws IOException
    {
        FileInputStream csvData = new FileInputStream(csv);
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            TreeSet<SteamAppWithKey> appList = new TreeSet<>();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                SteamAppWithKey newApp = new SteamAppWithKey(Integer.parseInt(values[0]));
                appList.add(newApp);
                try
                {
                    SteamApp app = SteamApiStatic.steamApi.retrieve(values[0]);
                    newApp.setApp(app);

                } catch (SteamApiException e)
                {
                    e.printStackTrace();
                }

                for(int i = 1 ; i < values.length ; i++)
                {
                    newApp.addKey(values[i]);
                }
                UserAppList newList = new UserAppList();
                newList.setAppList(appList);
                try
                {
                    newList.saveToXml(new File("importedData.xml"));
                } catch (JAXBException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
