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
