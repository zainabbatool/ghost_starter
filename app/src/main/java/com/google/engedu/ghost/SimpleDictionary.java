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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private HashSet <String> wordSet = new HashSet<String>();
    private boolean checkFirst=false;


    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        int max=words.size()-1;
        int min=0;
        if (prefix.length()==0){
            Random randomno= new Random();
            int randomNum =randomno.nextInt(words.size());
            return words.get(randomNum);
        }

        else{//find the first word 'biggest' than prefix
            int average=0;
            boolean check=false;

            while(true){
                average=min+((max-min)/2);

                if(words.get(average).startsWith(prefix)){
                    return words.get(average);
                }
                else if(average==min){
                    return null;
                }
                else if(words.get(average).compareTo(prefix)<0){//average is smaller than prefix
                    min=average;
                }
                else{
                    max=average;
                }
            }
        }


    }

    @Override
    public String getGoodWordStartingWith(String prefix, boolean checkFirst) {
        String selected = null;
        int max=words.size()-1;
        int min=0;
        ArrayList <String> myWords= new ArrayList<String>();
        ArrayList <String> oddLength= new ArrayList<String>();
        ArrayList <String> evenLength= new ArrayList<String>();
        Random randomno= new Random();


        if (prefix.length()==0){
            int randomNum =randomno.nextInt(words.size());
            return words.get(randomNum);
        }

        else{//find the first word 'biggest' than prefix
            int average=0;
            boolean check=false;

            while(true){
                average=min+((max-min)/2);

                if(words.get(average).startsWith(prefix)){
                    for(int i=0; words.get(average+i).startsWith(prefix);i++){
                        myWords.add(words.get(average+i));
                    }
                    for(int i=average; words.get(i).startsWith(prefix);i--){
                        myWords.add(words.get(i));

                    }
                    break;
                }
                else if(average==min){//word doesn't exist
                    return null;
                }
                else if(words.get(average).compareTo(prefix)<0){//average is smaller than prefix
                    min=average;
                }
                else{
                    max=average;
                }
            }

            for(String i:myWords){
                if (i.length()%2==0){
                    evenLength.add(i);
                }

                else{
                    oddLength.add(i);
                }
            }
        }

        //if computer starts and checkFirst=true
        if(checkFirst==true){
            int randomNum2 =randomno.nextInt(evenLength.size());
            selected= evenLength.get(randomNum2);
        }
        else {
            //if user starts and checkFirst=false
            int randomNum3 =randomno.nextInt(oddLength.size());
            selected= oddLength.get(randomNum3);
        }


        return selected;
    }

    /*public void checkFirstTurn() {
        checkFirst=false;
    }*/

}
