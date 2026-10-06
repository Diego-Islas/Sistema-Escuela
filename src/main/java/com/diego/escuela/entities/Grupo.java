package com.diego.escuela.entities;

import com.diego.escuela.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GRUPOS",
        uniqueConstraints = {
                @UniqueConstraint(name = "GRUPO_CU_MA_AU_PE_UK", columnNames = {
                        "ID_CURSO",
                        "ID_MAESTRO",
                        "ID_AULA",
                        "PERIODO"
                })
        })
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", length = 20, nullable = false)
    private String periodo;

    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Horario> horarios = new ArrayList<>();

    private static void validarDatos(
            Curso curso,
            Maestro maestro,
            Aula aula,
            String periodo
    ) {
        if (curso == null)
            throw new DatoInvalidoException("El curso es requerido");
        if (maestro == null)
            throw new DatoInvalidoException("El maestro es requerido");
        if (aula == null)
            throw new DatoInvalidoException("El aula es requerida");
        if (periodo == null || !periodo.matches("\\d{4}-(0?[1-9]|1[0-2])"))
            throw new DatoInvalidoException("El periodo debe tener formato YYYY-M o YYYY-MM");
    }

    public void actualizar(
            Curso curso,
            Maestro maestro,
            Aula aula,
            String periodo
    ) {
        validarDatos(curso, maestro, aula, periodo);
        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
        this.periodo = periodo;
    }


    public static Grupo crear(
            Curso curso,
            Maestro maestro,
            Aula aula,
            String periodo
    ) {
        validarDatos(curso, maestro, aula, periodo);
        return Grupo.builder()
                .curso(curso)
                .maestro(maestro)
                .aula(aula)
                .periodo(periodo)
                .build();
    }
}