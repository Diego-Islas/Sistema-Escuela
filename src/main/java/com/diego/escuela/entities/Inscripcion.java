package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "INSCRIPCIONES",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "INSCRIPCION_ALU_GRU_UK",
                        columnNames = {"ID_ALUMNO", "ID_GRUPO"}
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;
    @Column(name = "FECHA_INSCRIPCION", nullable = false)
    private LocalDate fechaInscripcion = LocalDate.now();

    @OneToOne(mappedBy = "inscripcion")
    private Calificacion calificacion;

    public void actualizar(Alumno alumno, Grupo grupo) {
        validarRelaciones(alumno, grupo);
        this.alumno = alumno;
        this.grupo = grupo;
    }

    public void asignarCalificacion(Calificacion calificacion) {
        if (calificacion == null)
            throw new DatoInvalidoException("La calificación es requerida");
        this.calificacion = calificacion;
    }

    public void quitarCalificacion(Calificacion calificacion) {
        if (this.calificacion == calificacion) {
            this.calificacion = null;
        }
    }

    private static void validarRelaciones(Alumno alumno, Grupo grupo) {
        if (alumno == null)
            throw new DatoInvalidoException("El alumno es requerido");
        if (grupo == null)
            throw new DatoInvalidoException("El grupo es requerido");
    }

    public static Inscripcion crear(Alumno alumno, Grupo grupo) {
        validarRelaciones(alumno, grupo);
        return Inscripcion.builder()
                .alumno(alumno)
                .grupo(grupo)
                .fechaInscripcion(LocalDate.now())
                .build();
    }
}
