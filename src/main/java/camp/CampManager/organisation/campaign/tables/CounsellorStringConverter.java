package camp.CampManager.organisation.campaign.tables;

import camp.CampManager.organisation.campaign.counsellors.Counsellor;
import camp.CampManager.organisation.campaign.counsellors.CounsellorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.LinkedList;
import java.util.List;

@Converter
@Service
public class CounsellorStringConverter implements AttributeConverter<List<Counsellor>, String> {
    @Autowired
    private CounsellorRepository counsellorRepository;

    @Override
    public String convertToDatabaseColumn(List<Counsellor> counsellors) {
        System.out.println("STARTING COUNSELLOR CONVERTER");
        StringBuilder column = new StringBuilder();
        if (counsellors == null) {
            return "";
        }
        for (Counsellor counsellor : counsellors) {
            column.append(counsellor.getId()).append(";");
        }
        if (column.length() == 0) {
            return "";
        }
        System.out.println("COUNSELLOR CONVERTER FINISHED");
        return column.substring(0, column.length() - 1);
    }

    @Override
    public List<Counsellor> convertToEntityAttribute(String s) {
        System.out.println("STARTING COUNSELLOR CONVERTER");
        List<Counsellor> returnValue = new LinkedList<>();
        List<Long> values = new LinkedList<>();
        for (String v : s.split(";")) {
            try {
                values.add(Long.parseLong(v));
            } catch (NumberFormatException ignored) {
            }
        }
        for (Counsellor counsellor : counsellorRepository.findAllById(values)) {
            returnValue.add(counsellor);
        }
        System.out.println("COUNSELLOR CONVERTER DONE");
        return returnValue;
    }
}