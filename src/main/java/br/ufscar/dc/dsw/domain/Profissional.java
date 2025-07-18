package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Representa a entidade Profissional, conforme o Requisito 1.
 * Armazena os dados de um usuário candidato a vagas.
 */
@Entity
@Table(name = "Profissional")
public class Profissional extends Usuario {

    @NotBlank(message = "{profissional.cpf.notblank}")
    @CPF(message = "{profissional.cpf.invalid}")
    @Column(unique = true, nullable = false, length = 14)
    private String cpf;

    @Column
    private String telefone; // sem formatação

    @Column
    private String sexo;

    @NotNull(message = "{profissional.dataNascimento.notnull}")
    @Past(message = "{profissional.dataNascimento.past}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    // Construtor padrão
    public Profissional() {
        super.setRole("ROLE_PROFISSIONAL");
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}