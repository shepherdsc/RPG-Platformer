package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class MainRPG extends Game {
	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 600;
	public static final float PPM = 100;

	public static final short CHARACTER_BIT = 1;
	public static final short HEAD_BIT = 2;
	public static final short BULLET_BIT = 4;
	public static final short OBJECT_BIT = 8;
	public static final short LEFT_BIT = 16;
	public static final short RIGHT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;

	public static SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
