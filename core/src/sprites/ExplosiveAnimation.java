package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by harbabaryhor on 06.01.16.
 */
public class ExplosiveAnimation {
    public static Sound explosive;
    private static final int FRAME_COLS = 9;
    private static final int FRAME_ROWS = 9;
    com.badlogic.gdx.graphics.g2d.Animation explosiveAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    int index;

    public ExplosiveAnimation(){
        explosive = Gdx.audio.newSound(Gdx.files.internal("blast.ogg"));
        walkSheet = new Texture("explosive_sprite.png");
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        explosiveAnimation = new com.badlogic.gdx.graphics.g2d.Animation(0.012345f, walkFrames);
    }

    public void setCurrentFrame(float keyFrame) {
        currentFrame = explosiveAnimation.getKeyFrame(keyFrame);
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public void dispose() {
      explosive.dispose();
        walkSheet.dispose();
    }
}
