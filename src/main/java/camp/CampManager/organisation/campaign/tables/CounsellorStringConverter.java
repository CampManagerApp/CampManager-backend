package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.LinkedList;
import java.util.List;

@Converter
public class CounsellorStringConverter implements AttributeConverter<List<Counsellor>, String> {
    @Autowired
    private CounsellorRepository counsellorRepository;

    @Override
    public String convertToDatabaseColumn(List<Counsellor> counsellors) {
        StringBuilder column = new StringBuilder();
        for (Counsellor counsellor : counsellors) {
            column.append(counsellor.getId()).append(";");
        }
        if (column.length() == 0) {
            return "";
        }
        return column.substring(0, column.length() - 1);
    }

    @Override
    public List<Counsellor> convertToEntityAttribute(String s) {
        List<Counsellor> returnValue = new LinkedList<>();
        List<Long> values = new LinkedList<>();
        for (String v : s.split(";")) {
            values.add(Long.parseLong(v));
        }
        for (Counsellor counsellor : counsellorRepository.findAllById(values)) {
            returnValue.add(counsellor);
        }
        return returnValue;
    }
}