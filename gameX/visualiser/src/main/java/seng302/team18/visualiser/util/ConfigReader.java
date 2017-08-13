package seng302.team18.visualiser.util;

import java.io.InputStream;
import java.util.*;

/**
 * Created by dhl25 on 12/08/17.
 */
public class ConfigReader {

    private final List<String> tokens;

    public ConfigReader(List<String> tokens) {
        this.tokens = tokens;
    }


    public Map<String, String> parseConfig(InputStream configs) {
        Scanner scanner = new Scanner(configs);
        Map<String, String> values = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            for (String token : tokens) {
                if (line.startsWith(token)) {
                    String[] tokenValue = line.split("=");
                    values.put(tokenValue[0], tokenValue[1]);
                }
            }
        }
        return values;
    }

    public List<String> getTokens() {
        return tokens;
    }
}
