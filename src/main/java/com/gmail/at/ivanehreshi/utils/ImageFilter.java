package com.gmail.at.ivanehreshi.utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ImageFilter extends FileFilter{
    private static final List<String> EXTENSIONS =
            Arrays.asList("jpg", "jpeg", "png");

    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;

        String extension = Utilities.getExtension(f);
        return EXTENSIONS.contains(extension);

    }

    @Override
    public String getDescription() {
        return "Images";
    }


}
