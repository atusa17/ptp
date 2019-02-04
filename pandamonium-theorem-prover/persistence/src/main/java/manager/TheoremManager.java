package manager;

import dao.TheoremDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class TheoremManager {
    final private TheoremDao theoremDao;
}
