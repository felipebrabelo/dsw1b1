package br.ufscar.dc.dsw.repository;

import br.ufscar.dc.dsw.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}


