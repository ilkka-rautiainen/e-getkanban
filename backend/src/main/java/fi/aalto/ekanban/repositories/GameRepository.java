package fi.aalto.ekanban.repositories;

import fi.aalto.ekanban.models.games.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findAll();
}
