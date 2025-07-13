package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.service.impl.EmpresaService;
import br.ufscar.dc.dsw.service.impl.ProfissionalService;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

  @Autowired
  private EmpresaService empresaService;

  @Autowired
  private ProfissionalService profissionalService;

  @Autowired
	private BCryptPasswordEncoder encoder;

  @GetMapping("/cadastrar")
  public String cadastrarProfissional(ModelMap model) {
    model.addAttribute("type", "PROFISSIONAL");
    model.addAttribute("usuario", new Profissional());
    return "usuario/cadastro";
  }

  @GetMapping("/cadastrar/empresa")
  public String cadastraEmpresa(ModelMap model) {
    model.addAttribute("type", "EMPRESA");
    model.addAttribute("usuario", new Empresa());
    return "usuario/cadastro";
  }

  @PostMapping("/salvar")
  public String salvaProfissional(@Valid Profissional profissional, BindingResult result, RedirectAttributes attr) {
    System.out.println("Salvando profissional: " + profissional);
    if (result.hasErrors()) {
      return "profissional/cadastro";
    }

    profissional.setSenha(encoder.encode(profissional.getSenha()));
    profissionalService.salvar(profissional);

    System.out.println("Profissional salvo: " + profissional);

    attr.addFlashAttribute("success", "usuario.create.success");
    return "redirect:/login";
  }

  @PostMapping("/salvar/empresa")
  public String salvaEmpresa(@Valid Empresa empresa, BindingResult result, RedirectAttributes attr) {
    System.out.println("Salvando empresa: " + empresa);
    if (result.hasErrors()) {
      return "empresa/cadastro";
    }

    empresa.setSenha(encoder.encode(empresa.getSenha()));
    empresaService.salvar(empresa);

    System.out.println("Empresa salva: " + empresa);

    attr.addFlashAttribute("success", "usuario.create.success");
    return "redirect:/login";
  }

  @GetMapping("/usuarios")
  public String listUsuarios(@RequestParam String cargo) {
    return "usuario/list";
  }
}