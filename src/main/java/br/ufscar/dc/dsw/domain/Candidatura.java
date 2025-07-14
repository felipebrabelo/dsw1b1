package br.ufscar.dc.dsw.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "candidaturas")
public class Candidatura extends AbstractEntity<Long> {

  @NotNull(message = "{candidatura.profissional.notnull}")
  @ManyToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Profissional profissional;

  @NotNull(message = "{candidatura.vaga.notnull}")
  @ManyToOne
  @JoinColumn(name = "vaga_id", nullable = false)
  private Vaga vaga;

  @NotNull(message = "{candidatura.status.notnull}")
  @Column(nullable = false, length = 20)
  @ColumnDefault("ABERTA")
  private String status = "ABERTA"; // Status da candidatura, padrão é "ABERTA"

  @Column(columnDefinition = "TEXT")
  private String statusDescription; // Descrição do status, opcional

  @Column(length = 255)
  private String entrevistaLink;

  @Future(message = "{candidatura.entrevistaData.future}")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
  @Column(name = "entrevista_data")
  private LocalDateTime entrevistaData;

  @Column(name = "curriculo_path")
  private String curriculoPath;

  public Candidatura() {
  }

  public Candidatura(Profissional profissional, Vaga vaga) {
    this.profissional = profissional;
    this.vaga = vaga;
  }

  public String getCurriculoPath() {
    return curriculoPath;
  }

  public void setCurriculoPath(String curriculoPath) {
    this.curriculoPath = curriculoPath;
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

  public String getStatusDescription() {
    return statusDescription;
  }

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public String getEntrevistaLink() {
    return entrevistaLink;
  }

  public void setEntrevistaLink(String entrevistaLink) {
    this.entrevistaLink = entrevistaLink;
  }

  public LocalDateTime getEntrevistaData() {
    return entrevistaData;
  }

  public void setEntrevistaData(LocalDateTime entrevistaData) {
    this.entrevistaData = entrevistaData;
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
