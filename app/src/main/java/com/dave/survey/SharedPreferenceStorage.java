package com.dave.survey;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.HashMap;

public class SharedPreferenceStorage {

    private Context context;
    private String myPreferences = "";
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;

    public SharedPreferenceStorage(Context context, String myPreferences) {
        this.context = context;
        this.myPreferences = myPreferences;
    }

    public void saveData(HashMap<String, String> hashMap, String keyName){

        sharedPreference = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();

        Gson gson = new Gson();
        String hashMapString = gson.toJson(hashMap);

        sharedPreference.edit().putString(keyName, hashMapString).apply();

    }

    public HashMap<String, String> getSavedData(String keyName){

        sharedPreference = context.getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String storedHashMapString = sharedPreference.getString(keyName, "No Data Found");

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();

        return gson.fromJson(storedHashMapString, type);

    }



}
