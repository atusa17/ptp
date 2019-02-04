package edu.msudenver.tsp.persistence.manager;

import edu.msudenver.tsp.persistence.dao.DefinitionDao;
import edu.msudenver.tsp.persistence.dao.NotationDao;
import edu.msudenver.tsp.persistence.dao.ProofDao;
import edu.msudenver.tsp.persistence.dao.TheoremDao;
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

    @Bean
    public NotationManager notationManager() {
        return new NotationManager(new NotationDao());
    }
}
