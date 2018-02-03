package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by harbabaryhor on 17.02.16.
 */
public class Raccoon {
    private Vector3 position;
    Rectangle raccoon;
    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 5;
    Animation explosiveAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    int index;

    public Raccoon(int x, int y) {
        position = new Vector3(x, y, 0);
        raccoon = new Rectangle(x, y, 64, 64);

        walkSheet = new Texture("raccoon.png");
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        explosiveAnimation = new Animation(0.033f, walkFrames);
    }

    public void setCurrentFrame(float keyFrame) {
        currentFrame = explosiveAnimation.getKeyFrame(keyFrame, true);
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public TextureRegion getRaccoon() {
        return currentFrame;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void dispose() {
        walkSheet.dispose();
    }
}
