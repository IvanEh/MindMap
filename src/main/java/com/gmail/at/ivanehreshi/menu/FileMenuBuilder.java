package com.gmail.at.ivanehreshi.menu;

import com.gmail.at.ivanehreshi.Strings;
import com.gmail.at.ivanehreshi.actions.menu.file.CloseAction;

import javax.swing.*;

public class FileMenuBuilder {
    public JMenu build() {
        JMenu menuFile = new JMenu(Strings.Menu.FILE);

        JMenuItem menuItemClose = buildCloseMenu();
        menuFile.add(menuItemClose);

        return menuFile;
    }

    private JMenuItem buildCloseMenu() {
        JMenuItem menuItemClose = new JMenuItem(new CloseAction());

        return menuItemClose;
    }

}
