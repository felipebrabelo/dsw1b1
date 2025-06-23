package br.ufscar.dc.dsw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// import org.hibernate.validator.constraints.br.CNPJ;

import java.util.Objects;

/*
 Representa a entidade Empresa, conforme o Requisito 2
 Contém os dados cadastrais de uma empresa no sistema.

 */

@Entity
@Table(name = "empresas")
public class Empresa extends Usuario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nome;

   

    

    @NotBlank(message = "O CNPJ é obrigatório.")
    // @CNPJ(message = "CNPJ inválido.")           -> removi pra popular o banco de dados inicialmente
    @Column(unique = true, nullable = false, length = 18) // Formato com máscara: XX.XXX.XXX/XXXX-XX
    private String cnpj;

    @Column(columnDefinition = "TEXT") // Permite uma descrição mais longa
    private String descricao;

    @NotBlank(message = "A cidade é obrigatória.")
    @Column(nullable = false)
    private String cidade;

    // Construtores

    public Empresa() {
        super.setRole("ROLE_EMPRESA");
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

      
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    // hashCode e equals para comparações de objetos

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(id, empresa.id) && Objects.equals(cnpj, empresa.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cnpj);
    }
}