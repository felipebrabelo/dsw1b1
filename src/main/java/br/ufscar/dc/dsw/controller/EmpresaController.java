package br.ufscar.dc.dsw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.impl.VagaService;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/empresas")
public class EmpresaController {
  @Autowired
  private IEmpresaService empresaService;

  @Autowired
  private VagaService vagaService;

  @Autowired
  private BCryptPasswordEncoder encoder;

  private Empresa getEmpresa() {
    UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    if (usuarioDetails.getUsuario() instanceof Empresa) {
      return (Empresa) usuarioDetails.getUsuario();
    }
    return null;
  } 

  @GetMapping
  public String listarEmpresas(ModelMap model) {
    model.addAttribute("empresas", empresaService.buscarTodos());
    return "empresa/lista";
  }

  @GetMapping("/cadastrar")
  public String cadastrarEmpresa(Empresa empresa) {
    return "empresa/cadastro";
  }

  @PostMapping("/salvar")
  public String salvar(
      @Valid Empresa empresa,
      BindingResult result,
      RedirectAttributes attr) {
    if (result.hasErrors()) {
      return "empresa/cadastro";
    }

    empresa.setSenha(encoder.encode(empresa.getSenha()));
    empresaService.salvar(empresa);
    attr.addFlashAttribute("success", "empresa.create.success");
    System.out.println(">>> Empresa cadastrada: " + empresa.getId());
    return "redirect:/empresas";
  }

  @GetMapping("/editar/{id}")
  public String editarEmpresa(@PathVariable("id") Long id, ModelMap model) {
    model.addAttribute("empresa", empresaService.buscarPorId(id));
    return "empresa/cadastro";
  }

  @PostMapping("/editar")
  public String editar(
      @Valid Empresa empresa,
      BindingResult result,
      @RequestParam(required = false) String novaSenha,
      ModelMap model,
      RedirectAttributes attr) {
    if (result.hasErrors()) {
      return "empresa/cadastro";
    }

    if (novaSenha != null && !novaSenha.trim().isEmpty()) {
      empresa.setSenha(encoder.encode(novaSenha));
    }

    empresaService.salvar(empresa);
    attr.addFlashAttribute("success", "empresa.edit.success");
    System.out.println(">>> Empresa editada: " + empresa.getId());
    return "redirect:/empresas";
  }

  @GetMapping("/vagas")
  public String listarVagas(ModelMap model) {
    Empresa empresa = this.getEmpresa();
    List<Vaga> vagas = List.of();
    if (empresa != null) {
      vagas = vagaService.buscaPorEmpresaId(empresa.getId());
    }
    model.addAttribute("vagas", vagas);
    return "vaga/list";
  }
}