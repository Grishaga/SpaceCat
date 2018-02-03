package grishaga.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import sprites.Raccoon;

/**
 * Created by harbabaryhor on 04.01.16.
 */
public class MenuState extends State {
    private Texture background, clouds;
    private Texture playBtn, playBtnPressed, gpsBtn, gpsBtnPressed;
    BitmapFont font;
    long score, clouSkyTime;
    float density;
    FreeTypeFontGenerator generator;
    boolean downBtn, gpsButton;
    private int width;
    private int height;
    Rectangle skyCloud;
    Raccoon raccoon;
    float duration;
    Array<Rectangle>cloudSkuy;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        density = Gdx.graphics.getDensity();
        camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        background = new Texture("voda.jpg");
        clouds = new Texture("cloud_sky_white.png");

        playBtn = new Texture("play_button.png");
        playBtnPressed = new Texture("play_button_pressed.png");
        gpsBtn = new Texture("like_button.png");
        gpsBtnPressed = new Texture("like_button_pressed.png");
        MyGame.getadsController().showBannerAd();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("corner_dark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        font = new BitmapFont();
        font = generator.generateFont(parameter);

        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        score = prefs.getLong("max_score");
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();

        raccoon = new Raccoon(768/2-48, 1000);
        cloudSkuy = new Array<Rectangle>();
    }

    @Override
    public void handleInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (x >= width / 2 - 64 * density && x <= width / 2 + 64 * density && y >= height / 2 - 64 * density && y <= height / 2 + 64 * density)
                    downBtn = true;
                if (x >= width / 2 - 64 * density && x <= width / 2 + 64 * density && y >= height / 2 - 64 * density + ((height / 2) / 2) && y <= height / 2 + 64 * density + ((height / 2) / 2))
                    gpsButton = true;
                return true;
            }

            public boolean touchUp(int x, int y, int pointer, int button) {
                if (downBtn) {
                    dispose();
                    gsm.set(new PlayState(gsm));
                }
                if (gpsButton) {
                    MyGame.getMyGooglePlayCallback().startIntentGooglePlay();
                }
                downBtn = false;
                gpsButton = false;
                return true;
            }
        });
    }

    private void spawnCloudSky(){
        skyCloud = new Rectangle();
        skyCloud.y = 1280;
        skyCloud.width = 768;
        skyCloud.height = 1280;
        cloudSkuy.add(skyCloud);
        clouSkyTime = TimeUtils.millis();
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        sb.draw(background, 0, 0);
        for (Rectangle skyFly: cloudSkuy){
            sb.draw(clouds, skyFly.x, skyFly.y);
        }
        Iterator<Rectangle> iterator4 = cloudSkuy.iterator();
        while (iterator4.hasNext()){
            Rectangle skiFly = iterator4.next();
            skiFly.y -= 20 * Gdx.graphics.getDeltaTime();
            if (skiFly.y + 1280 < 0){ iterator4.remove();
            }
        }

        duration += Gdx.graphics.getDeltaTime();
        raccoon.setCurrentFrame(duration);
        raccoon.getCurrentFrame();
        sb.draw(raccoon.getRaccoon(), raccoon.getPosition().x, raccoon.getPosition().y);

        if (downBtn){
        sb.draw(playBtnPressed, 768/2-64, 1280/2-64, 128 , 128);
        }else {
            sb.draw(playBtn, 768/2-64, 1280/2-64, 128, 128);
        }
        if (gpsButton){
            sb.draw(gpsBtnPressed, 768/2-64, 1280/2-64-320, 128 , 128);
        }else {
            sb.draw(gpsBtn, 768/2-64, 1280/2-64-320, 128 , 128);
        }
        font.draw(sb, "Max score is: "+score, 224, 512);

        if (TimeUtils.millis() - clouSkyTime > 47000) spawnCloudSky();
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        playBtnPressed.dispose();
        Gdx.input.setInputProcessor(null);
        gpsBtn.dispose();
        gpsBtnPressed.dispose();
        raccoon.dispose();
        clouds.dispose();
    }

}
