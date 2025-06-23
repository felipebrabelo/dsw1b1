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

  /**
   * Lista as vagas com base nos filtros de cargo e/ou cidade.
   * Todas as buscas consideram apenas vagas com data de inscrição válida.
   */
  @GetMapping
  public String listVagas(@RequestParam(name = "cargo", required = false) String cargo,
      @RequestParam(name = "cidade", required = false) String cidade,
      ModelMap model) {

    List<Vaga> vagas;
    // Garante que os parâmetros de busca não sejam nulos e remove espaços em
    // branco.
    String cargoBusca = (cargo != null) ? cargo.trim() : "";
    String cidadeBusca = (cidade != null) ? cidade.trim() : "";

    boolean buscaPorCargo = !cargoBusca.isEmpty();
    boolean buscaPorCidade = !cidadeBusca.isEmpty();
    LocalDate dataAtual = LocalDate.now(); // Data atual para filtrar vagas ativas

    if (buscaPorCargo && buscaPorCidade) {
      // Cenário 1: Ambos os campos (cargo e cidade) estão preenchidos.
      vagas = vagaRepository
          .findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(
              cargoBusca, cidadeBusca, dataAtual);
    } else if (buscaPorCargo) {
      // Cenário 2: Apenas o campo cargo está preenchido.
      vagas = vagaRepository.findByDescricaoContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(cargoBusca,
          dataAtual);
    } else if (buscaPorCidade) {
      // Cenário 3: Apenas o campo cidade está preenchido.
      vagas = vagaRepository.findByEmpresaCidadeContainingIgnoreCaseAndDataLimiteInscricaoGreaterThanEqual(cidadeBusca,
          dataAtual);
    } else {
      // Cenário 4: Nenhum filtro foi aplicado. Lista todas as vagas válidas.
      vagas = vagaRepository.findAllByDataLimiteInscricaoGreaterThanEqual(dataAtual);
    }

    model.addAttribute("vagas", vagas);
    // Devolve os parâmetros originais para preencher os campos de busca na tela.
    model.addAttribute("cargo", cargo);
    model.addAttribute("cidade", cidade);
    return "vaga/list";
  }

  /**
   * Exibe os detalhes de uma vaga específica.
   */
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