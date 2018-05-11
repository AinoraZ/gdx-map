package com.gdx.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FlagActor extends Actor {
    FlagWindow flagWindow;
    FlagActor _this = this;

    private float x;
    private float y;
    public String country;
    public String code;
    public String name;
    public String extra;

    private boolean hoverable = false;

    private Sprite flagSprite = new Sprite(new Texture(Gdx.files.internal("flag.png")));;

    public FlagActor(float x, float y){
        this.x = x;
        this.y = y;

        setPosition(x, y);
        setSize(3, 3);

        flagSprite.setPosition(x, y);
        flagSprite.setSize(3, 3);

        flagWindow = new FlagWindow(this);
        flagWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                remove_actor();
            }
        });

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(hoverable){
                    System.out.println("Works");
                }
            }
        });
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        flagSprite.draw(batch);
    }

    public void remove_actor(){
        addAction(Actions.removeActor(this));
    }

    public void validate(){
        if(country.equals("") || code.equals("") || name.equals("")){
            remove_actor();
        }
        else{
            hoverable = true;
        }
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}
