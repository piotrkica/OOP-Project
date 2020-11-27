package agh.lab;

public class World {

    public static void main(String[] args) {
        try {
        MapWithJungle map = new MapWithJungle(5, 5 , 0.5, 5, 3);
        IEngine engine = new SimulationEngine(6, map);
        engine.run();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            System.exit(1);
        }
    }
}
