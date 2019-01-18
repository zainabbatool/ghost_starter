/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String COMPLETE_WORD = "This word is complete!";
    private static final String COMPUTER_VICTORY = "Computer Wins, complete word!";
    private static final String COMPUTER_VICTORY_2 = "Computer Wins, no word possible!";
    private static final String USER_VICTORY = "User Wins, complete word!";
    private static final String USER_VICTORY_2 = "User Wins, no word possible!";

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private boolean checkFirst=false
            ;

    private OnClickListener mCorkyListener= new OnClickListener(){
        public void onClick(View v){
            TextView label = (TextView) findViewById(R.id.gameStatus);
            TextView ourWord = (TextView) findViewById(R.id.ghostText);
            String ourText= (String) ourWord.getText();
            if(ourText.length()>=4 && dictionary.isWord(ourText)){
                label.setText(USER_VICTORY);
            }

            else{
                if(dictionary.getAnyWordStartingWith(ourText)==null){
                    //challenge method content (helper method)
                    label.setText(USER_VICTORY_2);

                }
                else{
                    String character= dictionary.getAnyWordStartingWith(ourText);
                    ourWord.setText(character);
                    label.setText(COMPUTER_VICTORY_2);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        //challenge
        Button button=(Button) findViewById(R.id.challenge_button);
        button.setOnClickListener(mCorkyListener);
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
         if (userTurn) {
            label.setText(USER_TURN);
            //checkFirst=false;
            //dictionary.checkFirstTurn();

        } else {
            label.setText(COMPUTER_TURN);
            //checkFirst=true;
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView ourWord = (TextView) findViewById(R.id.ghostText);
        String ourText= (String) ourWord.getText();

        if(ourText.length()>=4 && dictionary.isWord(ourText)){
            label.setText(COMPUTER_VICTORY);
        }

        else{
            if(dictionary.getAnyWordStartingWith(ourText)==null){
                //challenge method content (helper method)
                label.setText(COMPUTER_VICTORY_2);

            }
            else{
                String character= dictionary.getAnyWordStartingWith(ourText).substring(ourText.length(),ourText.length()+1);
                ourText=ourText+character;
                ourWord.setText(ourText);
                userTurn = true;
                label.setText(USER_TURN);
            }
        }

    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode<29 || keyCode>54){
            return super.onKeyUp(keyCode, event);
        }
        else{
            char pressedKey= (char) event.getUnicodeChar();
            TextView text = (TextView) findViewById(R.id.ghostText);
            String existingText= (String) text.getText();
            String updatedText= existingText + pressedKey;
            text.setText(updatedText);
            if(dictionary.isWord(updatedText)){
                TextView label = (TextView) findViewById(R.id.gameStatus);
                    label.setText(COMPLETE_WORD);
            }
            else{
                computerTurn();
            }
        }
        return true;
    }
}
