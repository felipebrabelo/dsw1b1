package br.ufscar.dc.dsw.domain;

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
public class Empresa extends Usuario {
	@NotBlank(message = "O CNPJ é obrigatório.")
	// @CNPJ(message = "CNPJ inválido.") -> removi pra popular o banco de dados
	// inicialmente
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
}