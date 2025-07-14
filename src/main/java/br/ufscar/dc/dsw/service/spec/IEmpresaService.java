package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Empresa;

public interface IEmpresaService {
  Empresa buscarPorId(Long id);
  List<Empresa> buscarTodos();
  void salvar(Empresa profissional);
  void excluir(Long id);
  List<Empresa> buscarPorNome(String nome);
  List<Empresa> buscarPorCidade(String nome);
  boolean hasVagas(Long id);
}
