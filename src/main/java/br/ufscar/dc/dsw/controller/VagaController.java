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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

  

  private Usuario getUsuario() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
        return null;
    }

    Object principal = auth.getPrincipal();

    if (principal instanceof UsuarioDetails) {
        return ((UsuarioDetails) principal).getUsuario();
    }

    return null;
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

  @GetMapping("/empresa/{id}")
  public String minhasVagas(@PathVariable Long id,  ModelMap model) {
    List<Vaga> vagas = vagaService.buscaPorEmpresaId(id);
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
        List<Vaga> vagas = vagaService.buscarVagasAbertasComFiltros(cargo, cidade);
        
       
    model.addAttribute("vagas", vagas);
    model.addAttribute("cargo", cargo);
    // ALTERAÇÃO 2: Envia o valor de volta para a view com o nome "local" para
    // preencher o campo
    model.addAttribute("local", cidade);

    return "vaga/list";
  }

  @GetMapping("/{id}")
  public String detailsVaga(@PathVariable("id") Long id, ModelMap model) {
    // Usa o método otimizado para buscar a vaga com os dados da empresa.
    Vaga vaga = vagaService.buscarPorId(id);

    if (vaga == null) {
      // Redireciona para a lista de vagas se o ID for inválido.
      return "redirect:/vagas";
    }

    // Verifica se o usuário é dono da vaga ou se é um administrador.
    model.addAttribute("isOwner", isOwner(vaga));

    // Verifica se o usuário é um candidato à vaga.
    model.addAttribute("isCandidato", isCandidato(vaga));

    // Verifica se a vaga ainda é válida no momento do acesso.
    boolean vagaExpirada = vaga.getDataLimiteInscricao().isBefore(LocalDate.now());
    model.addAttribute("vagaExpirada", vagaExpirada);
    

    // Prepara dados para candidatura
    Candidatura candidatura = new Candidatura();
    Profissional profissional = getProfissional();
    if (profissional != null) {
      candidatura.setProfissional(profissional);
      candidatura.setVaga(vaga);
      model.addAttribute("candidatura", candidatura);
    } else {
      model.addAttribute("candidatura", null);
    }

    model.addAttribute("vaga", vaga);
    return "vaga/details";
  }

  @GetMapping("{id}/candidaturas")
  public String candidaturasVaga(@PathVariable("id") Long id, ModelMap model) {
    // Valida se o usuario é dono da vaga ou um administrador.
    Vaga vaga = vagaService.buscarPorId(id);
    if (vaga == null || !isOwner(vaga)) {
      return "redirect:/vagas";
    }

    List<Candidatura> candidaturas = candidaturaService.buscarPorVagaId(id);
    model.addAttribute("candidaturas", candidaturas);
    return "vaga/candidaturas";
  }

  @GetMapping("{id}/excluir")
  public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
    if (!candidaturaService.buscarPorVagaId(id).isEmpty()) {
      attr.addFlashAttribute("fail", "vaga.delete.fail");
      return "redirect:/vagas";
    }

    vagaService.excluir(id);
    attr.addFlashAttribute("success", "vaga.delete.success");
    return "redirect:/vagas";
  }

  private boolean isCandidato(Vaga vaga) {
    Profissional profissional = this.getProfissional();
    if (profissional == null) {
      return false;
    }

    return candidaturaService.isCandidateByVaga(vaga.getId(), profissional.getId());
  }

  private boolean isAdmin() {
    Usuario usuario = this.getUsuario();
    return usuario != null && usuario.getRole().equals("ROLE_ADMIN");
  }

  private boolean isOwner(Vaga vaga) {
    Usuario usuario = this.getUsuario();
    if (usuario == null) {
      return false;
    }
    if (usuario.getRole().equals("ROLE_ADMIN")) {
      return true;
    }
    if (vaga.getEmpresa().getId().equals(usuario.getId())) {
      return true;
    }
    return false;
  }
}