package com.gdx.map;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GDXMap implements ApplicationListener {
    static final int WORLD_WIDTH = 1230;
    static final int WORLD_HEIGHT = 1000;
	static final float ZOOM_DELTA = 0.015f;

	private OrthographicCamera cam;
	private SpriteBatch batch;

	private boolean need_fix = true;
	private Vector3 mouse_position = new Vector3(0,0,0);

    private InfoWindow infoWindow;

    private ExtendViewport viewport;
    private Stage stage;

    private List<FlagActor> actors = new ArrayList<FlagActor>();

    boolean hovered = false;
    boolean pop_up = false;
    boolean paused = false;
    private FlagActor temp_flag;

    FlagActor hoverFlag;
    HoverWindow hoverWindow;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT * (h / w));
		batch = new SpriteBatch();
		infoWindow = new InfoWindow(this);

		viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
		stage = new Stage(viewport, batch);

		stage.addActor(new MapActor());

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new MyInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render() {
        if (need_fix) {
            cam.zoom = WORLD_WIDTH / cam.viewportWidth;
            need_fix = false;
        }

		if(!paused) {
            if(hovered){
                if(!pop_up) {
                    pop_up = true;
                    hoverWindow = new HoverWindow(hoverFlag.country, hoverFlag.code, hoverFlag.name, hoverFlag.extra);

                    System.out.println("Hello");
                }
            }
            else{
                if(pop_up) {
                    pop_up = false;
                    hovered = false;
                    hoverWindow.dispose();
                }
            }
            handleInput();
            flag_filter();
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();
        }
        else{
            paused = !temp_flag.isDoneEditing();
        }
	}

	public void flag_filter(){
	    for(FlagActor flag : actors){
	        flag.search_country = infoWindow.country.getText();
	        flag.search_code = infoWindow.code.getText();
	        flag.search_name = infoWindow.name.getText();
        }
    }

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.zoom += ZOOM_DELTA;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.zoom -= ZOOM_DELTA;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.translate(-2, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.translate(2, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.translate(0, -2, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.translate(0, 2, 0);
		}
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
		    int translate_x = Gdx.input.getDeltaX() * 6 * (-1);
		    int translate_y = Gdx.input.getDeltaY() * 6;

		    if(cam.zoom < 0.8f){
		        translate_x = Gdx.input.getDeltaX() * 3 * (-1);
		        translate_y = Gdx.input.getDeltaY() * 3;
            }

            cam.translate(translate_x, translate_y, 0);
        }
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            if(Gdx.input.justTouched()){
				add_flag();
				paused = true;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)){
            if(Gdx.input.isKeyJustPressed(Input.Keys.F) && !infoWindow.isVisible()){
                infoWindow.setVisible(true);
                int x = MouseInfo.getPointerInfo().getLocation().x - 300;
                int y = MouseInfo.getPointerInfo().getLocation().y - (infoWindow.r.height + 50);

                infoWindow.setLocation(x, y);
            }
        }

		cam.zoom = MathUtils.clamp(cam.zoom, 0.2f, WORLD_WIDTH/cam.viewportWidth);

		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, WORLD_HEIGHT - effectiveViewportHeight / 2f);
	}

	public void add_flag(){
        mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mouse_position);
        temp_flag = new FlagActor(mouse_position.x, mouse_position.y, this);
        stage.addActor(temp_flag);
        actors.add(temp_flag);
    }

    public void camera_zoom(int amount){
        cam.zoom += ZOOM_DELTA * amount;
    }

    public void clear_flags(){
        for(FlagActor actor : actors){
            actor.remove_actor();
        }
        while(!actors.isEmpty()){
            actors.remove(actors.size() - 1);
        }
    }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void pause() {

	}
}

class MyInputProcessor extends InputAdapter {
    private GDXMap map;

    public MyInputProcessor(GDXMap map){
        this.map = map;
    }

    @Override
    public boolean scrolled(int amount){
    	if(map.paused)
    		return true;
        map.camera_zoom(amount * 2);
        return true;
    }
}
