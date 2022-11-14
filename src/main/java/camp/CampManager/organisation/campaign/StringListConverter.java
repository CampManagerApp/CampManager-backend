package camp.CampManager.organisation.campaign;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.emptyList;

@Converter
public class StringListConverter implements AttributeConverter<List<Long>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<Long> stringList) {
        if (stringList == null) {
            return "";
        }
        AtomicReference<String> mystring = new AtomicReference<>("");
        stringList.forEach(e -> {
            mystring.set(mystring.get().concat(e.toString()).concat(SPLIT_CHAR));
        });
        String thestring = mystring.toString();
        if (thestring.length() == 0){
            return thestring;
        }
        return thestring.substring(0, thestring.length() - 1);
    }

    @Override
    public List<Long> convertToEntityAttribute(String string) {
        if (string == null){
            return emptyList();
        }
        var thelist = Arrays.asList(string.split(SPLIT_CHAR));
        var thegoodlist = new LinkedList<Long>();
        thelist.forEach(e -> {
            try {
                thegoodlist.add(Long.parseLong(e));
            } catch (NumberFormatException ignored){

            }
        });
        return thegoodlist;
    }
}