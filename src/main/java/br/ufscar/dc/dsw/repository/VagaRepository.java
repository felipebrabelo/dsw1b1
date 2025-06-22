package br.ufscar.dc.dsw.repository;

import br.ufscar.dc.dsw.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    // Busca apenas pela cidade da empresa
    List<Vaga> findByEmpresaCidadeContainingIgnoreCase(String cidade);

    // Busca apenas pela descrição da vaga
    List<Vaga> findByDescricaoContainingIgnoreCase(String descricao);

    // Busca quando AMBOS os campos estão preenchidos
    List<Vaga> findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCase(String descricao, String cidade);

    @Query("SELECT v FROM Vaga v JOIN FETCH v.empresa WHERE v.id = :id")
    Vaga findByIdWithEmpresa(Long id); // Adicionado para resolver problema de LazyInitializationException
                                      // Conexão com o banco já fechada quando clicamos em "Ver detalhes" de uma vaga

}