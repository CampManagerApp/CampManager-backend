package camp.CampManager.organisation.campaign.activities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.emptyList;

@Converter
public class StringStringConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (stringList == null) {
            return "";
        }
        AtomicReference<String> mystring = new AtomicReference<>("");
        stringList.forEach(e -> {
            mystring.set(mystring.get().concat(e).concat(SPLIT_CHAR));
        });
        String thestring = mystring.toString();
        if (thestring.length() == 0){
            return thestring;
        }
        return thestring.substring(0, thestring.length() - 1);
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if (string == null){
            return emptyList();
        }
        return new LinkedList<>(Arrays.asList(string.split(SPLIT_CHAR)));
    }
}
