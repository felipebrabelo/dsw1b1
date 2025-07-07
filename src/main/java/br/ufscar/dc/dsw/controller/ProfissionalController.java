package br.ufscar.dc.dsw.controller;

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

import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/profissionais")
public class ProfissionalController {
  @Autowired
  private IProfissionalService profissionalService;

  @Autowired
	private BCryptPasswordEncoder encoder;

  @GetMapping
  public String listarProfissionais(ModelMap model) {
    model.addAttribute("profissionais", profissionalService.buscarTodos());
    return "profissional/lista";
  }

  @GetMapping("/cadastrar")
  public String cadastrarProfissional(Profissional profissional) {
    return "profissional/cadastro";
  }

  @PostMapping("/salvar")
  public String salvar(@Valid Profissional profissional, BindingResult result, RedirectAttributes attr) {
    if (result.hasErrors()) {
      return "profissional/cadastro";
    }

    profissional.setSenha(encoder.encode(profissional.getSenha()));
    profissionalService.salvar(profissional);
    attr.addFlashAttribute("success", "usuario.create.success");
    System.out.println(">>> Profissional cadastrado: " + profissional.getId());
    return "redirect:/profissionais";
  }

  @GetMapping("/editar/{id}")
  public String editarProfissional(@PathVariable("id") Long id, ModelMap model) {
    model.addAttribute("profissional", profissionalService.buscarPorId(id));
    return "profissional/cadastro";
  }

  @PostMapping("/editar")
  public String editar(
      @Valid Profissional profissional,
      BindingResult result,
      @RequestParam(required = false) String novaSenha,
      ModelMap model,
      RedirectAttributes attr) {
    System.out.println(">>> Editando profissional: " + profissional.getId());
    if (result.hasErrors()) {
      return "profissional/cadastro";
    }

    if (novaSenha != null && !novaSenha.trim().isEmpty()) {
      profissional.setSenha(encoder.encode(novaSenha));
    } else {
      System.out.println("Senha não foi editada");
    }

    profissionalService.salvar(profissional);
    attr.addFlashAttribute("success", "usuario.edit.success");
    return "redirect:/profissionais";
  }

  @PostMapping("/excluir/{id}")
  public String excluirProfissional(@PathVariable("id") Long id, RedirectAttributes attr){
    profissionalService.excluir(id);
    attr.addFlashAttribute("success", "usuario.delete.success");
    System.out.println(">>> Profissional excluído: " + id);
    return "redirect:/profissionais";
  }
}
