package br.ufscar.dc.dsw.domain;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "candidaturas")
public class Candidatura extends AbstractEntity<Long> {

  @NotNull(message = "O usuário é obrigatório.")
  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Profissional profissional;

  @NotNull(message = "A vaga é obrigatória.")
  @ManyToOne
  @JoinColumn(name = "vaga_id", nullable = false)
  private Vaga vaga;

  @NotNull(message = "O status da candidatura é obrigatório.")
  @Column(nullable = false, length = 20)
  @ColumnDefault("PENDENTE")
  private String status = "PENDENTE"; // Status da candidatura, padrão é "PENDENTE"

  public Candidatura() {}
  public Candidatura(Profissional profissional, Vaga vaga) {
    this.profissional = profissional;
    this.vaga = vaga;
  }

  public Usuario getProfissional() {
    return profissional;
  }

  public void setProfissional(Profissional profissional) {
    this.profissional = profissional;
  }

  public Vaga getVaga() {
    return vaga;
  }

  public void setVaga(Vaga vaga) {
    this.vaga = vaga;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
