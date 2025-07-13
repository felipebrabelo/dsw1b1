package br.ufscar.dc.dsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.service.impl.EmpresaService;
import br.ufscar.dc.dsw.service.impl.ProfissionalService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

  @Autowired
  private EmpresaService empresaService;

  @Autowired
  private ProfissionalService profissionalService;

  @Autowired
	private BCryptPasswordEncoder encoder;

  @GetMapping
  public String listUsuarios(@RequestParam String cargo) {
    return "usuario/list";
  }
}