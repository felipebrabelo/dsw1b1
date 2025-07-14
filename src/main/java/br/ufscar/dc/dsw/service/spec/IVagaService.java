package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Vaga;

public interface IVagaService {

	Vaga buscarPorId(Long id);

	List<Vaga> buscarTodos();

	List<Vaga> buscaPorEmpresaId(Long id);

	void salvar(Vaga vaga);

	void excluir(Long id);

	List<Vaga> buscarVagasAbertasComFiltros(String cargo, String cidade);	
	List<Vaga> buscarAbertasDaEmpresa(Long id); 
}
