package agh.lab;

public class World {

    public static void main(String[] args) {
        try {
            String[] dirs = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
            MoveDirection[] directions = new OptionsParser().parse(dirs);
            MapWithJungle map = new MapWithJungle(15, 10 , 0.4, 2);
            Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 4)};
            IEngine engine = new SimulationEngine(directions, map, positions);
            engine.run();
            System.out.print(map);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }
}
