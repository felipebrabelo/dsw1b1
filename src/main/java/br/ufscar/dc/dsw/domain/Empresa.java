package br.ufscar.dc.dsw.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// import org.hibernate.validator.constraints.br.CNPJ;

import java.util.Objects;

import org.hibernate.validator.constraints.br.CNPJ;

/*
	Representa a entidade Empresa, conforme o Requisito 2
	Contém os dados cadastrais de uma empresa no sistema.
 */

@Entity
@Table(name = "empresas")
public class Empresa extends Usuario {
	@NotBlank(message = "{empresa.cnpj.notblank}")
	@CNPJ(message = "{empresa.cnpj.invalid}")
	@Column(unique = true, nullable = false, length = 18)
	private String cnpj;

	@Column(columnDefinition = "TEXT") // Permite uma descrição mais longa
	private String descricao;

	@NotBlank(message = "{empresa.cidade.notblank}")
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