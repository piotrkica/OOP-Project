package agh.lab;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonReader {
    public static Map<String, Object> readParameters() throws IOException, ParseException, IllegalArgumentException {
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

        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Map width or height less than 1");
        }
        if (jungleRatio > 1 || jungleRatio < 0) {
            throw new IllegalArgumentException("Jungle ratio not in [0-1]");
        }

        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("width", width);
        parametersMap.put("height", height);
        parametersMap.put("jungleRatio", jungleRatio);
        parametersMap.put("startingAnimals", startingAnimals);
        parametersMap.put("startingGrassTiles", startingGrassTiles);
        parametersMap.put("startingEnergy", startingEnergy);
        parametersMap.put("grassEnergyValue", grassEnergyValue);
        parametersMap.put("moveEnergyCost", moveEnergyCost);

        return parametersMap;
    }
}
