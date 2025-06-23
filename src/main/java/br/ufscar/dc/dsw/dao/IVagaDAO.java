package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw.domain.Vaga;

import java.time.LocalDate;
import java.util.List;

public interface IVagaDAO extends CrudRepository<Vaga, Long> {

	// Busca vagas que contenham a cidade da empresa informada e que estejam dentro
	// da data limite.

	List<Vaga> findByEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(String cidade,
			LocalDate data);

	// Busca vagas que contenham a descrição (cargo) informada e que estejam dentro
	// da data limite.

	List<Vaga> findByDescricaoContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(String descricao,
			LocalDate data);

	// Busca vagas que correspondam tanto à descrição quanto à cidade da empresa, e
	// que estejam dentro da data limite.

	List<Vaga> findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(
			String descricao, String cidade, LocalDate data);

	// Busca todas as vagas que ainda estão abertas (cuja data limite é maior ou
	// igual à data atual).

	List<Vaga> findAllByDataLimiteInscricaoGreaterThanEqual(LocalDate data);

	// Busca uma vaga pelo seu ID e garante que os dados da empresa associada sejam
	// carregados na mesma consulta, evitando LazyInitializationException.

	@Query("SELECT v FROM Vaga v JOIN FETCH v.empresa WHERE v.id = :id")
	Vaga findByIdWithEmpresa(@Param("id") Long id);

}