package manager;

import dao.NotationDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class NotationManager {
    final private NotationDao notationDao;
}
