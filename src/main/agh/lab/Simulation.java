package agh.lab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class Simulation {

    public static void main(String[] args) {
        try {
            Map<String, Object> parameters = JsonReader.readParameters();
            GUI gui = new GUI(parameters, true);
            GUI gui2 = new GUI(parameters, false);
            gui.startSimulation();
            gui2.startSimulation();

        } catch (IOException | IllegalArgumentException | ParseException ex) {
            if (ex instanceof IllegalArgumentException) {
                System.out.println(ex);
                System.exit(1);
            }
            if (ex instanceof FileNotFoundException) {
                System.out.println("Couldn't find parameters.json");
                System.exit(1);
            }
            if (ex instanceof ParseException) {
                System.out.println(ex);
                System.exit(1);
            }
        }
    }
}
