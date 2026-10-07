package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CALIFICACIONES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INSCRIPCION", nullable = false, unique = true)
    private Inscripcion inscripcion;

    @Column(name = "CALIFICACION", nullable = false, precision = 3, scale = 1)
    private BigDecimal calificacion;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();

    private static void validarDatos(
            Inscripcion inscripcion,
            BigDecimal calificacion
    ) {
        if (inscripcion == null)
            throw new DatoInvalidoException("La inscripción es requerida");
        if (calificacion == null
                || calificacion.compareTo(BigDecimal.TEN) > 0
                || calificacion.compareTo(BigDecimal.ZERO) < 0)
            throw new DatoInvalidoException("La calificación debe ser positiva y estar entre 0 y 10");
    }

    public void actualizar(Inscripcion inscripcion, BigDecimal calificacion) {
        validarDatos(inscripcion, calificacion);
        if (this.inscripcion != inscripcion) {
            this.inscripcion.quitarCalificacion(this);
            inscripcion.asignarCalificacion(this);
            this.inscripcion = inscripcion;
        }
        this.calificacion = calificacion;
    }

    public static Calificacion crear(Inscripcion inscripcion, BigDecimal calificacion) {
        validarDatos(inscripcion, calificacion);
        Calificacion nueva = Calificacion.builder()
                .inscripcion(inscripcion)
                .calificacion(calificacion)
                .fechaRegistro(LocalDate.now())
                .build();
        inscripcion.asignarCalificacion(nueva);
        return nueva;
    }
}
