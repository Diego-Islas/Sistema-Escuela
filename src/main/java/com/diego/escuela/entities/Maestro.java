package com.diego.escuela.entities;

import com.diego.escuela.utils.StringCustomUtils;
import com.diego.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "MAESTROS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "MAESTRO_TELEFONO_UK",
                        columnNames = {"TELEFONO"}
                ),
                @UniqueConstraint(
                        name = "MAESTRO_EMAIL_UK",
                        columnNames = {"EMAIL"}
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Maestro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MAESTRO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "TELEFONO", nullable = false, length = 10, unique = true)
    private String telefono;

    @Builder.Default
    @OneToMany(mappedBy = "maestro", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();

    private static void validarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String telefono
    ) {

        StringCustomUtils.validarTamanio(
                nombre,
                1,
                50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres"
        );

        StringCustomUtils.validarTamanio(
                apellidoPaterno,
                1,
                50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres"
        );

        StringCustomUtils.validarTamanio(
                apellidoMaterno,
                1,
                50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres"
        );

        StringCustomUtils.validarTamanio(
                email,
                1,
                100,
                "El email es requerido y debe tener entre 1 y 100 caracteres"
        );

        StringCustomUtils.validarTamanio(
                telefono,
                10,
                10,
                "El teléfono es requerido y debe tener exactamente 10 dígitos"
        );
        ValoresNumericosUtils.validarStringSoloNumeros(
                telefono,
                "El teléfono debe contener únicamente dígitos"
        );
    }


    public void actualizarMaestro(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String telefono
    ) {
        // Validamos los datos antes de actualizar el maestro
        validarDatos(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                email,
                telefono
        );

        // Actualizamos los datos eliminando espacios innecesarios
        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
    }

    public static Maestro crearMaestro(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String telefono
    ) {

        // Validamos los datos antes de crear el maestro
        validarDatos(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                email,
                telefono
        );

        // Creamos el objeto Maestro usando Builder
        return Maestro.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .email(email.trim().toLowerCase())
                .telefono(telefono.trim())
                .build();
    }

    public String getNombreCompleto() {
        return String.join(" ", nombre, apellidoPaterno, apellidoMaterno);
    }
}
