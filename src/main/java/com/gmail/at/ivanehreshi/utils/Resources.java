package com.gmail.at.ivanehreshi.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Resources {
    private Map<String, BufferedImage> imageMap;

    public Resources() {
        this.imageMap = new HashMap<>();
    }

    public BufferedImage loadImage(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return null;
        }

        BufferedImage bufImage = null;
        try {
            bufImage = ImageIO.read(file);
            imageMap.put(path, bufImage);
        } catch (IOException e) {
            return null;
        }

        return bufImage;

    }

    public BufferedImage getImage(String path, boolean tryLoad) {
        BufferedImage bufferedImage = imageMap.get(path);
        if(bufferedImage == null) {
            if(tryLoad) {
                loadImage(path);
                return getImage(path, tryLoad);
            } else {
                return null;
            }
        }

//        if((imageIcon.getImageLoadStatus() & MediaTracker.COMPLETE) == MediaTracker.COMPLETE) {
            return bufferedImage;
//        } else  {
//            return null;
//        }
    }

}
