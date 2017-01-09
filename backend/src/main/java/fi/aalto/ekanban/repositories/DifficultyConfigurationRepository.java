package fi.aalto.ekanban.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import fi.aalto.ekanban.models.db.gameconfigurations.DifficultyConfiguration;

public interface DifficultyConfigurationRepository extends MongoRepository<DifficultyConfiguration, String> {

}
