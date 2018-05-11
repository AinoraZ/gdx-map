package com.gdx.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FlagActor extends Actor {
    FlagWindow flagWindow;
    FlagActor _this = this;
    GDXMap _map;

    private float x;
    private float y;
    public String country = "";
    public String code = "";
    public String name = "";
    public String extra = "";

    public String search_country = "";
    public String search_code = "";
    public String search_name = "";

    private boolean hoverable = false;
    private boolean doneEditing = false;

    private Sprite flagSprite = new Sprite(new Texture(Gdx.files.internal("flag.png")));;

    public FlagActor(float x, float y, GDXMap map){
        this.x = x;
        this.y = y;
        this._map = map;

        setPosition(x, y);
        setSize(3, 3);

        flagSprite.setPosition(x, y);
        flagSprite.setSize(3, 3);

        flagWindow = new FlagWindow(this);
        flagWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if(!hoverable)
                    remove_actor();
            }
        });

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                if(hoverable && !_map.hovered){
                    _map.hovered = true;
                    System.out.println("Works");
                }
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                _map.hovered = false;
            }
        });
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        if( name.toLowerCase().startsWith(search_name.toLowerCase()) &&
            code.toLowerCase().startsWith(search_code.toLowerCase()) &&
            country.toLowerCase().startsWith(search_country.toLowerCase())
        ){
            flagSprite.draw(batch);
        }
    }

    public void remove_actor(){
        addAction(Actions.removeActor(this));
    }

    public void validate(){
        doneEditing = true;
        if(country.equals("") || code.equals("") || name.equals("")){
            remove_actor();
        }
        else{
            hoverable = true;
        }
    }

    public boolean isDoneEditing(){
        return doneEditing;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}
