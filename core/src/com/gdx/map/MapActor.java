package com.gdx.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * The Actor class which displays the actual map image on the GDXMap stage
 * @author Ainoras Žukauskas
 * @version 2018-05-14
 * @see Actor
 */
public class MapActor extends Actor {
    private Sprite mapSprite = new Sprite(new Texture(Gdx.files.internal("map.png")));

    /**
     * Sets up the position and size of the image that has to be drawn
     */
    public MapActor(){
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(GDXMap.WORLD_WIDTH, GDXMap.WORLD_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        mapSprite.draw(batch);
    }

    @Override
    public void act(float delta){

    }
}
