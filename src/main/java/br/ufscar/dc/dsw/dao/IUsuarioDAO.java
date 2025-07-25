package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw.domain.Usuario;

public interface IUsuarioDAO extends CrudRepository<Usuario, Long> {
    Usuario findById(long id);

    List<Usuario> findAll();

    Usuario save(Usuario usuario);

    void deleteById(long id);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario getUserByEmail(@Param("email") String email);
}
