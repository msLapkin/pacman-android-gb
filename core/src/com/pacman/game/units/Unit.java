package com.pacman.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pacman.game.GameMap;
import com.pacman.game.GameScreen;

import java.io.Serializable;


public abstract class Unit implements Serializable {
    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE;
    }

    final int HALF_SIZE = GameScreen.WORLD_CELL_PX / 2;
    final int SIZE = GameScreen.WORLD_CELL_PX;

    transient GameScreen gameScreen;
    transient TextureRegion[] textureRegions;
    Vector2 position;
    Vector2 destination;
    GameMap gameMap;
    float animationTimer;
    float secPerFrame;
    float speed;
    int rotation;
    boolean flipX;
    boolean flipY;
    Vector2 tmp;

    public Vector2 getPosition() {
        return position;
    }

    public int getCurrentFrame() {
        return (int) (animationTimer / secPerFrame);
    }

    public abstract void render(SpriteBatch batch);

    public abstract void loadResources(GameScreen gameScreen);

    public abstract void resetPosition();

    public abstract void restart(boolean full);

    public void update(float dt) {
        animationTimer += dt;
        if (animationTimer >= textureRegions.length * secPerFrame) {
            animationTimer = 0.0f;
        }
    }

    public void move(Direction direction, boolean isBot) {
        if (Vector2.dst(position.x, position.y, destination.x, destination.y) < 0.001f) {
            switch (direction) {
                case RIGHT:
                    if(position.x==gameMap.getCellX()-1){
                        position.x=0;
                        destination.set(0,position.y);
                    } else
                    if (gameMap.isCellEmpty((int) position.x + 1, (int) position.y)) {
                        destination.set(position.x + 1, position.y);
                        rotation = 0;
                        flipX = false;
                        flipY = false;
                    }
                    break;
                case LEFT:
                    if(position.x==0){
                        position.x=gameMap.getCellX()-1;
                        destination.set(position.x,position.y);
                    } else
                    if (gameMap.isCellEmpty((int) position.x - 1, (int) position.y)) {
                        destination.set(position.x - 1, position.y);
                        rotation = 0;
                        flipX = true;
                        flipY = false;
                    }
                    break;
                case UP:
                    if (gameMap.isCellEmpty((int) position.x, (int) position.y + 1)) {
                        destination.set(position.x, position.y + 1);
                        if (!isBot) {
                            rotation = 90;
                            flipX = false;
                            flipY = false;
                        }
                    }
                    break;
                case DOWN:
                    if (gameMap.isCellEmpty((int) position.x, (int) position.y - 1)) {
                        destination.set(position.x, position.y - 1);
                        if (!isBot) {
                            rotation = 270;
                            flipX = false;
                            flipY = true;
                        }
                    }
                    break;
            }
        }
    }
}
