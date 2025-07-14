package br.ufscar.dc.dsw.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED) // Estratégia de herança
public class Usuario extends AbstractEntity<Long> {
    @NotBlank(message = "{usuario.nome.notblank}")
    @Size(max = 100, message = "{usuario.nome.size}")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "{usuario.email.notblank}")
    @Email(message = "{usuario.email.email}")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "{usuario.senha.notblank}")
    @Column(nullable = false, length = 64)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String senha;

    @NotBlank(message = "{usuario.role.notblank}")
    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false)
    private boolean enabled = true;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}