package com.sywhofloats.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	
	public AssetMusic music;
	public AssetSounds sounds;	
	public AssetFonts fonts;
	
	private Assets () {}

	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);

		// load music
		assetManager.load(Constants.MUSIC_PATH,Music.class);
		
		// load sounds
		assetManager.load(Constants.ROTATE_PATH,Sound.class);
		assetManager.load(Constants.BLOCK_LAND_PATH,Sound.class);	
		assetManager.load(Constants.LINES1_PATH,Sound.class);
		assetManager.load(Constants.LINES2_PATH,Sound.class);
		assetManager.load(Constants.LINES3_PATH,Sound.class);
		assetManager.load(Constants.LINES4_PATH,Sound.class);
		assetManager.load(Constants.LINE_SIGNAL_PATH,Sound.class);
		assetManager.load(Constants.LINE_SIGNAL_PATH4,Sound.class);
		assetManager.load(Constants.LOST_PATH,Sound.class);	
		
		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: "
				+ assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		
		// create game resource objects
		music = new AssetMusic(assetManager);
		fonts = new AssetFonts();
		sounds = new AssetSounds(assetManager);
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
		fonts.font.dispose();
	}

	@Override
	public void error (String filename, @SuppressWarnings("rawtypes") Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}

	public class AssetMusic {
		public final Music themeSong;
		public AssetMusic (AssetManager am) {
			themeSong = am.get(Constants.MUSIC_PATH,Music.class);
		}
	}
	
	public class AssetSounds {
		public final Sound rotate;
		public final Sound blockLand;
		public final Sound lines1;
		public final Sound lines2;
		public final Sound lines3;
		public final Sound lines4;
		public final Sound lineSignal;
		public final Sound lineSignal4;
		public final Sound lost;	
		
		public AssetSounds (AssetManager am) {
			rotate = am.get(Constants.ROTATE_PATH,Sound.class);
			blockLand = am.get(Constants.BLOCK_LAND_PATH,Sound.class);
			lines1 = am.get(Constants.LINES1_PATH,Sound.class);
			lines2 = am.get(Constants.LINES2_PATH,Sound.class);
			lines3 = am.get(Constants.LINES3_PATH,Sound.class);
			lines4 = am.get(Constants.LINES4_PATH,Sound.class);
			lineSignal = am.get(Constants.LINE_SIGNAL_PATH,Sound.class);
			lineSignal4 = am.get(Constants.LINE_SIGNAL_PATH4,Sound.class);
			lost = am.get(Constants.LOST_PATH,Sound.class);
		}
	}
	
	public class AssetFonts {
		public final BitmapFont font;
		
		public AssetFonts (){
			font = new BitmapFont(Gdx.files.internal(Constants.FONT_PATH),false);
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			font.scale(0.002f);
		}
		
	}
}
