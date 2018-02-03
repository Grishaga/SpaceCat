package grishaga.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import grishaga.game.AdsController;
import grishaga.game.MyGame;

public class DesktopLauncher  implements AdsController {
	private static DesktopLauncher application;
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 768;
		config.height = 1280;
		if (application == null) {
			application = new DesktopLauncher();
		}

		new LwjglApplication(new MyGame(application), config);
	}

	@Override
	public void showBannerAd() {

	}

	@Override
	public void hideBannerAd() {

	}
}
