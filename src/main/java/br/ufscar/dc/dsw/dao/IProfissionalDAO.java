package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Profissional;

public interface IProfissionalDAO extends CrudRepository<Profissional, Long> {
  Profissional findById(long id);
  Profissional save(Profissional profissional);
  void deleteById(long id);
  List<Profissional> findAll();
}

