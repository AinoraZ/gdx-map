package com.gdx.map;

import javax.swing.*;
import java.awt.*;

/**
 * Information Window containing market information
 * <p>
 * Pops up when marker is hovered
 *
 * @author Ainoras Žukauskas
 * @version 2018-05-14
 */
public class HoverWindow extends Window{
    private static final int DEFAULT_WIDTH = 500;
    private static final int HOVER_ADJUST_Y = 50;
    private static final int BORDER_SIZE = 10;
    private String title = "Bookmark Info";

    JPanel infoPane = new JPanel(new GridLayout(0,2));
    JLabel country = new JLabel();
    JLabel code = new JLabel();
    JLabel name = new JLabel();
    JLabel extra = new JLabel();

    /**
     * Set's up the Window instance to be shown immediately
     * @param country String value of the country the company belongs to.
     * @param code String value of the code of the company
     * @param name String value of the name of the company
     * @param extra String value of any extra information
     */
    public HoverWindow(String country, String code, String name, String extra) {
        this.country.setText(country);
        this.code.setText(code);
        this.name.setText(name);
        this.extra.setText(extra);

        mainPane.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        add_hover_info();
        showUI();
    }

    private void add_hover_info(){
        JLabel label = new JLabel("Country");
        infoPane.add(label); infoPane.add(country);
        label = new JLabel("Code");
        infoPane.add(label); infoPane.add(code);
        label = new JLabel("Name");
        infoPane.add(label); infoPane.add(name);
        label = new JLabel("Extra");
        infoPane.add(label); infoPane.add(extra);

        mainPane.add(infoPane);
    }

    /**
     * Sets up Window instance size, title and other necessary things to show the Window
     */
    public void showUI() {
        this.add(mainPane);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();

        Rectangle r = this.getBounds();

        this.setSize(DEFAULT_WIDTH, r.height);

        r = this.getBounds();

        int x = MouseInfo.getPointerInfo().getLocation().x - (r.width/2);
        int y = MouseInfo.getPointerInfo().getLocation().y - (r.height + HOVER_ADJUST_Y);

        setLocation(x, y);

        this.setResizable(false);
        this.setTitle(title);

        this.setVisible(true);
    }
}
