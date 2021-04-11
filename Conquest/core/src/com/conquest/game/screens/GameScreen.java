package com.conquest.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.UBJsonReader;
import com.conquest.game.ConquestGame;
import com.conquest.game.Placeholders;
import com.conquest.game.logic.Player;
import com.conquest.game.logic.entities.Tile;
import com.conquest.game.logic.entities.Unite;

public class GameScreen implements Screen ,InputProcessor {
	
	final ConquestGame game;
	final static Vector3 origin =  new Vector3(0f, 0f, 0f);
	final static Vector3 YAxis =new Vector3(0f, 1f, 0f);
	final static Vector3 IYAxis =new Vector3(0f, -1f, 0f);
	final static Vector3 XAxis =new Vector3(1f, 0f, 0f);
	final static Vector3 ZAxis =new Vector3(0f, 0f, 1f);
	private static final float sensitivity = .2f;
	private Vector3 tempTranslator = new Vector3();
	public Model box;
	public Model triedre;
	public Model sapin;
	public Model dictateur;
	public Model ocean;
	public ModelBuilder primitiveBuilder;
	public 	ModelBuilder builder;
	public ModelBatch modelBatch;
	public SpriteBatch batch;
	public BitmapFont font;
	public ModelInstance triedreInstance;
	public ModelInstance sapinstance;
	public ModelInstance dictateuristance;
	public ArrayList<ModelInstance> oceanistances;
	public PerspectiveCamera camera;
	public Environment environment;
	//OrthographicCamera camera;
	public AssetManager assets;
	public ArrayList<ModelInstance> Tileinstances;
    
    /*******Logic related fields*******/
	public ArrayList<Tile> monde;
	public Player[] participants;
	public int joueurAutorise;
	public Unite unite;
	
	/********Shadow related fields*********/
	@SuppressWarnings("deprecation")
	DirectionalShadowLight shadowLight;
	ModelBatch shadowBatch;
	
	@SuppressWarnings("deprecation")
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
		monde = new ArrayList<Tile>();
		Tileinstances = new ArrayList<ModelInstance>();
		oceanistances = new ArrayList<ModelInstance>();
		
		new Placeholders();
		box = Placeholders.TILE_PLAINE;
		/***************On fabrique le triedre origine***************/
		builder.begin();
		builder.node("Y", primitiveBuilder.createArrow(origin, YAxis, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
					VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal));
		builder.node("X", primitiveBuilder.createArrow(origin, XAxis, new Material(ColorAttribute.createDiffuse(Color.RED)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal));
		builder.node("Z", primitiveBuilder.createArrow(origin, ZAxis, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal));
		triedre = builder.end();
		
		/************On charge les modeles***********/
        UBJsonReader jsonReader = new UBJsonReader();
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		sapin = modelLoader.loadModel(Gdx.files.getFileHandle("Sapin_type_I.g3db", FileType.Internal));
		dictateur = modelLoader.loadModel(Gdx.files.getFileHandle("Dictateur.g3db", FileType.Internal));
		ocean = modelLoader.loadModel(Gdx.files.getFileHandle("water.g3db", FileType.Internal));
		//ocean.materials.get(0).set(new Material(ColorAttribute.createDiffuse(Color.valueOf("#d4f1f9"))));
		sapinstance = new ModelInstance(sapin,4.5f,1,4.5f);
		dictateuristance = new ModelInstance(dictateur,4.5f,1,2.5f);
		sapinstance.transform.scl(2f);
		
		triedreInstance = new ModelInstance(triedre,0,0,0);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,.5f,.5f,.5f,1f));
        environment.add(new DirectionalLight().set(.5f, .5f, .5f, -1f, -0.8f, -0.2f));
        environment.add((shadowLight = new DirectionalShadowLight(2048, 2048, 20f, 20f, 1f, 100f)).set(0.8f, 0.8f, 0.8f, -1f, -.8f,
    			-.2f));
        
        /*********On fait les ombres ************/
		shadowBatch = new ModelBatch(new DepthShaderProvider());
		environment.shadowMap = shadowLight;
		
        
		/*************On fabrique une sorte de terrain****************/
		for(int i = 0; i<20;i++) {
			for(int j = 0; j<20;j++) {
				ModelInstance ref = new ModelInstance(box,i*2.1f,0,j*2.1f);
		        monde.add(new Tile(ref,i,j));
		        Tileinstances.add(ref);
				oceanistances.add(new ModelInstance(ocean,-40 + i*19.5f,.5f,-40 +j*19.5f));

			}
		}

		/*************On fabrique l'eau****************/
		/*Unite*/
		unite = new Unite(2,2);
		
        Gdx.input.setInputProcessor(this);        
        
	}

	@Override
	public void render(float delta) {
		Color color = Color.valueOf("#4287f5");
		Gdx.gl.glClearColor(color.r,color.g,color.b,color.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);	
		triedreInstance.transform.scl(0.5f*camera.position.dst(origin)/triedreInstance.transform.getScaleX());
		sapinstance.transform.rotate(YAxis, 10/60f);
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(YAxis);
		} else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(IYAxis);
		}

		camera.update();
		
		shadowLight.begin(Vector3.Zero,camera.direction);
		shadowBatch.begin(shadowLight.getCamera());
		shadowBatch.render(Tileinstances);
		shadowBatch.render(triedreInstance);
		shadowBatch.render(sapinstance);
		shadowBatch.render(dictateuristance);
		shadowBatch.render(oceanistances);
		shadowBatch.end();
		shadowLight.end();
		
		modelBatch.begin(camera);
		modelBatch.render(Tileinstances,environment);
		modelBatch.render(triedreInstance,environment);
		modelBatch.render(sapinstance,environment);
		modelBatch.render(dictateuristance,environment);
		modelBatch.render(oceanistances,environment);
		modelBatch.end();

		batch.begin();
		unite.update(this);
		font.draw(batch, "Camera: x= "+camera.position.x, 10, 150);
		font.draw(batch, "......: z= "+camera.position.z, 10, 140);
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps", 100, 50);
		batch.end();
				
	}
	
	public void interact(Tile tile) {
		
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
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		box.dispose();
		triedre.dispose();
		dictateur.dispose();
		sapin.dispose();
		ocean.dispose();
	}
}
