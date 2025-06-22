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

import java.util.List;

@Controller
@RequestMapping("/vagas")
public class VagaController {

  @Autowired
  private VagaRepository vagaRepository;

  // Lista as vagas com base nos filtros de cargo e cidade.

  @GetMapping
  public String listVagas(@RequestParam(name = "cargo", required = false) String cargo,
      @RequestParam(name = "cidade", required = false) String cidade,
      ModelMap model) {

    List<Vaga> vagas;

    boolean buscaPorCargo = cargo != null && !cargo.trim().isEmpty();
    boolean buscaPorCidade = cidade != null && !cidade.trim().isEmpty();

    if (buscaPorCargo && buscaPorCidade) {
      vagas = vagaRepository.findByDescricaoContainingIgnoreCaseAndEmpresaCidadeContainingIgnoreCase(cargo, cidade);
    } else if (buscaPorCargo) {
      vagas = vagaRepository.findByDescricaoContainingIgnoreCase(cargo);
    } else if (buscaPorCidade) {
      vagas = vagaRepository.findByEmpresaCidadeContainingIgnoreCase(cidade);
    } else {
      // Se nenhum filtro for aplicado, lista todas as vagas.
      vagas = vagaRepository.findAll();
    }

    model.addAttribute("vagas", vagas);
    model.addAttribute("cargo", cargo);
    model.addAttribute("cidade", cidade);
    return "vaga/list";
  }

  // Exibe os detalhes de uma vaga específica.

  @GetMapping("/{id}")
  public String detailsVaga(@PathVariable("id") Long id, ModelMap model) {
    Vaga vaga = vagaRepository.findByIdWithEmpresa(id);

    if (vaga == null) {
      // Lidar com o caso de uma vaga não encontrada, redirecionando
      // ou mostrando uma página de erro 404.
      return "redirect:/vagas";
    }

    model.addAttribute("vaga", vaga);
    return "vaga/details";
  }
}