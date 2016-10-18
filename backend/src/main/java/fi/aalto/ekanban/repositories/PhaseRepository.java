package fi.aalto.ekanban.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import fi.aalto.ekanban.models.db.phases.Phase;

public interface PhaseRepository extends MongoRepository<Phase, String> {

    Phase findByName(String name);

}
