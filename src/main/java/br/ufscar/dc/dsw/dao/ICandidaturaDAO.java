package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Candidatura;

public interface ICandidaturaDAO extends CrudRepository<Candidatura, Long> {
  List<Candidatura> findByProfissionalId(Long profissionalId);
}
