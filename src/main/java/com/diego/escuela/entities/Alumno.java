package com.diego.escuela.entities;

import jakarta.persistence.*;
import com.diego.escuela.exceptions.DatoInvalidoException;
import com.diego.escuela.utils.StringCustomUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ALUMNOS", uniqueConstraints = {
        @UniqueConstraint(
                name = "ALUMNO_MATRICULA_UK",
                columnNames = {"MATRICULA"}
        ),
        @jakarta.persistence.UniqueConstraint(
                name = "ALUMNO_EMAIL_UK",
                columnNames = {"EMAIL"}
        )})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "MATRICULA", nullable = false, length = 10, unique = true)
    private String matricula;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Builder.Default
    @Column(name = "FECHA_INGRESO", nullable = false)
    private LocalDate fechaIngreso = LocalDate.now();

    @Builder.Default
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    private static void validarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String matricula,
            String email
    ) {
        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(matricula, 1, 10,
                "La matrícula es requerida y no debe superar 10 caracteres");
        StringCustomUtils.validarTamanio(email, 1, 100,
                "El email es requerido y no debe superar 100 caracteres");
    }

    public static Alumno crear(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String matricula,
            String email
    ) {
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, matricula, email);
        return Alumno.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .matricula(matricula.trim())
                .email(email.trim().toLowerCase())
                .fechaIngreso(LocalDate.now())
                .build();
    }

    public void actualizar(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String matricula,
            String email
    ) {
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, matricula, email);
        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.matricula = matricula.trim();
        this.email = email.trim().toLowerCase();
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        if (inscripcion != null && !inscripciones.contains(inscripcion)) {
            inscripciones.add(inscripcion);
        }
    }

    public void quitarInscripcion(Inscripcion inscripcion) {
        inscripciones.remove(inscripcion);
    }
}
