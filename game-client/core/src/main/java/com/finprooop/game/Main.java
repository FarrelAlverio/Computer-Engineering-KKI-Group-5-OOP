package com.finprooop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter {
    private FitViewport viewport;
    private ShapeRenderer shapes;
    private Map map;
    private Player player;

    @Override
    public void create() {
        viewport = new FitViewport(480, 270);
        shapes = new ShapeRenderer();
        map = new Map();
        player = new Player();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.20f, 1f);

        float delta = Math.min(com.badlogic.gdx.Gdx.graphics.getDeltaTime(), 0.033f);
        player.update(delta, map.getFloor());

        viewport.apply();
        shapes.setProjectionMatrix(viewport.getCamera().combined);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        map.render(shapes);
        player.render(shapes);
        shapes.end();
    }

    @Override
    public void dispose() {
        shapes.dispose();
    }
}
