package com.gdx.map;

import java.awt.*;
import java.awt.color.ColorSpace;

public final class ColorLibrary extends Color{
    public static final Color FLAT_RED = new Color(231, 76, 60);
    public static final Color FLAT_GREEN = new Color(46, 204, 113);
    public static final Color FLAT_BLUE = new Color(52, 152, 219);

    public ColorLibrary(ColorSpace cSpace, float[] components, float alpha){
        super(cSpace, components, alpha);
    }

    public ColorLibrary(float r, float g, float b){
        super(r, g, b);
    }

    public ColorLibrary(float r, float g, float b, float a){
        super(r, g, b, a);
    }

    public ColorLibrary(int rgb) {
        super(rgb);
    }

    public ColorLibrary(int rgba, boolean hasAlpha){
        super(rgba, hasAlpha);
    }

    public ColorLibrary(int r, int g, int b){
        super(r, g, b);
    }

    public ColorLibrary(int r, int g, int b, int a){
        super(r, g, b, a);
    }
}
