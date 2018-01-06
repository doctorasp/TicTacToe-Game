package com.example.deepbrain.anagramgame;

import java.util.Random;

/**
 * Created by DeepBrain on 1/4/2018.
 */

public class Anagram {

    public static final Random RANDOM = new Random();
    public static final String[] WORDS = {"КАВАЛЕРИСТ", "МАШИНКА",
            "БЕЙСБОЛ", "МАТЕРИК", "РАКЕТА", "ПЛОМБА", "ЖЕЛАТЬ", "НАТУРА", "ПРИКАЗ", "ГРАНАТ",
            "КОМАР", "АКТЕР", "РУЧКА", "ГОЛОД", "ШУТКА", "КРОНА", "МАРКА",
            "ДОМЕН", "СТЕНА", "ХАЛВА", "ЛИРА", "КАРА", "ОДИН", "СОЛЬ", "СЕРП"};
    public static int currentIndex = 0;

    public static String randomWord() {
        return WORDS[RANDOM.nextInt(WORDS.length)];
    }
    public static String getNext(){
        if(currentIndex<WORDS.length){
            return WORDS[currentIndex++];
        }
        else
        {
            return "end";
        }
    }

    public static String shuffleWord(String word) {
        if (word != null  &&  !"".equals(word)) {
            char a[] = word.toCharArray();

            for (int i = 0; i < a.length; i++) {
                int j = RANDOM.nextInt(a.length);
                char tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }

            return new String(a);
        }

        return word;
    }
}