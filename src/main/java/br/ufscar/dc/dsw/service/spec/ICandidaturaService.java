package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Candidatura;

public interface ICandidaturaService {

	Candidatura buscarPorId(Long id);

	List<Candidatura> buscarTodos();

  List<Candidatura> buscarPorVagaId(Long id);

	void salvar(Candidatura editora);

	void excluir(Long id);	
}
