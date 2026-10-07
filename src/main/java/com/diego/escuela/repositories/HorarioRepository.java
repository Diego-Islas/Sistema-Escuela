package com.diego.escuela.repositories;

import com.diego.escuela.entities.Horario;
import com.diego.escuela.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    boolean existsByGrupoId(Long grupoId);

    @Query("""
        SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END
        FROM Horario h
        WHERE (:idHorario IS NULL OR h.id <> :idHorario)
          AND h.dia = :dia
          AND (
                h.grupo.id = :idGrupo
                OR h.grupo.aula.id = :idAula
              )
          AND h.horaInicio < :horaFin
          AND h.horaFin > :horaInicio
        """)
    boolean existeTraslape(
            @Param("idHorario") Long idHorario,
            @Param("dia") DiaSemana dia,
            @Param("idGrupo") Long idGrupo,
            @Param("idAula") Long idAula,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin
    );
}