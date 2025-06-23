package br.ufscar.dc.dsw.dao;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Profissional;

public interface IProfissionalDAO extends CrudRepository<Profissional, Long> {
}

