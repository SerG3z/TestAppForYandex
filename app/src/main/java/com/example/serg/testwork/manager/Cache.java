package com.example.serg.testwork.manager;

import android.content.Context;

import com.example.serg.testwork.models.Artist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by serg3z on 24.04.16.
 */
public class Cache {

    private static final String nameFile = "/Data.dat";

    public static void writeToCache(ArrayList<Artist> artists, Context context) {

        try {
            File file = new File(context.getExternalCacheDir() + nameFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeInt(artists.size());
            for (Artist artist : artists) {
                objectOutputStream.writeObject(artist);
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Artist> readFromCache(Context context) {

        try {
            FileInputStream inputStream = new FileInputStream(context.getExternalCacheDir() + nameFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            int count = objectInputStream.readInt();
            ArrayList<Artist> artists = new ArrayList<>();
            for (int c = 0; c < count; c++) {
                artists.add((Artist) objectInputStream.readObject());
            }
            objectInputStream.close();
            return artists;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
