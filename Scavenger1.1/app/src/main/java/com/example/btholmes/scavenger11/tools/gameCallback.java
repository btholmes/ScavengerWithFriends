package com.example.btholmes.scavenger11.tools;

import com.example.btholmes.scavenger11.model.Game;

import java.util.ArrayList;

/**
 * Created by btholmes on 11/5/17.
 */

public interface gameCallback{
    void onSuccess(ArrayList<Game> gameList);
    void onError();
}