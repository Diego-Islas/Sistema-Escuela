package com.diego.escuela.entities;

import com.diego.escuela.utils.StringCustomUtils;
import com.diego.escuela.utils.ValoresNumericosUtils;
import com.diego.escuela.exceptions.DatoInvalidoException;
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

    @Column(name = "DESCRIPCION", length = 200)
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

        if (descripcion != null && descripcion.length() > 200) {
            throw new DatoInvalidoException("La descripción no debe superar los 200 caracteres");
        }

        ValoresNumericosUtils.validarEnteroPositvo(
                creditos,
                "El crédito es requerido y debe ser positivo"
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
                .descripcion(normalizarDescripcion(descripcion))
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
        this.descripcion = normalizarDescripcion(descripcion);
        this.creditos = creditos;
    }

    private static String normalizarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            return null;
        }
        return descripcion.trim();
    }
}