package com.gdx.map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creates new swing window for inputting information about the FlagActor on the map.
 * @author Ainoras Žukauskas
 * @version 2018-05-14
 */
public class FlagWindow extends Window {
    private static final int DEFAULT_WIDTH = 500;
    static final int HOVER_ADJUST_Y = 50;
    private String title = "Flag Info";

    private FlagWindow _this;
    private FlagActor flag;
    private JPanel infoPane = new JPanel(new GridLayout(0,2));
    private JTextField country = new JTextField();
    private JTextField code = new JTextField();
    private JTextField name = new JTextField();
    private JTextField extra = new JTextField();

    /**
     * Sets up the swing window for inputting information into the FlagActor instance.
     * @param flag FlagActor instance which spawned this FlagWindow for information filling
     * @see FlagActor
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

        button.setBackground(ColorLibrary.FLAT_GREEN);

        infoPane.add(new JLabel());
        infoPane.add(button);
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

        this.setVisible(true);
        this.setTitle(title);
    }
}