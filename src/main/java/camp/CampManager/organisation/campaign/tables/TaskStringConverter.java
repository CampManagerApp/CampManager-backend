package camp.CampManager.organisation.campaign.tables;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.LinkedList;
import java.util.List;

@Converter
public class TaskStringConverter implements AttributeConverter<List<Task>, String> {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public String convertToDatabaseColumn(List<Task> tasks) {
        StringBuilder column = new StringBuilder();
        for (Task task : tasks) {
            column.append(task.getId()).append(";");
        }
        if (column.length() == 0) {
            return "";
        }
        return column.substring(0, column.length() - 1);
    }

    @Override
    public List<Task> convertToEntityAttribute(String s) {
        List<Task> returnValue = new LinkedList<>();
        List<Long> values = new LinkedList<>();
        for (String v : s.split(";")) {
            values.add(Long.parseLong(v));
        }
        for (Task task : taskRepository.findAllById(values)) {
            returnValue.add(task);
        }
        return returnValue;
    }
}
