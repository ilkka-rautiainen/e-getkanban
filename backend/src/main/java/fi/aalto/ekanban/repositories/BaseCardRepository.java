package fi.aalto.ekanban.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import fi.aalto.ekanban.models.db.gameconfigurations.BaseCard;

public interface BaseCardRepository extends MongoRepository<BaseCard, String> {
    List<BaseCard> findAll();
}
