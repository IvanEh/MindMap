package com.gmail.at.ivanehreshi.customui.controllers;

import com.gmail.at.ivanehreshi.MindMapApplication;
import com.gmail.at.ivanehreshi.customui.NodeStylesheet;
import com.gmail.at.ivanehreshi.customui.NodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TextFormattingController {
    MindMapController mindMapController;

    public TextFormattingController(MindMapController mindMapController) {
        this.mindMapController = mindMapController;
    }

    public void onFontSelect(ActionEvent actionEvent) {
        JComboBox<String> comboBox = (JComboBox<String>) actionEvent.getSource();
        mindMapController.getSelection()
                .forEach(view -> view.getModel().getProps()
                                     .put(NodeStylesheet.FONT_NAME, comboBox.getSelectedItem()));
    }

    public void onFontSizeSelect(ActionEvent actionEvent) {
        JComboBox<Integer> comboBox = (JComboBox<Integer>) actionEvent.getSource();
        mindMapController.getSelection().stream()
                .map(view -> view.getModel().getProps())
                .forEach(props -> props.put(NodeStylesheet.FONT_SIZE, (int) comboBox.getSelectedItem()));
    }

    public void onColorButtonPressed(ActionEvent actionEvent) {
        JButton button = (JButton) actionEvent.getSource();
        Color color = JColorChooser.showDialog(MindMapApplication.getInstance(),
                                 "Колір", button.getBackground());
        button.setBackground(color);
        mindMapController.getSelection().stream()
                .map(view -> view.getModel().getProps())
                .forEach(props -> props.put(NodeStylesheet.FONT_COLOR, color));
    }

    public void onBoldButtonPressed(ActionEvent actionEvent) {
        JButton button = (JButton) actionEvent.getSource();
        mindMapController.getSelection().stream()
                .map(view -> view.getModel().getProps())
                .forEach(props -> props.toggle(NodeStylesheet.BOLD_TITLE));
    }

    public void onItalicButtonPressed(ActionEvent actionEvent) {
        JButton button = (JButton) actionEvent.getSource();
        mindMapController.getSelection().stream()
                .map(view -> view.getModel().getProps())
                .forEach(props -> props.toggle(NodeStylesheet.ITALIC_TITLE));
    }

    public void onUnderscoredButtonPressed(ActionEvent actionEvent) {
        JButton button = (JButton) actionEvent.getSource();
        mindMapController.getSelection().stream()
                .map(view -> view.getModel().getProps())
                .forEach(props -> props.toggle(NodeStylesheet.UNDERSCORED_TITLE));
    }
}
