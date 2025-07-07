package br.ufscar.dc.dsw.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.service.impl.CandidaturaService;
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
  public String cadastrarProfissional(Profissional profissional, ModelMap model) {
    model.addAttribute("type", "PROFISSIONAL");
    return "usuario/cadastro";
  }

  @GetMapping("/cadastrar-empresa")
  public String cadastraEmpresa(Empresa empresa, ModelMap model) {
    model.addAttribute("action", "/salvar-empresa");
    return "empresa/cadastro";
  }

  @PostMapping("/salvar")
  public String salvaEmpresa(@Valid Profissional profissional, BindingResult result, RedirectAttributes attr) {
    if (result.hasErrors()) {
      return "profissional/cadastro";
    }

    profissional.setSenha(encoder.encode(profissional.getSenha()));
    profissionalService.salvar(profissional);
    attr.addFlashAttribute("success", "Usuario criado com sucesso");
    return "redirect:/login";
  }

  @GetMapping("/usuarios")
  public String listUsuarios(@RequestParam String cargo) {
    return "usuario/list";
  }

  @GetMapping("/usuarios/{id}")
  public String detailUsuario(@PathVariable("id") Long id, ModelMap model) {
    return "usuario/details";
  }
}
