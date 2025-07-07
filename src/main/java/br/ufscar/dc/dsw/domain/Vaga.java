package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Representa a entidade Vaga, conforme os Requisitos 3 e 5.
 * Armazena os dados de uma oportunidade de trabalho/estágio.
 */
@Entity
@Table(name = "vagas")
public class Vaga extends AbstractEntity<Long> {
    @NotBlank(message = "O título é obrigatório.")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "A descrição é obrigatória.")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao; // Descrição da vaga, pode ser longa

    @NotNull(message = "A remuneração é obrigatória.")
    @PositiveOrZero(message = "A remuneração não pode ser negativa.")
    @Column(nullable = false)
    private BigDecimal remuneracao; // Remuneração da vaga, pode ser zero (vaga não remunerada)

    @NotNull(message = "A data limite de inscrição é obrigatória.")
    @Future(message = "A data limite deve ser no futuro.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_limite_inscricao", nullable = false)
    private LocalDate dataLimiteInscricao; // Data limite para inscrição na vaga, deve ser no futuro

    // Relacionamento: Muitas vagas pertencem a UMA empresa.
    @NotNull
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa; // Vinculado a partir do "CNPJ da empresa"

    // Construtor padrão
    public Vaga() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getRemuneracao() {
        return remuneracao;
    }

    public void setRemuneracao(BigDecimal remuneracao) {
        this.remuneracao = remuneracao;
    }

    public LocalDate getDataLimiteInscricao() {
        return dataLimiteInscricao;
    }

    public void setDataLimiteInscricao(LocalDate dataLimiteInscricao) {
        this.dataLimiteInscricao = dataLimiteInscricao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}