package com.gmail.at.ivanehreshi.menu;

import com.gmail.at.ivanehreshi.Strings;

import javax.swing.*;

public class FileMenuBuilder {
    public JMenu build() {
        JMenu menuFile = new JMenu(Strings.Menu.FILE);

        JMenuItem menuItemClose = buildCloseMenu();
        menuFile.add(menuItemClose);

        return menuFile;
    }

    private JMenuItem buildCloseMenu() {
        JMenuItem menuItemClose = new JMenuItem(Strings.Menu.CLOSE);

        return menuItemClose;
    }
}
