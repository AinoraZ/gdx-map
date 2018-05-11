package com.gdx.map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Main Window of the program. Responsible for accessing all other Windows.
 */
public class FlagWindow extends JFrame {
    FlagWindow _this;
    FlagActor flag;
    JPanel mainPane = new JPanel(new BorderLayout());
    JPanel infoPane = new JPanel(new GridLayout(0,2));
    JTextField country = new JTextField();
    JTextField code = new JTextField();
    JTextField name = new JTextField();
    JTextField extra = new JTextField();

    /**
     * Draws the Window and sets up the other Window instances.
     */
    public FlagWindow(FlagActor flag) {
        this.flag = flag;
        add_flag_info();
        showUI();

        _this = this;
    }

    private void add_flag_info(){
        JLabel label = new JLabel("Country");
        infoPane.add(label); infoPane.add(country);
        label = new JLabel("Code");
        infoPane.add(label); infoPane.add(code);
        label = new JLabel("Name");
        infoPane.add(label); infoPane.add(name);
        label = new JLabel("Extra");
        infoPane.add(label); infoPane.add(extra);

        JButton button = new JButton("Submit");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag.country = country.getText();
                flag.code = code.getText();
                flag.name = name.getText();
                flag.extra = extra.getText();
                flag.validate();
                _this.dispose();
            }
        });

        button.setBackground(new Color(46, 204, 113));

        infoPane.add(new JLabel());
        infoPane.add(button);
        mainPane.add(infoPane);
    }


    public void showUI() {
        this.add(mainPane);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();

        Rectangle r = this.getBounds();

        this.setSize(500, r.height);
        this.setLocationRelativeTo(null);

        this.setVisible(true);
        this.setTitle("Flag Info");
    }
}