package camp.CampManager.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Getter
@Setter
@NoArgsConstructor
@PropertySource("classpath:application.properties")
public class ConfigProperties {

    @Autowired
    private Environment env;

    public String getConfigValue(String configKey){
        return env.getProperty(configKey);
    }
}
