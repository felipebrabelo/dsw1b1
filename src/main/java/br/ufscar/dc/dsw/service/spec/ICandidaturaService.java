package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Profissional;

public interface ICandidaturaService {

	Candidatura buscarPorId(Long id);

	List<Candidatura> buscarTodos();

  List<Candidatura> buscarPorVagaId(Long id);

	List<Candidatura> buscarPorProfissional(Profissional profissional);

	boolean isCandidateByVaga(Long vagaId, Long profissionalId);

	void salvar(Candidatura editora);

	void excluir(Long id);	
}
