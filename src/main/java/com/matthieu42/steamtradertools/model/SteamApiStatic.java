package com.matthieu42.steamtradertools.model;

import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;
import javafx.concurrent.Task;

/**
 * Created by Matthieu on 13/03/2017.
 */
public final class SteamApiStatic
{
    public static final SteamApi steamApi = new SteamApi();

    public static Task<SteamApp> retrieve(int id) throws InterruptedException, SteamApiException
    {
        return new Task<SteamApp>()
        {

            @Override
            public SteamApp call() throws InterruptedException, SteamApiException
            {
                return steamApi.retrieve(id);
            }

        };

    }
}
