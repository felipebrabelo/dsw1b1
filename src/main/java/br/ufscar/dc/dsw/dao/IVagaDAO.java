package br.ufscar.dc.dsw.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Vaga;

import java.time.LocalDate;
import java.util.List;

public interface IVagaDAO extends CrudRepository<Vaga, Long> {
	Vaga findById(long id);
	Vaga save(Vaga profissional);
	void deleteById(long id);

	List<Vaga> findAll();
	 // Busca apenas pela cidade da empresa e data
    List<Vaga> findByEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(String cidade, LocalDate data);

    // Busca apenas pela descrição da vaga e data
    List<Vaga> findByDescricaoContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(String descricao, LocalDate data);

    // Busca quando AMBOS os campos estão preenchidos e data
    List<Vaga> findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(String descricao, String cidade, LocalDate data);

    // Busca todas as vagas em aberto
    List<Vaga> findAllByDataLimiteInscricaoGreaterThanEqual(LocalDate data);

    // Busca todas as vagas de uma empresa específica (para o Requisito R6)
    List<Vaga> findByEmpresa_Id(Long empresaId);

    // Garante que a empresa venha junto na consulta de detalhes
    @Query("SELECT v FROM Vaga v JOIN FETCH v.empresa e WHERE v.id = :id")
    Vaga findByIdWithEmpresa(Long id);
}
	

