package com.gdx.map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoWindow extends JFrame {
    GDXMap map;

    JPanel mainPane = new JPanel(new BorderLayout());
    JPanel infoPane = new JPanel(new GridLayout(0,2));
    JTextField country = new JTextField();
    JTextField code = new JTextField();
    JTextField name = new JTextField();

    /**
     * Draws the Window and sets up the other Window instances.
     */
    public InfoWindow(GDXMap map) {
        this.map = map;
        add_map_functions();
        showUI();
    }

    private void add_map_functions(){
        JLabel label = new JLabel("Search by Country");
        infoPane.add(label); infoPane.add(country);
        label = new JLabel("Search by Code");
        infoPane.add(label); infoPane.add(code);
        label = new JLabel("Search by Name");
        infoPane.add(label); infoPane.add(name);

        label = new JLabel();
        infoPane.add(label); infoPane.add(label);

        JButton button = new JButton("Clear Flags");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.clear_flags();
            }
        });

        button.setBackground(new Color(231, 76, 60));

        infoPane.add(label);
        infoPane.add(button);
        mainPane.add(infoPane);
    }

    public void showUI() {
        this.add(mainPane);

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.pack();

        Rectangle r = this.getBounds();

        this.setSize(600, r.height);
        this.setLocationRelativeTo(null);

        this.setTitle("Map Functions");
    }
}
