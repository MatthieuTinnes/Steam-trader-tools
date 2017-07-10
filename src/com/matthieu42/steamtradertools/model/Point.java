package com.matthieu42.steamtradertools.model;

/**
 * Created by Matthieu on 27/04/2017.
 */
public class Point
{
    private double x;
    private double y;
    public Point()
    {

    }

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }
    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }


}
