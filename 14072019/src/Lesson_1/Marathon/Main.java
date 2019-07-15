package Lesson_1.Marathon;

import Lesson_1.Marathon.Competitors.Cat;
import Lesson_1.Marathon.Competitors.Dog;
import Lesson_1.Marathon.Competitors.Human;
import Lesson_1.Marathon.Competitors.Team;
import Lesson_1.Marathon.Obstacles.Course;
import Lesson_1.Marathon.Obstacles.Cross;
import Lesson_1.Marathon.Obstacles.Wall;
import Lesson_1.Marathon.Obstacles.Water;

public class Main {
    public static void main(String[] args) {
        Course course = new Course(new Cross(80), new Wall(2), new Water(50), new Cross(120));
        Team teamA = new Team("Команда А", new Human("Боб"), new Cat("Барсик"), new Dog("Бобик"), new Human("Джек"));
        course.doIt(teamA);
        teamA.showResults();
    }
}