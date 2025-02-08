package fr.it.exalt.infrastructure.repository;

import fr.it.exalt.infrastructure.entity.JackpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JackpotRepository extends JpaRepository<JackpotEntity, UUID> {

    boolean existsByEmail(String email);
}
