package com.gdx.map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creates window which let's user search for specific FlagActors according to country, code and name.
 * @author Ainoras Žukauskas
 * @version 2018-05-14
 */
public class InfoWindow extends Window {
    GDXMap map;

    JPanel infoPane = new JPanel(new GridLayout(0,2));
    JTextField country = new JTextField();
    JTextField code = new JTextField();
    JTextField name = new JTextField();
    public Rectangle r;


    /**
     * Sets up the InfoWindow instance for searching the map of specific FlagActors
     * @param map The main map window GDXMap reference.
     * @see GDXMap
     * @see FlagActor
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

        JButton clear = new JButton("Clear Filter");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name.setText("");
                code.setText("");
                country.setText("");
            }
        });

        JButton button = new JButton("Clear Flags");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                map.clear_flags();
            }
        });

        clear.setBackground(new Color(52, 152, 219));
        button.setBackground(new Color(231, 76, 60));

        infoPane.add(clear); infoPane.add(button);
        mainPane.add(infoPane);
    }

    /**
     * Sets up Window instance size, title and other necessary things to show the Window
     */
    public void showUI() {
        this.add(mainPane);

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.pack();

        r = this.getBounds();

        this.setSize(600, r.height);

        r = this.getBounds();

        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.setTitle("Map Functions");
    }
}
