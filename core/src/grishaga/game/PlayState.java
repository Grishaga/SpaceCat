package grishaga.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import sprites.Plane;
import sprites.Raccoon;
import sprites.ExplosiveAnimation;

public class PlayState extends State {
    ExplosiveAnimation explosiveAnimation;
    private Raccoon raccoon;
    private Plane plane;
    private Vector3 touchPos;
    private Texture backGround, smallCloud, meteor, bigCloud, duckImage, satellite, cloudSky;
    private long lastCloudTime, lastBigCloudTime, meteorTime, lastDuckTime, clouSkyTime, lastSputnikTime, lastPleneTime;
    private long timeCount, maxScore;
    public static long score;
    private Array<Rectangle> smallClouds, meteors, starsLeft, bigClouds, ducks, sputniks, cloudSkuy, planes;
    private Vector2 groundPos1, groundPos2;
    private Rectangle cloudSmall,cloudBig, border, borderPlane, borderRaccoon, borderCount, meteorit, duck, sputnik, skyCloud, bounds, airPlane;
    private  TextureRegion textureRegionSputnik;
    private  Music windMusic, aircraft, geeseMigration, satelliteSound;
    private int angleSatellite = 0;
    private float duration, meteorGrowCount;
    private  float explosiveDuration;
    private int bounsMeteorDetect, bounsDuckDetect, bounsPlaneDetect, bounsSputnikDetect;
    private int meteorTimer, planeTimer, sputnikTimer, duckTimer;
    private boolean planeSoundIn, duckSoundIn, satelliteSoundIn;
    private BitmapFont font;
    private Preferences prefs;
    private FreeTypeFontGenerator generator;

    public PlayState(GameStateManager gsm) {
            super(gsm);
        prefs = Gdx.app.getPreferences("My Preferences");
        maxScore = prefs.getLong("max_score", 0);
        explosiveAnimation = new ExplosiveAnimation();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 768, 1280);
        raccoon = new Raccoon(768/2, 200);
        backGround = new Texture("bg.png");
        windMusic = Gdx.audio.newMusic(Gdx.files.internal("wind.ogg"));
        aircraft = Gdx.audio.newMusic(Gdx.files.internal("plane.ogg"));
        geeseMigration = Gdx.audio.newMusic(Gdx.files.internal("geese_migration.ogg"));
        satelliteSound = Gdx.audio.newMusic(Gdx.files.internal("satellite.ogg"));
        windMusic.play();
        windMusic.setLooping(true);
        groundPos1 = new Vector2(0, camera.position.y - 1280);
        groundPos2 = new Vector2(0,(camera.position.y - 1280) + backGround.getHeight());
        touchPos = new Vector3();
        smallCloud = new Texture("small_cloud.png");
        cloudSky = new Texture("cloud_sky.png");
        bigCloud = new Texture("big_cloud.png");
        meteor = new Texture("meteor.png");
        duckImage = new Texture("duck.png");
        satellite = new Texture("satellite.png");
        smallClouds = new Array<Rectangle>();
        bigClouds = new Array<Rectangle>();
        meteors = new Array<Rectangle>();
        starsLeft = new Array<Rectangle>();
        ducks = new Array<Rectangle>();
        sputniks = new Array<Rectangle>();
        cloudSkuy = new Array<Rectangle>();
        planes = new Array<Rectangle>();
        textureRegionSputnik = new TextureRegion(satellite);
        border = new Rectangle();
        borderRaccoon = new Rectangle();
        borderPlane = new Rectangle();
        borderCount = new Rectangle();
        borderRaccoon.y = 1184;
        borderCount.y = 1270;
        border.y = 1280;
        borderPlane.y = -156;
        bounds = new Rectangle(raccoon.getPosition().x, raccoon.getPosition().y, 64, 64);
        plane = new Plane();
        MyGame.getadsController().hideBannerAd();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("corner_dark.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;
        font = new BitmapFont();
        font = generator.generateFont(parameter);
    }

    private void spawnPlane() {
        airPlane = new Rectangle();
        airPlane.x = MathUtils.random(192, 768 - 192);
        airPlane.width = 192;
        airPlane.height= 147;
        planes.add(airPlane);
       if (planeSoundIn)aircraft.play();
        lastPleneTime = TimeUtils.millis();
    }

    private void spawnSmallClouds(){
        cloudSmall = new Rectangle();
        cloudSmall.x = MathUtils.random(64, 768 - 64);
        cloudSmall.width = 64;
        cloudSmall.height = 64;
        smallClouds.add(cloudSmall);
        lastCloudTime = TimeUtils.millis();
    }

    private void spawnBigClouds() {
        cloudBig = new Rectangle();
        cloudBig.x = MathUtils.random(64, 768 - 64);
        cloudBig.width = 128;
        cloudBig.height = 128;
        bigClouds.add(cloudBig);
        lastBigCloudTime = TimeUtils.millis();
    }

    private void spawnMeteor(){
        meteorit = new Rectangle();
        meteorit.x = MathUtils.random(-660, 580);
        meteorit.width = 34;
        meteorit.height = 46;
        meteors.add(meteorit);
        meteorTime = TimeUtils.millis();
    }

    private void spawnDucks(){
        duck = new Rectangle();
        duck.x = MathUtils.random(760, 980);
        duck.width = 128;
        duck.height = 128;
        ducks.add(duck);
        if (duckSoundIn)geeseMigration.play();
        lastDuckTime = TimeUtils.millis();
    }

    private void spawnSatellite(){
        sputnik = new Rectangle();
        sputnik.x = MathUtils.random(64, 768-64);
        sputnik.width = 96;
        sputnik.height = 96;
        sputniks.add(sputnik);
        if (satelliteSoundIn)satelliteSound.play();
        lastSputnikTime = TimeUtils.millis();
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
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        sb.draw(backGround, groundPos1.x, groundPos1.y);
        sb.draw(backGround, groundPos2.x, groundPos2.y);

        timeCount +=1;

        for (Rectangle skyFly: cloudSkuy){
            sb.draw(cloudSky, skyFly.x, skyFly.y);
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
        if (Gdx.input.getAccelerometerX()>0){
            raccoon.getPosition().x -= (Gdx.input.getAccelerometerX()*2);}
        if (Gdx.input.getAccelerometerX()<0){
            raccoon.getPosition().x += (Math.abs(Gdx.input.getAccelerometerX()*2));}
        if (Gdx.input.getAccelerometerY()>0){
            raccoon.getPosition().y -= (Gdx.input.getAccelerometerY()*2);}
        if (Gdx.input.getAccelerometerY()<0){
            raccoon.getPosition().y += (Math.abs(Gdx.input.getAccelerometerY()*2));}

        for (Rectangle duckFly: ducks){
            sb.draw(duckImage, duckFly.x, duckFly.y);
        }
        Iterator<Rectangle> iterator2 = ducks.iterator();
        while (iterator2.hasNext()){
            duckSoundIn = true;
            Rectangle duckFly = iterator2.next();
            duckFly.y -= 50 * Gdx.graphics.getDeltaTime();
            duckFly.x -= 25 * Gdx.graphics.getDeltaTime();

            if (duckFly.overlaps(bounds)){
                bounsDuckDetect =1;
            }
            if (duckFly.y + 128 < 0){ iterator2.remove();
            }
        }
        if (bounsDuckDetect ==1) duckTimer +=1;
        if (bounsDuckDetect == 1){
            explosiveDuration += Gdx.graphics.getDeltaTime();
            explosiveAnimation.setCurrentFrame(explosiveDuration);
            sb.draw(explosiveAnimation.getCurrentFrame(), raccoon.getPosition().x, raccoon.getPosition().y);}
        if (duckTimer ==1)ExplosiveAnimation.explosive.play();
        if (duckTimer == 60){
            gsm.set(new GameOver(gsm));
            dispose();
        }

        plane.setCurrentFrame(duration);
        for (Rectangle planeFly: planes){
            sb.draw(plane.getCurrentFrame(), planeFly.x, planeFly.y);
        }
        Iterator<Rectangle> iterator5 = planes.iterator();
        while (iterator5.hasNext()){
            planeSoundIn =true;
            Rectangle planeFly = iterator5.next();
            planeFly.y += 100 * Gdx.graphics.getDeltaTime();
            if (planeFly.overlaps(bounds)){
                bounsPlaneDetect =1;
            }
            if (planeFly.y >borderPlane.y+1280+147){ iterator5.remove();
            }
        }
        if (bounsPlaneDetect ==1) planeTimer +=1;
        if (bounsPlaneDetect == 1){
            explosiveDuration += Gdx.graphics.getDeltaTime();
            explosiveAnimation.setCurrentFrame(explosiveDuration);
            sb.draw(explosiveAnimation.getCurrentFrame(), raccoon.getPosition().x, raccoon.getPosition().y);}
        if (planeTimer ==1)ExplosiveAnimation.explosive.play();
        if (planeTimer == 60){
            gsm.set(new GameOver(gsm));
            dispose();
        }

        meteorGrowCount += 0.02;
        for (Rectangle starFly: meteors){
            sb.draw(meteor, starFly.x, starFly.y);
        }
        Iterator<Rectangle> iterator = meteors.iterator();
        while (iterator.hasNext()){
            Rectangle meteorFly = iterator.next();
            meteorFly.y -= 300 * Gdx.graphics.getDeltaTime();
            meteorFly.x += 150 * Gdx.graphics.getDeltaTime();
            if (meteorFly.overlaps(bounds)) bounsMeteorDetect =1;
            if (meteorFly.y + 64 < 0)iterator.remove();
        }
        if (bounsMeteorDetect ==1) meteorTimer +=1;
        if (bounsMeteorDetect == 1){
            explosiveDuration += Gdx.graphics.getDeltaTime();
            explosiveAnimation.setCurrentFrame(explosiveDuration);
            sb.draw(explosiveAnimation.getCurrentFrame(), raccoon.getPosition().x, raccoon.getPosition().y);}
        if (meteorTimer ==1)ExplosiveAnimation.explosive.play();
        if (meteorTimer == 60){
            gsm.set(new GameOver(gsm));
            dispose();
        }

        for (Rectangle sputnikFly: sputniks){
            sb.draw(textureRegionSputnik, sputnikFly.x, sputnikFly.y, sputnik.width/2, sputnik.height/2, sputnik.width, sputnik.height, 1, 1, angleSatellite);
        }
        Iterator<Rectangle> iterator3 = sputniks.iterator();
        while (iterator3.hasNext()){
            satelliteSoundIn = true;
            Rectangle satelliteFly = iterator3.next();
            satelliteFly.y -= 150 * Gdx.graphics.getDeltaTime();
            if (satelliteFly.overlaps(bounds)){
                bounsSputnikDetect =1;
            }
            if (satelliteFly.y + 128 < 0){ iterator3.remove();
            }
        }
        if (bounsSputnikDetect ==1) sputnikTimer +=1;
        if (bounsSputnikDetect == 1){
            explosiveDuration += Gdx.graphics.getDeltaTime();
            explosiveAnimation.setCurrentFrame(explosiveDuration);
            sb.draw(explosiveAnimation.getCurrentFrame(), raccoon.getPosition().x, raccoon.getPosition().y);}
        if (sputnikTimer ==1)ExplosiveAnimation.explosive.play();
        if (sputnikTimer == 60){
            gsm.set(new GameOver(gsm));
            dispose();
        }

        for (Rectangle cloudFly: smallClouds){
            sb.draw(smallCloud, cloudFly.x, cloudFly.y);
        }
        Iterator<Rectangle> iterator6 = smallClouds.iterator();
        while (iterator6.hasNext()){
            Rectangle cloudFly = iterator6.next();
            cloudFly.y -= 100 * Gdx.graphics.getDeltaTime();
            if (cloudFly.y + 64 < 0){ iterator6.remove();
            }
        }

        for (Rectangle cloudFly: bigClouds){
            sb.draw(bigCloud, cloudFly.x, cloudFly.y);
        }
        Iterator<Rectangle> iterator1 = bigClouds.iterator();
        while (iterator1.hasNext()){
            Rectangle cloudFly = iterator1.next();
            cloudFly.y -= 50 * Gdx.graphics.getDeltaTime();
            if (cloudFly.y + 64 < 0){ iterator1.remove();
            }
        }

        font.draw(sb, "Score: "+timeCount, 512, borderCount.y);
            if (raccoon.getPosition().x < 0) raccoon.getPosition().x = 0;
            if (raccoon.getPosition().y < 0) raccoon.getPosition().y = 0;
            if (raccoon.getPosition().x > 768 - 96) raccoon.getPosition().x = 768 - 96;

        if (TimeUtils.millis() - lastCloudTime > 5000) spawnSmallClouds();
        if (TimeUtils.millis() - lastBigCloudTime > 15000) spawnBigClouds();
        int lastMeteorTime = 800;
        if (TimeUtils.millis() - meteorTime > lastMeteorTime -(int)meteorGrowCount) spawnMeteor();
        if (TimeUtils.millis() - lastDuckTime > 20000) spawnDucks();
        if (TimeUtils.millis() - lastSputnikTime > 100000) spawnSatellite();
        if (TimeUtils.millis() - clouSkyTime > 50000) spawnCloudSky();
        if (TimeUtils.millis() - lastPleneTime > 35000) spawnPlane();
        sb.end();

        cloudSmall.y = border.getY();
        sputnik.y = border.getY();
        cloudBig.y = border.getY();
        meteorit.y = border.getY();
        skyCloud.y = border.getY();
        duck.y = border.getY();
        airPlane.y = borderPlane.getY();
        }

    @Override
    public void update(float dt) {
        updateGround();
        camera.position.y += 15 * Gdx.graphics.getDeltaTime();
        border.y += 15 * Gdx.graphics.getDeltaTime();
        borderRaccoon.y += 15 * Gdx.graphics.getDeltaTime();
        borderPlane.y += 15 * Gdx.graphics.getDeltaTime();
        borderCount.y += 15*Gdx.graphics.getDeltaTime();

        if (raccoon.getPosition().y > borderRaccoon.getY()){
            raccoon.getPosition().y = borderRaccoon.getY();
        }
        if (raccoon.getPosition().y < borderRaccoon.getY()-1184){
            raccoon.getPosition().y = borderRaccoon.getY()-1184;
        }
        bounds.x = raccoon.getPosition().x;
        bounds.y = raccoon.getPosition().y;

        angleSatellite +=5;
        camera.update();
    }

    @Override
    public void dispose() {
        raccoon.dispose();
        explosiveAnimation.dispose();
        plane.dispose();
        backGround.dispose();
        smallCloud.dispose();
        bigCloud.dispose();
        meteor.dispose();
        duckImage.dispose();
        satellite.dispose();
        cloudSky.dispose();
        windMusic.dispose();
        aircraft.dispose();
        geeseMigration.dispose();
        satelliteSound.dispose();
        score = timeCount;
        if (score>maxScore)prefs.putLong("max_score", score);
        prefs.flush();
    }

    private void updateGround(){
        if (camera.position.y - (1280) > groundPos1.y + backGround.getHeight()/2)
            groundPos1.add(0, backGround.getHeight() * 2);
        if (camera.position.y - (1280) > groundPos2.y + backGround.getHeight()/2)
            groundPos2.add(0, backGround.getHeight() * 2);
    }
    @Override
    protected void handleInput() {}
    }

