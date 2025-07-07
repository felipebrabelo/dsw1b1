package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.impl.CandidaturaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/candidaturas")
public class CandidaturaController {

  @Autowired
  private CandidaturaService candidaturaService;

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

  @PostMapping("/cadastrar")
  public String cadastrar(@Valid Candidatura candidatura, BindingResult result, RedirectAttributes attr) {
    if (result.hasErrors()) {
      attr.addFlashAttribute("mensagem", "Erro ao cadastrar candidatura.");
      return "redirect:/vagas/detalhes/" + candidatura.getVaga().getId();
    }
    candidatura.setId(null);
    candidaturaService.salvar(candidatura);

    attr.addFlashAttribute("success", "vaga.candidatura.success");

    return "redirect:/candidaturas";
  }

  @GetMapping
  public String listar(ModelMap model) {
    List<Candidatura> candidaturas = candidaturaService.buscarPorProfissional(this.getProfissional());
    model.addAttribute("candidaturas", candidaturas);

    return "candidatura/lista";
  }
}
