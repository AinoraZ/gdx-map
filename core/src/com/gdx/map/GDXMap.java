package com.gdx.map;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * The main application of the program. Contains the scene and all of the logic for the actors to be displayed.
 * <p>
 * Handles user interaction with the application
 * @author Ainoras Žukauskas
 * @version 2018-05-14
 */
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

    Json json = new Json();

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

        try{
            String content = new Scanner(new File("flags.json")).useDelimiter("\\Z").next();
            List<Flag> flag_info = json.fromJson(new ArrayList<Flag>().getClass(), content);
            for(Flag flag : flag_info){
                FlagActor flag_temp = new FlagActor(flag.getX(), flag.getY(), this, true);
                flag_temp.country = flag.getCountry();
                flag_temp.code = flag.getCode();
                flag_temp.name = flag.getName();
                flag_temp.extra = flag.getExtra();

                stage.addActor(flag_temp);
                actors.add(flag_temp);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

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

    /**
     * Gets the input provided by the FlagWindow instance and places that information in every FlagActor.
     * The FlagActors render themselves accordingly.
     */
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
                int x = MouseInfo.getPointerInfo().getLocation().x - (infoWindow.r.width/2);
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

	private void add_flag(){
        mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mouse_position);
        temp_flag = new FlagActor(mouse_position.x, mouse_position.y, this, false);
        stage.addActor(temp_flag);
        actors.add(temp_flag);
    }

    /**
     * Removes the FlagActor reference from the actor list
     * @param flag FlagActor instance to be removed from the actor list
     * @see FlagActor
     */
    public synchronized void remove_flag(FlagActor flag){
	    actors.remove(flag);
    }

    /**
     * Zooms in the camera by amount specified multiplied by zoom multiplier
     * @param amount
     */
    public void camera_zoom(int amount){
        cam.zoom += ZOOM_DELTA * amount;
    }

    /**
     * Removes all the flags from the actor list and the stage
     * <p>
     * Called from InfoWindow
     */
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
	    json = new Json();
	    List<Flag> flag_array = new ArrayList<Flag>();
	    for(FlagActor flag : actors){
	        Flag flag_info = new Flag(flag.getX(), flag.getY(), flag.country, flag.code, flag.name, flag.extra);
	        flag_array.add(flag_info);
        }
	    try {
            PrintWriter writer = new PrintWriter("flags.json");
            json.setOutputType(JsonWriter.OutputType.json);
            writer.print(json.prettyPrint(flag_array));
            writer.close();
        }
        catch (Exception e){
	        System.out.println(e.getMessage());
        }
		stage.dispose();
	}

	@Override
	public void pause() {

	}
}

class Flag{
    private float x;
    private float y;
    private String country = "";
    private String code = "";
    private String name = "";
    private String extra = "";

    public Flag(){

    }

    public Flag(float x, float y, String country, String code, String name, String extra){
        this.x = x;
        this.y = y;
        this.country = country;
        this.code = code;
        this.name = name;
        this.extra = extra;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public float getX() {

        return x;
    }

    public float getY() {
        return y;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getExtra() {
        return extra;
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
