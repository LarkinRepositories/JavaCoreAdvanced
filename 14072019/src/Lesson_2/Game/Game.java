package Lesson_2.Game;
import Lesson_2.Game.Heroes.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    public static void main(String[] args) {

        //Random randomStep = new Random();
        //Random randomHealing = new Random();
        //int round = 3;

        Hero[] team1 = new Hero[]{new Warrior(200, "Воин1", 700)
                , new Assasin(150, "Ассасин1", 70)
                , new Doctor(120, "Доктор1", 60)};


        Hero[] team2 = new Hero[]{new Warrior(250, "Воин2", 60)
                , new Assasin(160, "Ассасин2", 90)
                , new Doctor(110, "Доктор2", 80)};


        teamFight(team1, team2);

    }

    private static void teamFight(Hero[] team1, Hero[] team2) {
        ArrayList<Hero> teamOne = new ArrayList<>(Arrays.asList(team1));
        ArrayList<Hero> teamTwo = new ArrayList<>(Arrays.asList(team2));

        while (teamOne.size() > 0 && teamTwo.size() > 0) {
            fight(teamOne, teamTwo);
            fight(teamTwo, teamOne);
        }
        System.out.println("---------------");

        for (Hero t1 : teamOne) {
            t1.info();
        }

        for (Hero t2 : teamTwo) {
            t2.info();
        }
    }
        private static void fight(ArrayList<Hero> teamOne, ArrayList<Hero> teamTwo) {
            int x = (int) (Math.random() * teamOne.size());
            int y = (int) (Math.random() * teamTwo.size());
            if (x >= teamOne.size() || y >= teamTwo.size()) {
            } else {
                if (teamOne.get(x) instanceof Doctor) { //TODO: добавить проверку на полное здоровье в healing();
                    teamOne.get(x).healing(teamOne.get((int) (Math.random() * teamOne.size() - 1)));
                } else {
                    teamOne.get(x).hit(teamTwo.get(y));
                    if (teamTwo.get(y).getHealth() <= 0) {
                        System.out.println(teamTwo.get(y).getName() + " мертв!");
                        teamTwo.remove(teamTwo.get(y));
                        System.out.println("Теперь игроков в команде: " + teamTwo.size());
                    }
                }
            }

        }
    }
