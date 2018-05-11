package com.gdx.map;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.List;

public class GDXMap implements ApplicationListener {
	static final int WORLD_WIDTH = 246;
	static final int WORLD_HEIGHT = 200;
	static final float ZOOM_DELTA = 0.015f;

	private OrthographicCamera cam;
	private SpriteBatch batch;

	private Sprite mapSprite;
	private Sprite flagSprite;

	private List<Flag> flags = new ArrayList<Flag>();
	private boolean need_fix = true;
	private Vector3 mouse_position = new Vector3(0,0,0);

    private InfoWindow infoWindow;

    private ExtendViewport viewport;
    private Stage stage;

	@Override
	public void create() {
		/*
		mapSprite = new Sprite(new Texture(Gdx.files.internal("map.png")));
		mapSprite.setPosition(0, 0);
		mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
		*/

		//flagSprite = new Sprite(new Texture(Gdx.files.internal("flag.png")));

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT * (h / w));

		//cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		//cam.update();

		batch = new SpriteBatch();
		infoWindow = new InfoWindow(this);

		viewport = new ExtendViewport(cam.viewportWidth / 2f, cam.viewportHeight / 2f, cam);
		stage = new Stage(viewport, batch);

		stage.addActor(new MapActor());
	}

	@Override
	public void render() {
		handleInput();

        if(need_fix) {
            cam.zoom = WORLD_WIDTH / cam.viewportWidth;
            need_fix = false;
        }
		//cam.update();
		//batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	private void handleInput() {
	    Gdx.input.setInputProcessor(new MyInputProcessor(this));

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
		    System.out.println(cam.zoom);
		    int translate_x = Gdx.input.getDeltaX() * 2 * (-1);
		    int translate_y = Gdx.input.getDeltaY() * 2;

		    if(cam.zoom < 0.8f){
		        translate_x = Gdx.input.getDeltaX() * (-1);
		        translate_y = Gdx.input.getDeltaY();
            }

            cam.translate(translate_x, translate_y, 0);
		    System.out.println(Gdx.input.getDeltaX() + " " + Gdx.input.getDeltaY());
        }
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            if(Gdx.input.justTouched()){
				mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				cam.unproject(mouse_position);
				/*
				Flag temp_flag = new Flag(mouse_position.x, mouse_position.y);
                flags.add(temp_flag);
                */

				FlagActor temp_flag = new FlagActor(mouse_position.x, mouse_position.y);
				stage.addActor(temp_flag);

                //new FlagWindow(temp_flag);
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
	}

    public void camera_zoom(int amount){
        cam.zoom += ZOOM_DELTA * amount;
    }

    public void clear_flags(){
	    while(!flags.isEmpty()){
	        flags.remove(flags.size() - 1);
        }
    }

	@Override
	public void resize(int width, int height) {
		//cam.viewportWidth = WORLD_WIDTH;
		//cam.viewportHeight = WORLD_HEIGHT * height/width;
		//cam.update();
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

class Flag{
    private float x;
    private float y;
    public String country;
    public String code;
    public String name;
    public String extra;

    public Flag(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
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
