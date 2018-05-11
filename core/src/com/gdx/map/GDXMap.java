package com.gdx.map;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class GDXMap implements ApplicationListener {
	static final int WORLD_WIDTH = 246;
	static final int WORLD_HEIGHT = 200;
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
    FlagActor chosen_actor;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT * (h / w));

		batch = new SpriteBatch();
		infoWindow = new InfoWindow(this);

		viewport = new ExtendViewport(cam.viewportWidth / 2f, cam.viewportHeight / 2f, cam);
		stage = new Stage(viewport, batch);

		stage.addActor(new MapActor());

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new MyInputProcessor(this));
        Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render() {
		handleInput();

        if(need_fix) {
            cam.zoom = WORLD_WIDTH / cam.viewportWidth;
            need_fix = false;
        }

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
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
		    //System.out.println(cam.zoom);
		    int translate_x = Gdx.input.getDeltaX() * 2 * (-1);
		    int translate_y = Gdx.input.getDeltaY() * 2;

		    if(cam.zoom < 0.8f){
		        translate_x = Gdx.input.getDeltaX() * (-1);
		        translate_y = Gdx.input.getDeltaY();
            }

            cam.translate(translate_x, translate_y, 0);
		    //System.out.println(Gdx.input.getDeltaX() + " " + Gdx.input.getDeltaY());
        }
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            if(Gdx.input.justTouched()){
				mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				cam.unproject(mouse_position);
				FlagActor temp_flag = new FlagActor(mouse_position.x, mouse_position.y);
				stage.addActor(temp_flag);
				actors.add(temp_flag);
                pause();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)){
            if(Gdx.input.isKeyJustPressed(Input.Keys.F) && !infoWindow.isVisible()){
                infoWindow.setVisible(true);
            }
        }

		cam.zoom = MathUtils.clamp(cam.zoom, 0.3f, WORLD_WIDTH/cam.viewportWidth);

		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, WORLD_WIDTH - effectiveViewportWidth / 2f);
		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, WORLD_HEIGHT - effectiveViewportHeight / 2f);

		handleFlags();
	}

	private void handleFlags(){
        mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mouse_position);

        if(!hovered) {
            for (FlagActor flag : actors) {
                if (Math.abs(flag.getX() - mouse_position.x) < 2 && Math.abs(flag.getY() - mouse_position.y) < 2) {
                    if(flag.isHoverable()) {
                        hovered = true;
                        chosen_actor = flag;
                        System.out.println("Hovered");
                    }
                }
            }
        }
        else{
            if (Math.abs(chosen_actor.getX() - mouse_position.x) > 2 || Math.abs(chosen_actor.getY() - mouse_position.y) > 2) {
                hovered = false;
            }
        }
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
        map.camera_zoom(amount * 2);
        return true;
    }
}
