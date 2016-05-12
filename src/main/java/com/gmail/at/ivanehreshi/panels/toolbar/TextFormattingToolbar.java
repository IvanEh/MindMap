package com.gmail.at.ivanehreshi.panels.toolbar;

import com.gmail.at.ivanehreshi.customui.MindMapDrawer;
import com.gmail.at.ivanehreshi.customui.controllers.TextFormattingController;
import com.gmail.at.ivanehreshi.utils.HtmlDrawer;
import com.gmail.at.ivanehreshi.utils.Utilities;

import javax.swing.*;
import java.awt.*;

public class TextFormattingToolbar extends JToolBar {
    private MindMapDrawer mindMapDrawer;
    private JComboBox<String> fontsList;
    private JComboBox<Integer> fontSizeList;
    private JButton colorButton;
    private JButton bold;
    private JButton italic;
    private JButton underscored;

    private TextFormattingController textFormattingController;

    public TextFormattingToolbar(MindMapDrawer mindMapDrawer) {
        this.mindMapDrawer = mindMapDrawer;
        setLayout(new FlowLayout());
        createGui();
        setUpControllers();
    }

    private void setUpControllers() {
        textFormattingController = new TextFormattingController(mindMapDrawer);

        fontsList.addActionListener(textFormattingController::onFontSelect);
        fontSizeList.addActionListener(textFormattingController::onFontSizeSelect);
        colorButton.addActionListener(textFormattingController::onColorButtonPressed);
        bold.addActionListener(textFormattingController::onBoldButtonPressed);
        italic.addActionListener(textFormattingController::onItalicButtonPressed);
        underscored.addActionListener(textFormattingController::onUnderscoredButtonPressed);
    }

    private void createGui() {
        fontsList = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames());
        add(fontsList);

        fontSizeList = new JComboBox<Integer>(Utilities.genRangeArr(1, 10));
        add(fontSizeList);

        colorButton = new JButton();
        add(colorButton);

        bold = new JButton(HtmlDrawer.encloseWithHtml(
                HtmlDrawer.encloseWithDecoration("b", false, true, false)
        ));
        add(bold);

        italic = new JButton(HtmlDrawer.encloseWithHtml(
                HtmlDrawer.encloseWithDecoration("i", true, false, false)
        ));
        add(italic);

        underscored = new JButton(HtmlDrawer.encloseWithHtml(
                HtmlDrawer.encloseWithDecoration("u", false, false, true)
        ));
        add(underscored);
    }

}
