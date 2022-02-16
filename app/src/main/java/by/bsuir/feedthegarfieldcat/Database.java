package by.bsuir.feedthegarfieldcat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Database {
    private static Database instance = null;
    private static final String databaseFilePath = "/data/data/by.bsuir.feedthegarfieldcat/files/database.txt";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    private void createFileIfNecessary(){
        File file = new File(databaseFilePath);
        FileOutputStream fos = null;

        if(file == null || !file.exists()) {
            try {
                fos = new FileOutputStream(databaseFilePath);
                fos.write("0".getBytes(StandardCharsets.UTF_8));
                System.err.println("CREATED DATABASE");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initAchievements(){
        achievements = new ArrayList();
        achievements.add(new UserInfo.Achievement("Новичок", 100));
        achievements.add(new UserInfo.Achievement("Опытный", 500));
        achievements.add(new UserInfo.Achievement("Подмастерье", 1000));
        achievements.add(new UserInfo.Achievement("Стажёр", 1500));
        achievements.add(new UserInfo.Achievement("Бывалый", 2000));
        achievements.add(new UserInfo.Achievement("Воин", 2500));
        achievements.add(new UserInfo.Achievement("Сержант", 3000));
        achievements.add(new UserInfo.Achievement("Генералиссимус", 5000));
        achievements.add(new UserInfo.Achievement("Лидер", 10000));
        achievements.add(new UserInfo.Achievement("Безумец", 100000));
    }

    private HashMap<String, UserInfo> idToUser;
    private Database() throws Exception {
        initAchievements();
        createFileIfNecessary();
        idToUser = new HashMap<>();

        Scanner in = new Scanner(new File(databaseFilePath));
        ArrayList<UserInfo.GameInfo> games = new ArrayList<>();
        ArrayList<UserInfo.Achievement > achievements = new ArrayList<>();

        int N = in.nextInt();
        for (int i = 0; i < N; ++i){
            in.nextLine();
            String id = in.nextLine();
            //Log.e("FUCK", id);
            String name = in.nextLine();
            //Log.e("FUCK", name);
            int gameCount = in.nextInt();
            //Log.e("FUCK", String.valueOf(gameCount));

            for (int j = 0; j < gameCount; ++j){
                in.nextLine();
                String ddMMyyy = in.nextLine();
                String hhmmss = in.nextLine();
                long score = in.nextLong();
                games.add(new UserInfo.GameInfo(dateFormat.parse(ddMMyyy + " " + hhmmss), score));
            }

            int achievementCount = in.nextInt();
            for (int j = 0; j < achievementCount; ++j){
                in.nextLine();
                String title = in.nextLine();
                long beatScore = in.nextLong();
                achievements.add(new UserInfo.Achievement(title, beatScore));
            }

            idToUser.put(id, new UserInfo(name, games, achievements));
            games = new ArrayList<>();
            achievements = new ArrayList<>();
        }
    }

    public static Database Instance()  {
        if (instance == null){
            try {
                instance = new Database();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public UserInfo getUser(String id){
        if (idToUser.containsKey(id))
            return idToUser.get(id);
        else
            return null;
    }

    public UserInfo putNewUser(String id, UserInfo user){
        idToUser.put(id, user);
        return user;
    }

    public void saveDatabaseToFile(){
        try {
            PrintStream printStream = new PrintStream(databaseFilePath);
            printStream.println(idToUser.size());

            for (Map.Entry<String, UserInfo> pair : idToUser.entrySet()){
                printStream.println(pair.getKey());
                UserInfo user = pair.getValue();

                printStream.println(user.getUserName());

                int gameCount = user.getGameCount();
                printStream.println(gameCount);

                for (int i = 0; i < gameCount; ++i){
                    printStream.println(user.getGameAt(i));
                }

                int achievementCount = user.getAchievementCount();
                printStream.println(achievementCount);

                for (int i = 0; i < achievementCount; ++i){
                    printStream.println(user.getAchivementAt(i));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<UserInfo.Achievement> achievements;
}
