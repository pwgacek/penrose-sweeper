package pl.edu.agh.tgk.penrosesweeper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FirstScreen implements Screen {
    private ShapeRenderer shapeRenderer;
    private ArrayList<Rhombus> rhombuses;
    private OrthographicCamera camera;

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        rhombuses = new ArrayList<>();
        camera = new OrthographicCamera();

        loadFigures();
    }


    private void loadFigures() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.internal("figures.txt").read()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] coordinates = line.split(",");
                if (coordinates.length == 8) {
                    Vector2 vA = new Vector2(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1]));
                    Vector2 vB = new Vector2(Float.parseFloat(coordinates[2]), Float.parseFloat(coordinates[3]));
                    Vector2 vC = new Vector2(Float.parseFloat(coordinates[4]), Float.parseFloat(coordinates[5]));
                    Vector2 vD = new Vector2(Float.parseFloat(coordinates[6]), Float.parseFloat(coordinates[7]));

                    rhombuses.add(new Rhombus(vA, vB, vC, vD));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float screenX = Gdx.input.getX();
            float screenY = Gdx.input.getY();
            Vector3 unprojected = camera.unproject(new Vector3(screenX, screenY, 0f));
            Vector2 worldCoords = new Vector2(unprojected.x, unprojected.y) ;
            for (Rhombus rhombus : rhombuses) {
                if (rhombus.isPointInside(worldCoords)) {
                    rhombus.click();
                }
            }
        }

        for (Rhombus rhombus : rhombuses) {
            rhombus.render(shapeRenderer);
        }
    }

    @Override
    public void resize(int width, int height) {
        float viewportHeight = 400;
        float aspectRatio = (float) width / height;
        float viewportWidth = viewportHeight * aspectRatio;
        camera.setToOrtho(false, viewportWidth, 400);
        camera.position.set(0, 0, 0);
        camera.update();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
