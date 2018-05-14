package com.matthieu42.steamtradertools.model.ImageCache;

import com.matthieu42.steamtradertools.model.steamapp.LinkedSteamAppWithKey;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageCacheHandler
{
    private HashMap<Integer, Image> imageCache;

    public ImageCacheHandler(HashMap<Integer, Image> imageCache)
    {

        this.imageCache = imageCache;
    }

    public void saveImageCache() throws ImageCacheError
    {
        File dir = new File("cache/");
        if (!dir.exists())
            throw new ImageCacheError();
        for (Map.Entry<Integer, Image> i : imageCache.entrySet())
        {
            BufferedImage bi = SwingFXUtils.fromFXImage(i.getValue(), null);
            File file = new File("cache/" + i.getKey());
            if (!file.exists() && bi != null)
            {
                try
                {
                    ImageIO.write(bi, "png", file);
                } catch (IOException e)
                {
                    throw new ImageCacheError();
                }
            }

        }
    }

    public void loadImageCache()
    {
        File dir = new File("cache/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null)
        {
            for (File child : directoryListing)
            {
                if (Objects.equals(child.getName(), "Thumbs.db"))
                    child.delete();
                else
                {
                    Image ima = new Image("file:///" + child.getAbsolutePath());
                    imageCache.put(Integer.parseInt(child.getName()), ima);
                }


            }
        }
    }

    public void addImageToCache(LinkedSteamAppWithKey app)
    {
        Image banner = new Image(app.getHeaderImage());
        imageCache.put(app.getId(), banner);
    }

    public void delImageFromCache(LinkedSteamAppWithKey app) throws ImageCacheError
    {
        int id = app.getId();
        imageCache.remove(id);
        File cached = new File("cache/" + id);
        if (cached.exists())
        {
            if (!cached.delete())
            {
                throw new ImageCacheError();
            }
        }

    }

    public void deleteImageCache() throws ImageCacheError
    {
        File imageCacheDir = new File("cache/");
        File[] files = imageCacheDir.listFiles();
        if (files != null)
        {
            for(File f: files) {
                if(!f.delete())
                    throw new ImageCacheError();
            }
        }
        else
            throw new ImageCacheError();
    }
}
