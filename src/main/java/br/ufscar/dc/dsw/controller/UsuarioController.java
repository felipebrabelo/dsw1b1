package br.ufscar.dc.dsw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

  @GetMapping
  public String listUsuarios(@RequestParam String cargo) {
    return "usuario/list";
  }

  @GetMapping("/{id}")
  public String detailUsuario(@PathVariable("id") Long id, ModelMap model) {
    return "usuario/details";
  }

  @GetMapping("/{id}/vagas")
  public String listVagasUsuario(@PathVariable("id") Long id, ModelMap model) {
    model.addAttribute("id", id);
    return "usuario/vagas";
  }
}
