package com.gdx.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Actor class for putting markers on the GDXMap map stage.
 * @author Ainoras Žukauskas
 * @version 2018-05-14
 * @see Actor
 */
public class FlagActor extends Actor {
    private static final int DEFAULT_WIDTH = 18;
    private static final int DEFAULT_HEIGHT = 18;

    private FlagWindow flagWindow;
    private FlagActor _this = this;
    private GDXMap _map;

    private float x;
    private float y;
    String country = "";
    String code = "";
    String name = "";
    String extra = "";

    String search_country = "";
    String search_code = "";
    String search_name = "";

    private boolean hoverable = false;
    private boolean doneEditing = false;

    private Sprite flagSprite = new Sprite(new Texture(Gdx.files.internal("flag.png")));;

    /**
     * Sets up all the listeners and positions necessary for interacting with the FlagActor on the GDXMap
     * @param x The world float x position of the Actor. Never changes.
     * @param y The world float y position of the Actor. Never changes.
     * @param map The main GDXMap map window parent.
     * @param load Boolean value of whether it is being user generated (false) or loaded from save (true)
     * @see Actor
     */
    public FlagActor(float x, float y, GDXMap map, boolean load){
        this.x = x;
        this.y = y;
        this._map = map;

        setPosition(x, y);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        flagSprite.setPosition(x, y);
        flagSprite.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (hoverable && !_map.hovered) {
                    _map.hovered = true;
                    _map.hoverFlag = _this;
                }
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                _map.hovered = false;
            }
        });

        if(load){
            hoverable = true;
        }
        else {
            flagWindow = new FlagWindow(this);
            flagWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    doneEditing = true;
                    if (!hoverable)
                        clean_remove();
                }
            });
        }
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

    /**
     * Queues up the removal of this actor from the GDXMap parent
     */
    public void remove_actor(){
        addAction(Actions.removeActor(this));
    }

    /**
     * Removes flag from both the GDXMap parent stage and the flag array.
     */
    public void clean_remove(){
        _map.remove_flag(this);
        addAction(Actions.removeActor(this));
    }

    /**
     * Validates whether the input user gave is valid
     */
    public void validate(){
        doneEditing = true;
        if(country.equals("") || code.equals("") || name.equals("")){
            clean_remove();
        }
        else{
            hoverable = true;
        }
    }

    /**
     * Tells whether user is done editing flag information
     * @return boolean value
     */
    public boolean isDoneEditing(){
        return doneEditing;
    }

    /**
     * Getter for x world position
     * @return x world position float
     */
    public float getX(){
        return x;
    }

    /**
     * Getter for y world position
     * @return y world position float
     */
    public float getY(){
        return y;
    }
}
