package camp.CampManager.organisation.campaign.tables;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;

@Converter
public class MapStringConverter implements AttributeConverter<Map<String, Set<String>>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Set<String>> setMap) {
        StringBuilder column = new StringBuilder();
        if (setMap == null) {
            return "";
        }
        for (String key : setMap.keySet()) {
            StringBuilder row = new StringBuilder(key + "|");
            for (String element : setMap.get(key)) {
                row.append(element).append(",");
            }
            try {
                String rowConverted = row.substring(0, row.length() - 1);
                column.append(rowConverted).append(";");
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        if (column.length() == 0) {
            return "";
        }
        return column.substring(0, column.length() - 1);
    }

    @Override
    public Map<String, Set<String>> convertToEntityAttribute(String s) {
        Map<String, Set<String>> returnValue = new HashMap<>();
        for (String row : s.split(";")) {
            if (row.contains("|")) {
                String key = row.split("\\|")[0];
                String values = row.split("\\|")[1];
                returnValue.put(key, new HashSet<>(Arrays.asList(values.split(","))));
            }
        }
        return returnValue;
    }
}
