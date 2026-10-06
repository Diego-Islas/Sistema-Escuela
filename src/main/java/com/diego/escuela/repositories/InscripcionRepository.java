package com.diego.escuela.repositories;

import com.diego.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    @Query("""
            SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
            FROM Inscripcion i
            WHERE i.alumno.id = :idAlumno AND i.grupo.id = :idGrupo
            """)
    boolean existePorAlumnoYGrupo(
            @Param("idAlumno") Long idAlumno,
            @Param("idGrupo") Long idGrupo
    );

    @Query("""
            SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
            FROM Inscripcion i
            WHERE i.alumno.id = :idAlumno
              AND i.grupo.id = :idGrupo
              AND i.id <> :idInscripcion
            """)
    boolean existePorAlumnoYGrupoExcluyendo(
            @Param("idAlumno") Long idAlumno,
            @Param("idGrupo") Long idGrupo,
            @Param("idInscripcion") Long idInscripcion
    );

    @Override
    @EntityGraph(attributePaths = {
            "alumno",
            "grupo.curso",
            "grupo.maestro",
            "grupo.aula",
            "calificacion"
    })
    List<Inscripcion> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "alumno",
            "grupo.curso",
            "grupo.maestro",
            "grupo.aula",
            "calificacion"
    })
    Optional<Inscripcion> findById(Long id);
}
