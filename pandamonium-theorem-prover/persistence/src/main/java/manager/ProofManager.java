package manager;

import dao.ProofDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class ProofManager {
    final private ProofDao proofDao;
}
