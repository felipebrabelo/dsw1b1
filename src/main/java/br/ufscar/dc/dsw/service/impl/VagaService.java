package br.ufscar.dc.dsw.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.service.spec.IVagaService;

@Service
@Transactional(readOnly = false)
public class VagaService implements IVagaService {

	@Autowired
	IVagaDAO dao;

	public void salvar(Vaga vaga) {
		dao.save(vaga);
	}

	public void excluir(Long id) {
		dao.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Vaga buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Transactional(readOnly = true)
	public List<Vaga> buscarTodos() {
		return dao.findAll();
	}

	@Override
	public List<Vaga> buscarVagasAbertasComFiltros(String descricao, String cidade) {
		LocalDate dataAtual = LocalDate.now();
		if (descricao != null && cidade != null) {
			return dao
					.findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(
							descricao, cidade, dataAtual);
		} else if (descricao != null) {
			return dao.findByDescricaoContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(descricao, dataAtual);
		} else if (cidade != null) {
			return dao.findByEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(cidade, dataAtual);
		} else {
			return buscarTodos();
		}

	}

	@Override
	public List<Vaga> buscarAbertasDaEmpresa(Long id) {
		return dao.findByEmpresa_Id(id);
	}

}
