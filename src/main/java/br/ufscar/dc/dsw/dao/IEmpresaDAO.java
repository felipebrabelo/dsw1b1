package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Empresa;

public interface IEmpresaDAO extends CrudRepository<Empresa, Long> {
  Empresa findById(long id);
  Empresa save(Empresa profissional);
  void deleteById(long id);
  List<Empresa> findAll();
  List<Empresa> findByNome(String nome);
  List<Empresa> findByCidade(String nome);
}


