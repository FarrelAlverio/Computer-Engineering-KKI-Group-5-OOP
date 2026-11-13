
package com.CalistaAqilaRamadhani.backend.repository;

import com.CalistaAqilaRamadhani.backend.model.Score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    List<Score> findByPointGreaterThan(Integer minValue);

    List<Score> findAllByOrderByCreatedAtDesc();

    @Query("SELECT s FROM Score s ORDER BY s.point DESC")
    List<Score> findTopScores(Pageable pageable);

    default List<Score> findTopScores(Integer limit) {
        return findTopScores(
            PageRequest.of(0, Math.max(1, limit == null ? 10 : limit))
        );
    }
}
