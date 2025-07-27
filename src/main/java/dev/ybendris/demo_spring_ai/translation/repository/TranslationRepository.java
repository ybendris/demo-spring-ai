package dev.ybendris.demo_spring_ai.translation.repository;

import dev.ybendris.demo_spring_ai.translation.bo.Translation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends MongoRepository<Translation, Long> {
}
