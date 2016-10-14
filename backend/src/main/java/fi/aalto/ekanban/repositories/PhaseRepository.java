package fi.aalto.ekanban.repositories;

import fi.aalto.ekanban.models.db.gameconfigurations.Phase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhaseRepository extends MongoRepository<Phase, String> {

    Phase findByName(String name);

}
