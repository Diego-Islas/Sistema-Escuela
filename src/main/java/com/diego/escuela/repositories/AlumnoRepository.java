package com.diego.escuela.repositories;

import com.diego.escuela.entities.Alumno;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    @Query(value = """
            SELECT GENERAR_MATRICULA(:nombre, :apellidoPaterno, :apellidoMaterno)
            FROM DUAL
            """, nativeQuery = true)
    String generarMatricula(
            @Param("nombre") String nombre,
            @Param("apellidoPaterno") String apellidoPaterno,
            @Param("apellidoMaterno") String apellidoMaterno
    );

    @Query(value = """
            SELECT GENERAR_EMAIL(:nombre, :apellidoPaterno, :apellidoMaterno)
            FROM DUAL
            """, nativeQuery = true)
    String generarEmail(
            @Param("nombre") String nombre,
            @Param("apellidoPaterno") String apellidoPaterno,
            @Param("apellidoMaterno") String apellidoMaterno
    );

    @Override
    @EntityGraph(attributePaths = {
            "inscripciones.grupo.curso",
            "inscripciones.calificacion"
    })
    List<Alumno> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "inscripciones.grupo.curso",
            "inscripciones.calificacion"
    })
    Optional<Alumno> findById(Long id);

    @Query("""
            SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
            FROM Inscripcion i
            WHERE i.alumno.id = :alumnoId
            """)
    boolean tieneInscripciones(@Param("alumnoId") Long alumnoId);
}
