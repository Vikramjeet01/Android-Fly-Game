package demoexam.indravadan.com.flygame;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    GameEngine flyGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        flyGame = new GameEngine(this, size.x, size.y);

        setContentView(flyGame);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Pause the game
        flyGame.pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();


        flyGame.startGame();
    }
}
