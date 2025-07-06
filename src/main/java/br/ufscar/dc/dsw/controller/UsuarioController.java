package br.ufscar.dc.dsw.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Vaga;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

  @Autowired
  private IVagaDAO vagaRepository;

  @Autowired
  private ICandidaturaDAO candidaturaRepository;

  @GetMapping("/cadastrar")
  public String cadastrarUsuario(ModelMap model) {
    return "usuario/cadastro";
  }

  @GetMapping
  public String listUsuarios(@RequestParam String cargo) {
    return "usuario/list";
  }

  @GetMapping("/{id}")
  public String detailUsuario(@PathVariable("id") Long id, ModelMap model) {
    return "usuario/details";
  }

  @GetMapping("/{id}/vagas")
  public String listVagas(@PathVariable("id") Long id, ModelMap model) {

    List<Candidatura> candidaturas;
    List<Vaga> vagas;
    
    // find vagas by user id
    candidaturas = candidaturaRepository.findByProfissionalId(id);
    vagas = candidaturas.stream()
        .map(Candidatura::getVaga)
        .toList();

    // if (buscaPorCargo && buscaPorCidade) {
    //   vagas = vagaRepository
    //       .findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(
    //           cargo, cidade, dataAtual);
    // } else if (buscaPorCargo) {
    //   vagas = vagaRepository.findByDescricaoContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(cargo,
    //       dataAtual);
    // } else if (buscaPorCidade) {
    //   vagas = vagaRepository.findByEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(cidade,
    //       dataAtual);
    // } else {
    //   vagas = vagaRepository.findAllByDataLimiteInscricaoGreaterThanEqual(dataAtual);
    // }

    model.addAttribute("vagas", vagas);

    return "usuario/vagas";
  }
}
