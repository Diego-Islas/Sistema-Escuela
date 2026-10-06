package com.diego.escuela.entities;

import com.diego.escuela.enums.DiaSemana;
import com.diego.escuela.exceptions.DatoInvalidoException;
import com.diego.escuela.utils.HoraUtils;
import com.diego.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HORARIOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIA", nullable = false, length = 15)
    private DiaSemana dia;

    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

    private static void validarDatos(
            Grupo grupo,
            DiaSemana dia,
            String horaInicio,
            String horaFin
    ) {
        if (grupo == null)
            throw new DatoInvalidoException("El grupo es requerido");
        if (dia == null)
            throw new DatoInvalidoException("El día es requerido");
        StringCustomUtils.validarNoVacio(horaInicio, "La hora de inicio es requerida");
        HoraUtils.validarHora(horaInicio, "El formato de hora inicio debe ser HH:mm");
        StringCustomUtils.validarNoVacio(horaFin, "La hora de fin es requerida");
        HoraUtils.validarHora(horaFin, "El formato de hora fin debe ser HH:mm");
        if (!HoraUtils.esHoraPosterior(horaFin, horaInicio))
            throw new DatoInvalidoException("La hora de fin debe ser posterior a la hora de inicio");
    }

    public static Horario crear(
            Grupo grupo,
            DiaSemana diaSemana,
            String horaInicio,
            String horaFin
    ) {
        validarDatos(grupo, diaSemana, horaInicio, horaFin);

        return Horario.builder()
                .grupo(grupo)
                .dia(diaSemana)
                .horaInicio(horaInicio)
                .horaFin(horaFin)
                .build();
    }

    public void actualizar(
            Grupo grupo,
            DiaSemana dia,
            String horaInicio,
            String horaFin
    ) {
        validarDatos(grupo, dia, horaInicio, horaFin);
        this.grupo = grupo;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
}
