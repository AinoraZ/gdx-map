package com.gdx.map;

import javax.swing.*;
import java.awt.*;

public class HoverWindow extends JFrame{
    GDXMap map;

    JPanel mainPane = new JPanel(new BorderLayout());
    JPanel infoPane = new JPanel(new GridLayout(0,2));
    JLabel country = new JLabel();
    JLabel code = new JLabel();
    JLabel name = new JLabel();
    JLabel extra = new JLabel();


    public HoverWindow(String country, String code, String name, String extra) {
        this.country.setText(country);
        this.code.setText(code);
        this.name.setText(name);
        this.extra.setText(extra);

        mainPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

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

    public void showUI() {
        this.add(mainPane);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();

        Rectangle r = this.getBounds();

        this.setSize(500, r.height);

        r = this.getBounds();

        int x = MouseInfo.getPointerInfo().getLocation().x - (r.width/2);
        int y = MouseInfo.getPointerInfo().getLocation().y - (r.height + 50);

        setLocation(x, y);

        this.setResizable(false);
        this.setTitle("Bookmark Info");

        this.setVisible(true);
    }
}
