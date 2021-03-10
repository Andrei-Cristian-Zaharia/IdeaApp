package com.example.ideaapp;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Objects;

import Models.Idea;
import Models.UserModel;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.sync.SyncConfiguration;

public class Database {

    String AppId = "ideaapp-mautk";
    Realm uiThreadRealm;
    App app;

    Database(Context context) {
        Realm.init(context);
        app = new App(new AppConfiguration.Builder(AppId).build());

        Credentials credentials = Credentials.anonymous();
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                SyncConfiguration config = new SyncConfiguration.Builder(
                        Objects.requireNonNull(app.currentUser()), "IdeaApp").allowWritesOnUiThread(true).build();

                uiThreadRealm = Realm.getInstance(config);

                Log.v("EXAMPLE", "Successfully opened a realm at: " + uiThreadRealm.getPath());

/*
                InsertUser("Prajeala10000");
                InsertUser("Asar");
                InsertUser("Ciro");

                InsertIdea("Advanced Tour Guide as the name goes is an advance yet highly promising system helping a tourist or any user to get accurate " +
                        "and best data in no time. This System is an Android Application and uses Android Studio as its Front End and SQL Server as its Back End. " +
                        "This System works on 2 most important Entities i.e. Four Square and Weather Underground i.e. it uses Four Square API to get the places " +
                        "and Weather Underground API to get the weather details of a place. The Application acts as a Tour Guide giving out outputs to the user for " +
                        "every input given to the system. The System is highly reliable as it uses foursquare API which are very accurate and same goes for the weather " +
                        "conditions. This System tries the user to gives a heads-up giving the weather conditions to make sure that the user will be comfortable to visit" +
                        " the desired place. The User has options to select for the places he wants to visit for instance parks, beaches monuments or food joints and so" +
                        " on; the system will ask whether he is searching for the current locality or some other place. The System is very flexible in changing places and" +
                        " makes use of Google maps to display places if the user wishes to.", "Advanced Tour Guide Android");

                InsertIdea("The objective of this project is to implement a remote controlled " +
                        "robot using an Android application. This is an 8051 based robot with control interface being provided by the And" +
                        "roid application. The communication between the robot and the Android based smart phone is through Bluetooth. Hence, " +
                        "the robot is installed with a Bluetooth receiver which then communicates with the microcontroller." , "Remote Controlled Robot Using Android Application");

                InsertIdea("Personal Nutritionist as the name, the system can act as your personal nutritionist while this system can be used also by nutritionist gaining a lot of information and help in many ways. FatSecret API helps the System to get the information in many ways. The user can get details about a number of nutrients, vitamins etc of a fruit or vegetable. The user can add his recipes or get recopies using the API.\n" +
                        "The System basically helps the user in what to eat and which is good, what will help him and etc, the system will help him filter things easily.\n" +
                        "The System also allows the user to make a diet plan and remind him his food timings.", "Your Personal Nutritionist Using FatSecret API");

                List<UserModel> users = getAllUsers();

                for (UserModel user: users) {
                    Log.v("User:", user.getUsername());
                }

                List<Idea> ideas = getIdeasOf("IdeaApp");

                for (Idea idea: ideas) {
                    Log.v("Idea:", idea.get_nume());
                }
*/
            } else {
                // server disconnected
            }
        });
    }

    void InsertIdea(String description, String idea_name){
        uiThreadRealm.executeTransaction(r -> {
            Idea idea = new Idea();
            idea.set_nume(idea_name);
            idea.set_description(description);

            r.insertOrUpdate(idea);
        });
    }

    void InsertUser(String name){
        uiThreadRealm.executeTransaction(r -> {
            UserModel userModel = new UserModel();
            userModel.setUsername(name);

            r.insertOrUpdate(userModel);
        });
    }

    List<UserModel> getAllUsers(){
        RealmResults<UserModel> results = uiThreadRealm.where(UserModel.class).findAll();

        return results;
    }

    List<Idea> getAllIdeas(){
        RealmResults<Idea> results = uiThreadRealm.where(Idea.class).findAll();

        return results;
    }

    List<Idea> getIdeasOf(String user){
        RealmResults<Idea> results = uiThreadRealm.where(Idea.class).contains("username", user).findAll();

        return results;
    }
}
