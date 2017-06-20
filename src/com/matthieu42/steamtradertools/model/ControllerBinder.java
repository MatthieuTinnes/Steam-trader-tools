package com.matthieu42.steamtradertools.model;

import com.matthieu42.steamtradertools.controller.AppController;

/**
 * Created by Matthieu on 07/03/2017.
 */
public class ControllerBinder
{

    public AppController appController;


    public ControllerBinder(AppController controller)
    {
        this.appController = controller;
    }
}
