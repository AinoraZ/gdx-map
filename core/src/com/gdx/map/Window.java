package com.gdx.map;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract Window class, which should be used for all swing windows.
 *
 * @author Ainoras Žukauskas
 * @version 2018-05-14
 */
public abstract class Window extends JFrame {
    JPanel mainPane = new JPanel(new BorderLayout());

    public abstract void showUI();
}
