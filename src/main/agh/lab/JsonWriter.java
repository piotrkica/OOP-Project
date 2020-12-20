package agh.lab;

import com.google.common.io.Files;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;

public class JsonWriter {
    public static void writeJson(StatsMaker stats, String filename) {
        JSONObject statsObject = new JSONObject();
        statsObject.put("finalDay", stats.getDay());
        statsObject.put("avgAliveAnimalsCount", stats.getAvgAliveAnimalsCountOverTime());
        statsObject.put("avgGrassTilesCount", stats.getAvgGrassTilesCountOverTime());
        statsObject.put("avgDominantGenotype", stats.getDominantGeneOverTime());
        statsObject.put("avgAnimalEnergy", stats.getAvgEnergyCountOverTime());
        statsObject.put("avgDeadAnimalsLifeSpan", stats.getAvgDeadAnimalsLifeSpanOverTime());
        statsObject.put("avgChildrenCount", stats.getAvgChildrenCountOverTime());

        String path = System.getProperty("user.dir") + "\\" + filename + ".json";
        try {
            Files.write(statsObject.toJSONString().getBytes(), new File(path));
        } catch (IOException e) {
            System.exit(-1);
        }
    }
}
