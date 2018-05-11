package com.gdx.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MapActor extends Actor {
    static final int WORLD_WIDTH = 1230;
    static final int WORLD_HEIGHT = 1000;

    private Sprite mapSprite = new Sprite(new Texture(Gdx.files.internal("map.png")));

    public MapActor(){
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        mapSprite.draw(batch);
    }

    @Override
    public void act(float delta){

    }
}
