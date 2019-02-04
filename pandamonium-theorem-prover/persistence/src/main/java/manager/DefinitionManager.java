package manager;

import dao.DefinitionDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class DefinitionManager {
    final private DefinitionDao definitionDao;
}
