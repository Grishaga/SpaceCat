package grishaga.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by harbabaryhor on 06.01.16.
 */
public class GameOver extends State{
    private Texture background;
    private Texture backBtn, backBtnPressed;
    BitmapFont font;
    FreeTypeFontGenerator generator;
    private int width;
    private int height;
    float density;
    boolean downBtn;

    public GameOver(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        background = new Texture("voda.jpg");
        backBtn = new Texture("back_button.png");
        backBtnPressed = new Texture("back_button_pressed.png");
        font = new BitmapFont();
        MyGame.getadsController().showBannerAd();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("corner_dark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        font = new BitmapFont();
        font = generator.generateFont(parameter);
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        density = Gdx.graphics.getDensity();
    }

    @Override
    public void handleInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (x >= width / 2 - 64 * density && x <= width / 2 + 64 * density && y >= height / 2 - 64 * density && y <= height / 2 + 64 * density)
                    downBtn = true;
                return true;
            }

            public boolean touchUp(int x, int y, int pointer, int button) {
                if (downBtn) {
                    dispose();
                    gsm.set(new MenuState(gsm));
                }
                downBtn = false;
                return true;
            }
        });
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
        if (downBtn){
            sb.draw(backBtnPressed, 768/2-64, 1280/2-64, 128 , 128);
        }else {
            sb.draw(backBtn, 768/2-64, 1280/2-64, 128, 128);
        }
        font.draw(sb, "Score is: "+PlayState.score, 256, 512);
        sb.end();

    }

    @Override
    public void dispose() {
        background.dispose();
        Gdx.input.setInputProcessor(null);
        backBtn.dispose();
        backBtnPressed.dispose();
    }

}
