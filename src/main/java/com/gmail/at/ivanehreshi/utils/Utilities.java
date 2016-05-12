package com.gmail.at.ivanehreshi.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Utilities {
    private static final HtmlDrawer htmlDrawer = new HtmlDrawer();

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

    public static HtmlDrawer getHtmlDrawer() {
        return htmlDrawer;
    }

    public static Integer[] genRangeArr(int begin, int end) {
        Integer[] arr = new Integer[end - begin + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = begin + i;
        }
        return arr;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
