package camp.CampManager.organisation.campaign.tables.restrictions;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.LinkedList;
import java.util.List;

@Converter
public class RestrictionStringConverter implements AttributeConverter<List<Restriction>, String> {
    @Autowired
    private RestrictionRepository restrictionRepository;

    @Override
    public String convertToDatabaseColumn(List<Restriction> restrictions) {
        StringBuilder column = new StringBuilder();
        for (Restriction restriction : restrictions) {
            column.append(restriction.getId()).append(";");
        }
        if (column.length() == 0) {
            return "";
        }
        return column.substring(0, column.length() - 1);
    }

    @Override
    public List<Restriction> convertToEntityAttribute(String s) {
        List<Restriction> returnValue = new LinkedList<>();
        List<Long> values = new LinkedList<>();
        for (String v : s.split(";")) {
            values.add(Long.parseLong(v));
        }
        for (Restriction restriction : restrictionRepository.findAllById(values)) {
            returnValue.add(restriction);
        }
        return returnValue;
    }
}
