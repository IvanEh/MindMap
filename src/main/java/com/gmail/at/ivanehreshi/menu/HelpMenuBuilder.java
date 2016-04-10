package com.gmail.at.ivanehreshi.menu;

import com.gmail.at.ivanehreshi.Strings;

import javax.swing.*;

public class HelpMenuBuilder {
    public JMenu build() {
        JMenu menuHelp = new JMenu(Strings.Menu.HELP);

        JMenuItem menuItemAbout = buildAboutMenu();
        menuHelp.add(menuItemAbout);

        return  menuHelp;
    }

    private JMenuItem buildAboutMenu() {
        JMenuItem menuAbout = new JMenuItem(Strings.Menu.ABOUT);
        return menuAbout;
    }
}
