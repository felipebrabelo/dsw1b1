package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.impl.CandidaturaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/candidaturas")
public class CandidaturaController {

  @Autowired
  private CandidaturaService candidaturaService;

  @Value("${upload.directory}")
  private String uploadDir;

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
  public String cadastrar(@RequestParam("curriculoFile") MultipartFile curriculoFile, @Valid Candidatura candidatura,
      BindingResult result, RedirectAttributes attr) {
    if (curriculoFile.isEmpty()) {
      attr.addFlashAttribute("fail", "candidatura.curriculo.required");
      return "redirect:/vagas/detalhes/" + candidatura.getVaga().getId();
    }

    if (result.hasErrors()) {
      attr.addFlashAttribute("mensagem", "Erro ao cadastrar candidatura.");
      return "redirect:/vagas/detalhes/" + candidatura.getVaga().getId();
    }
    candidatura.setId(null);

    if (curriculoFile.isEmpty() || curriculoFile.getContentType() == null) {
      result.rejectValue("curriculoPath", "candidatura.curriculo.required");

      return "redirect:/vagas/detalhes/" + candidatura.getVaga().getId();
    }

    if (!curriculoFile.getContentType().equalsIgnoreCase("application/pdf")) {
      result.rejectValue("curriculoPath", "candidatura.curriculo.invalid");
      return "redirect:/vagas/detalhes/" + candidatura.getVaga().getId();
    }

    try {
      salvaCurriculo(curriculoFile, candidatura);

      candidaturaService.salvar(candidatura);

      attr.addFlashAttribute("success", "vaga.candidatura.success");

      return "redirect:/candidaturas";

    } catch (Exception e) {
      result.rejectValue("curriculoPath", "candidatura.curriculo.invalid");
      attr.addFlashAttribute("mensagem", "Erro ao salvar o currículo.");
      return "redirect:/vagas/detalhes/" + candidatura.getVaga().getId();
    }
  }

  @GetMapping
  public String listar(ModelMap model) {
    Usuario usuario = this.getUsuario();
    List<Candidatura> candidaturas = null;

    if (usuario.getRole().equals("ROLE_ADMIN")) {
      candidaturas = candidaturaService.buscarTodos();
    } else {
      candidaturas = candidaturaService.buscarPorProfissional(this.getProfissional());
    }

    model.addAttribute("candidaturas", candidaturas);

    return "candidatura/lista";
  }

  @GetMapping("/{id}")
  public String detalhes(@PathVariable Long id, ModelMap model, RedirectAttributes attr) {
    Candidatura candidatura = candidaturaService.buscarPorId(id);
    if (candidatura == null) {
      model.addAttribute("mensagem", "Candidatura não encontrada.");
      return "redirect:/";
    }
    Vaga vaga = candidatura.getVaga();

    // Verifica se o usuário é dono da candidatura ou se é um administrador.
    if (!isOwner(vaga)) {
      return "redirect:/";
    }

    Boolean readOnly = !candidatura.getStatus().equals("ABERTA");
    Boolean subscriptionFinished = vaga.getDataLimiteInscricao().isBefore(LocalDate.now());
    model.addAttribute("subscriptionFinished", subscriptionFinished);
    model.addAttribute("readOnly", readOnly);
    model.addAttribute("candidatura", candidatura);
    return "candidatura/detalhes";
  }

  @PostMapping("/atualizar-status")
  public String atualizarStatus(@Valid Candidatura candidatura, BindingResult result, RedirectAttributes attr) {
    System.out.println(">>> Atualizando status da candidatura: " + candidatura.getId());
    if (result.hasErrors()) {
      System.out.println(">>> Erros ao atualizar status: " + result.getAllErrors());
      attr.addFlashAttribute("fail", "candidatura.update.status.fail");
      return "redirect:/candidaturas/" + candidatura.getId();
    }

    candidaturaService.salvar(candidatura);
    attr.addFlashAttribute("success", "candidatura.update.status.success");
    return "redirect:/candidaturas/" + candidatura.getId();
  }

  @GetMapping("/curriculo/{id}")
  public ResponseEntity<Resource> downloadCurriculo(@PathVariable Long id) {
    Candidatura candidatura = candidaturaService.buscarPorId(id);
    if (candidatura == null || candidatura.getCurriculoPath() == null) {
      return ResponseEntity.notFound().build();
    }

    Path curriculoPath = Path.of(candidatura.getCurriculoPath());
    if (!Files.exists(curriculoPath)) {
      return ResponseEntity.notFound().build();
    }

    Resource resource;
    try {
      resource = new UrlResource(curriculoPath.toUri());
    } catch (MalformedURLException e) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=\"" + curriculoPath.getFileName() + "\"")
        .body(resource);
  }

  private void salvaCurriculo(MultipartFile curriculoFile, Candidatura candidatura) throws IOException {
    String fileName = "curriculo_" + candidatura.getProfissional().getId() + "_" + candidatura.getVaga().getId()
      + "_" + System.currentTimeMillis() + ".pdf";

    Path filePath = Path.of(uploadDir).resolve(fileName);
    Files.createDirectories(filePath.getParent());
    Files.copy(curriculoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    candidatura.setCurriculoPath(filePath.toString());
  }

  private boolean isOwner(Vaga vaga) {
    Usuario usuario = this.getUsuario();
    if (usuario.getRole().equals("ROLE_ADMIN")) {
      return true;
    }
    if (vaga.getEmpresa().getId().equals(usuario.getId())) {
      return true;
    }
    return false;
  }
}