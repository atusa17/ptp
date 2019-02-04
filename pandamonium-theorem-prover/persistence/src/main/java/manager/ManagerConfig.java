package manager;

import dao.DefinitionDao;
import dao.ProofDao;
import dao.TheoremDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:development.properties")
public class ManagerConfig {

    @Bean
    public TheoremManager theoremManager() {
        return new TheoremManager(new TheoremDao());
    }

    @Bean
    public DefinitionManager definitionManager() {
        return new DefinitionManager(new DefinitionDao());
    }

    @Bean
    public ProofManager proofManager() {
        return new ProofManager(new ProofDao());
    }
}
