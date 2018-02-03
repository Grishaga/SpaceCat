package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by harbabaryhor on 27.02.16.
 */
public class Plane {
    private static final int FRAME_COLS = 9;
    private static final int FRAME_ROWS = 9;
    Animation planeAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    int index;

    public Plane() {
        walkSheet = new Texture("plane_sprite.png");
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        planeAnimation = new com.badlogic.gdx.graphics.g2d.Animation(0.012345f, walkFrames);
    }

    public void setCurrentFrame(float keyFrame) {
        currentFrame = planeAnimation.getKeyFrame(keyFrame, true);
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public void dispose() {
        walkSheet.dispose();
    }
}
