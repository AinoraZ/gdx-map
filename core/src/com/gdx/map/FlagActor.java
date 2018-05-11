package com.gdx.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class FlagActor extends Actor {
    private float x;
    private float y;
    public String country;
    public String code;
    public String name;
    public String extra;

    private Sprite flagSprite = new Sprite(new Texture(Gdx.files.internal("flag.png")));;

    public FlagActor(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void act(float delta){

    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        flagSprite.setPosition(x, y);
        flagSprite.setSize(3, 3);
        flagSprite.draw(batch);
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}
