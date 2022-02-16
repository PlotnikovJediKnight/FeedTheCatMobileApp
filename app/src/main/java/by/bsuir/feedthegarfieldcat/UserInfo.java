package by.bsuir.feedthegarfieldcat;

import android.provider.ContactsContract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class UserInfo {
    private String userName;
    private ArrayList<GameInfo> userGames;
    private ArrayList<Achievement> userAchievements;
    private long highScore;

    private long findHighestScore(){
        if (userGames.isEmpty()) return 0;

        long cScore = -1;
        for (GameInfo game : userGames){
            if (game.score > cScore){
                cScore = game.score;
            }
        }

        return cScore;
    }

    public UserInfo(String name, ArrayList<GameInfo> games, ArrayList<Achievement> achievements){
        userName = name;
        userGames = games;
        userAchievements = achievements;
        highScore = findHighestScore();
    }

    public ArrayList<Achievement> getUserAchievement() { return userAchievements; }

    public int getGameCount(){
        return userGames.size();
    }

    public GameInfo getGameAt(int index){
        return userGames.get(index);
    }

    public long getHighScore(){
        return highScore;
    }

    public void setHighScore(long newHighScore){
        highScore = newHighScore;
    }

    public int getAchievementCount(){
        return userAchievements.size();
    }

    public Achievement getAchivementAt(int index){
        return userAchievements.get(index);
    }

    public String getUserName(){
        return userName;
    }

    public void AddNewGame(GameInfo gameInfo){
        userGames.add(gameInfo);
    }

    public void AddNewAchievement(Achievement achievement){
        userAchievements.add(achievement);
    }

    public ArrayList<GameInfo> GetArrayListGameInfo(){
        return userGames;
    }

    public ArrayList<Achievement> GetArrayListAchievement(){
        return userAchievements;
    }

    static class GameInfo{
        @Override
        public String toString(){
            String[] twoComponents = Database.dateFormat.format(date).split(" ");

            return  twoComponents[0] + "\n" +
                    twoComponents[1] + "\n" +
                    String.valueOf(score);
        }

        public GameInfo(Date date, long score){
            this.date = date;
            this.score = score;
        }

        public Date getDate(){
            return date;
        }

        public long getScore(){
            return score;
        }

        Date date;
        long score;
    }

    static class Achievement{
        @Override
        public String toString(){
            return title + "\n" + String.valueOf(beatScore);
        }

        public Achievement(String title, long beatScore){
            this.title = title;
            this.beatScore = beatScore;
        }

        String title;
        long beatScore;
    }
}
