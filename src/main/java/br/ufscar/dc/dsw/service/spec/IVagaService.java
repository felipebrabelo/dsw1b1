package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Vaga;

public interface IVagaService {

	Vaga buscarPorId(Long id);

	List<Vaga> buscarTodos();

	void salvar(Vaga vaga);

	void excluir(Long id);

	List<Vaga> buscarVagasAbertasComFiltros(String descricao, String cidade);	
	List<Vaga> buscarAbertasDaEmpresa(Long id); 
}
