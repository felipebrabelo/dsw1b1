package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.impl.CandidaturaService;
import br.ufscar.dc.dsw.service.impl.VagaService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vagas")
public class VagaController {

  @Autowired
  private VagaService vagaService;

  @Autowired
  private CandidaturaService candidaturaService;

  @Autowired
  private IVagaDAO vagaRepository;

  private Usuario getUsuario() {
    UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return usuarioDetails.getUsuario();
  }

  private Empresa getEmpresa() {
    Usuario usuario = this.getUsuario();
    if (usuario instanceof Empresa) {
      return (Empresa) usuario;
    }
    return null;
  }

  private Profissional getProfissional() {
    Usuario usuario = this.getUsuario();
    if (usuario instanceof Profissional) {
      return (Profissional) usuario;
    }
    return null;
  }

  @GetMapping("/minhas-vagas")
  public String minhasVagas(ModelMap model) {
    List<Vaga> vagas = vagaService.buscaPorEmpresaId(this.getEmpresa().getId());
    model.addAttribute("vagas", vagas);

    return "vaga/list";
  }

  @GetMapping("/cadastrar")
  public String cadastrarVaga(Vaga vaga) {
    Empresa empresa = this.getEmpresa();
    if (empresa != null) {
      vaga.setEmpresa(empresa);
    }
    return "vaga/cadastro";
  }

  @PostMapping("/salvar")
  public String salvar(@Valid Vaga vaga, BindingResult result, RedirectAttributes attr) {
    if (result.hasErrors()) {
      return "vaga/cadastro";
    }

    vagaService.salvar(vaga);
    attr.addFlashAttribute("success", "vaga.create.success");
    System.out.println(">>> Vaga cadastrada: " + vaga.getId());
    return "redirect:/vagas";
  }

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

  @GetMapping("/{id}")
  public String detailsVaga(@PathVariable("id") Long id, Candidatura candidatura, ModelMap model) {
    // Usa o método otimizado para buscar a vaga com os dados da empresa.
    Vaga vaga = vagaService.buscarPorId(id);

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

  @GetMapping("{id}/candidaturas")
  public String candidaturasVaga(@PathVariable("id") Long id, ModelMap model) {

    List<Candidatura> candidaturas = candidaturaService.buscarPorVagaId(id);
    model.addAttribute("candidaturas", candidaturas);
    return "vaga/candidaturas";
  }
}