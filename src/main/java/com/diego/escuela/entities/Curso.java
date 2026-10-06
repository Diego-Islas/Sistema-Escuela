package com.diego.escuela.entities;

import com.diego.escuela.utils.StringCustomUtils;
import com.diego.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CURSOS",
        uniqueConstraints = {
                @UniqueConstraint(name = "CURSO_UK", columnNames = {"NOMBRE"})
        })
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long id;

    @Column(name = "NOMBRE", length = 100, nullable = false, unique = true)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200, nullable = false)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false)
    private Integer creditos;

    private static void validarDatos(
            String nombre,
            String descripcion,
            Integer creditos
    ) {
        StringCustomUtils.validarTamanio(
                nombre, 5, 100,
                "El nombre es requerido y debe tener entre 5 y 100 caracteres"
        );

        StringCustomUtils.validarTamanio(
                descripcion, 1, 200,
                "La descripción es requerida y no debe superar los 200 caracteres"
        );

        ValoresNumericosUtils.validarEnteroPositvo(
                creditos,
                "El credito es requerido y debe ser positivo"
        );
    }

    public static Curso crear(
            String nombre,
            String descripcion,
            Integer creditos
    ) {
        validarDatos(nombre, descripcion, creditos);
        return Curso.builder()
                .nombre(nombre.trim())
                .descripcion(descripcion.trim())
                .creditos(creditos)
                .build();
    }

    public void actualizar(
            String nombre,
            String descripcion,
            Integer creditos
    ) {
        validarDatos(nombre, descripcion, creditos);
        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
        this.creditos = creditos;
    }
}