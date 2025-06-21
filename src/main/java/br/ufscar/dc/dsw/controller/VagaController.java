package br.ufscar.dc.dsw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/vagas")
public class VagaController {

  @GetMapping
  public String listVagas(@RequestParam String cargo, @RequestParam String local, ModelMap model) {
    model.addAttribute("cargo", cargo);
    model.addAttribute("local", local);
    return "vaga/list";
  }

  @GetMapping("/{id}")
  public String detailsVaga(@PathVariable("id") Long id, ModelMap model) {
    return "vaga/details";
  }
}
