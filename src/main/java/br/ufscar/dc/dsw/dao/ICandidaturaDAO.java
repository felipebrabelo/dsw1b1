package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Candidatura;

public interface ICandidaturaDAO extends CrudRepository<Candidatura, Long> {
  Candidatura findById(long id);
  Candidatura save(Candidatura profissional);
  void deleteById(long id);
  List<Candidatura> findAll();
  List<Candidatura> findByProfissionalId(Long profissionalId);
  List<Candidatura> findByVagaId(Long vagaId);
}
