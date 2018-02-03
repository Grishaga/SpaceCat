package grishaga.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends ApplicationAdapter {

	SpriteBatch batch;
	private GameStateManager gsm;
	public static final int WIDTH = 768;
	public static final int HEIGHT = 1280;
	private static AdsController adsController;
	private static MyGooglePlayCallback myGooglePlayCallback;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0, 0, 1, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	public  MyGame(){}

	public MyGame(AdsController adsController){
		this.adsController = adsController;
	}

	public static AdsController getadsController() {
		return adsController;
	}

	public interface MyGooglePlayCallback {
		void startIntentGooglePlay();
	}

	public void setMyGameCallback(MyGooglePlayCallback callback) {
		myGooglePlayCallback = callback;
	}

	public static MyGooglePlayCallback getMyGooglePlayCallback(){return myGooglePlayCallback;}
}
