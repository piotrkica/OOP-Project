package agh.lab;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

public class World {

    public static void main(String[] args){
        try {
            JSONParser parser = new JSONParser();
            String path = System.getProperty("user.dir") + "\\parameters.json";
            Object parameters = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) parameters;

            int width = ((Long) jsonObject.get("width")).intValue();
            int height = ((Long) jsonObject.get("height")).intValue();
            float jungleRatio = ((Double) jsonObject.get("jungleRatio")).floatValue();
            int startingAnimals = ((Long) jsonObject.get("startingAnimals")).intValue();
            int startingGrassTiles = ((Long) jsonObject.get("startingGrassTiles")).intValue();
            int startingEnergy = ((Long) jsonObject.get("startingEnergy")).intValue();
            int grassEnergyValue = ((Long) jsonObject.get("grassEnergyValue")).intValue();
            int moveEnergyCost = ((Long) jsonObject.get("moveEnergyCost")).intValue();

            IEngine engine = new SimulationEngine(width, height, jungleRatio, startingAnimals, startingGrassTiles, startingEnergy, grassEnergyValue, moveEnergyCost);
            engine.run();

        } catch ( IOException | ParseException | NullPointerException ex){
            if (ex instanceof FileNotFoundException) {
                System.out.println("File not found");
                System.exit(1);
            }
            if (ex instanceof NullPointerException) {
                System.out.println(ex);
                System.exit(1);
            }
            if (ex instanceof ParseException) {
                System.out.println(ex);
                System.exit(1);
            }
        }
    }
}
