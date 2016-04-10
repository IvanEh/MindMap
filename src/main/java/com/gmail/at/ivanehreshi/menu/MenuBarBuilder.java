package com.gmail.at.ivanehreshi.menu;

import com.gmail.at.ivanehreshi.Strings;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;

/**
 * Menu bar is not customized so it could be aggregated along with all
 * it's components. Apart from this <i>menu bar</i> is a complex process
 * so it should be break down into smaller ones
 */
public class MenuBarBuilder {

    public JMenuBar build() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new FileMenuBuilder().build();
        JMenu menuHelp = new HelpMenuBuilder().build();

        menuBar.add(menuFile);
        menuBar.add(menuHelp);

        return  menuBar;
    }



    ;

}
