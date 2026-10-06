package com.diego.escuela.repositories;

import com.diego.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    boolean existsByMaestroId(Long maestroId);

    boolean existsByCursoId(Long cursoId);

    boolean existsByAulaId(Long aulaId);

    boolean existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(
            Long cursoId,
            Long maestroId,
            Long aulaId,
            String periodo
    );

    boolean existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
            Long cursoId,
            Long maestroId,
            Long aulaId,
            String periodo,
            Long id
    );
}
