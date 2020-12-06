package com.conquest.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.conquest.game.ConquestGame;
import com.conquest.game.Placeholders;
import com.conquest.game.logic.ConquestLogic;

public class GameScreen implements Screen ,InputProcessor {
	
	final ConquestGame game;
	final static Vector3 origin =  new Vector3(0f, 0f, 0f);
	final static Vector3 YAxis =new Vector3(0f, 1f, 0f);
	final static Vector3 IYAxis =new Vector3(0f, -1f, 0f);
	final static Vector3 XAxis =new Vector3(1f, 0f, 0f);
	final static Vector3 ZAxis =new Vector3(0f, 0f, 1f);
	private static final float sensitivity = .2f;
	private Vector3 tempTranslator = new Vector3();
	Model box;
	Model triedre;
	ModelBuilder primitiveBuilder;
	ModelBuilder builder;
    ModelBatch modelBatch;
	SpriteBatch batch;
	BitmapFont font;
	ArrayList<ModelInstance> terrain;
	ModelInstance triedreInstance;
	PerspectiveCamera camera;
    Environment environment;
	//OrthographicCamera camera;
    
    /*******Conquest related fields*******/
    ConquestLogic logic;
    
	public GameScreen(ConquestGame game) {
		camera = new PerspectiveCamera(24,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//camera = new OrthographicCamera();
		//camera.setToOrtho(true,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.set(50f, 50f, 50f);
		camera.lookAt(0f,0f,0f);
        camera.near =0.1f;
        camera.far = 300f;

		this.game = game;
		builder = new ModelBuilder();
		primitiveBuilder= new ModelBuilder();
		modelBatch = new ModelBatch();
		batch = new SpriteBatch();
		font = new BitmapFont();
		terrain = new ArrayList<ModelInstance>();
		new Placeholders();
		box = Placeholders.TILE_EAU;
		/***************On fabrique le triedre origine***************/
		builder.begin();
		builder.node("Y", primitiveBuilder.createArrow(origin, YAxis, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
					VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal));
		builder.node("X", primitiveBuilder.createArrow(origin, XAxis, new Material(ColorAttribute.createDiffuse(Color.RED)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal));
		builder.node("Z", primitiveBuilder.createArrow(origin, ZAxis, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal));
		triedre = builder.end();
		
		/*************On fabrique une sorte de terrain****************/
		for(int i = 0; i<60;i++) {
			for(int j = 0; j<60;j++) {
		        terrain.add(new ModelInstance(box,i*2.1f,0,j*2.1f));
			}
		}
		
        triedreInstance = new ModelInstance(triedre,0,0,0);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,.5f,.5f,.5f,1f));
        environment.add(new DirectionalLight().set(.5f, .5f, .5f, -1f, -0.8f, -0.2f));
        Gdx.input.setInputProcessor(this);
        
        /*****Conquest init******/
        logic = new ConquestLogic(null, null, 0);
        
        
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);	
		triedreInstance.transform.scl(0.5f*camera.position.dst(origin)/triedreInstance.transform.getScaleX());

		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(YAxis);
		} else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(IYAxis);
		}
		
		camera.update();
		
		modelBatch.begin(camera);
		modelBatch.render(terrain,environment);
		modelBatch.render(triedreInstance,environment);
		modelBatch.end();

		batch.begin();
		font.draw(batch, "Camera: x= "+camera.position.x, 10, 150);
		font.draw(batch, "......: z= "+camera.position.z, 10, 140);
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps", 100, 50);
		batch.end();
				
	}
	

	@Override
	public boolean keyDown(int keycode) {
		
        return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		tempTranslator.x = Gdx.input.getDeltaX()*sensitivity;
		tempTranslator.z = Gdx.input.getDeltaY()*sensitivity;
		camera.translate(tempTranslator);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		return false;
	}
	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		box.dispose();
	}
}
