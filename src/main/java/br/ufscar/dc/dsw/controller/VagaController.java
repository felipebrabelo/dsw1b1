package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.model.Vaga;
import br.ufscar.dc.dsw.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vagas")
public class VagaController {

  @Autowired
  private VagaRepository vagaRepository;

  // Lista as vagas com base nos filtros de cargo e/ou cidade.
  // Todas as buscas consideram apenas vagas com data de inscrição válida.

  @GetMapping
  public String listVagas(@RequestParam(name = "cargo", required = false) String cargo,
      // ALTERAÇÃO 1: Mapeia o parâmetro "local" da URL para a variável "cidade"
      @RequestParam(name = "local", required = false) String cidade,
      ModelMap model) {

    List<Vaga> vagas;
    LocalDate dataAtual = LocalDate.now(); // Pega a data atual uma vez

    boolean buscaPorCargo = cargo != null && !cargo.trim().isEmpty();
    boolean buscaPorCidade = cidade != null && !cidade.trim().isEmpty();

    if (buscaPorCargo && buscaPorCidade) {
      vagas = vagaRepository
          .findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(
              cargo, cidade, dataAtual);
    } else if (buscaPorCargo) {
      vagas = vagaRepository.findByDescricaoContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(cargo,
          dataAtual);
    } else if (buscaPorCidade) {
      vagas = vagaRepository.findByEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(cidade,
          dataAtual);
    } else {
      vagas = vagaRepository.findAllByDataLimiteInscricaoGreaterThanEqual(dataAtual);
    }

    model.addAttribute("vagas", vagas);
    model.addAttribute("cargo", cargo);
    // ALTERAÇÃO 2: Envia o valor de volta para a view com o nome "local" para
    // preencher o campo
    model.addAttribute("local", cidade);

    return "vaga/list";
  }

  // Exibe os detalhes de uma vaga específica.

  @GetMapping("/{id}")
  public String detailsVaga(@PathVariable("id") Long id, ModelMap model) {
    // Usa o método otimizado para buscar a vaga com os dados da empresa.
    Vaga vaga = vagaRepository.findByIdWithEmpresa(id);

    if (vaga == null) {
      // Redireciona para a lista de vagas se o ID for inválido.
      return "redirect:/vagas";
    }

    // Verifica se a vaga ainda é válida no momento do acesso.
    if (vaga.getDataLimiteInscricao().isBefore(LocalDate.now())) {
      model.addAttribute("vagaExpirada", true);
    }

    model.addAttribute("vaga", vaga);
    return "vaga/details";
  }
}