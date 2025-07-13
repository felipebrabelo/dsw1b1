package br.ufscar.dc.dsw.controller.REST;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Vaga;
import br.ufscar.dc.dsw.security.UsuarioDetails; // Importe sua classe UserDetails
import br.ufscar.dc.dsw.service.spec.IEmpresaService;
import br.ufscar.dc.dsw.service.spec.IVagaService;

@RestController
@RequestMapping("/api/vagas")
public class VagaRestController {

    @Autowired
    private IVagaService vagaService;

    @Autowired
    private IEmpresaService empresaService; // Necessário para o cadastro

    // Retorna a lista de vagas abertas, com filtros opcionais.
    // Requisito R4.
    @GetMapping
    public ResponseEntity<List<Vaga>> listarVagasAbertas(
            @RequestParam(name = "cargo", required = false) String cargo,
            @RequestParam(name = "cidade", required = false) String cidade) {

        List<Vaga> vagas = vagaService.buscarVagasAbertasComFiltros(cargo, cidade);

        if (vagas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vagas);
    }

    // REST API -- Retorna a vaga de id = {id} [Read - CRUD]
    @GetMapping("/{id}")
    public ResponseEntity<Vaga> buscarPorId(@PathVariable Long id) {
        Vaga vaga = vagaService.buscarPorId(id);
        if (vaga == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vaga);
    }

    // Retorna a lista de todas as vagas de uma empresa específica.
    // Requisito R6.
    @GetMapping("/empresas/{Id}")
    public ResponseEntity<List<Vaga>> buscarVagasPorEmpresa(@PathVariable Long id) {
        List<Vaga> vagas = vagaService.buscarAbertasDaEmpresa(id); //
        if (vagas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vagas);
    }

    // Cadastra uma nova vaga para a empresa autenticada.
    // Requisito R3.
    @PostMapping
    public ResponseEntity<Vaga> cadastrar(@RequestBody Vaga vaga) {
        // Pega o usuário logado a partir do contexto de segurança
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();

        // Busca a entidade Empresa correspondente ao usuário logado
        Empresa empresaLogada = empresaService.buscarPorId(userDetails.getId());
        if (empresaLogada == null) {
            // Isso não deveria acontecer se o token for válido, mas é uma boa verificação
            return ResponseEntity.status(403).build(); // Forbidden
        }

        // Associa a vaga à empresa correta e salva
        vaga.setEmpresa(empresaLogada);
        vagaService.salvar(vaga);

        return ResponseEntity.ok(vaga);
    }

    // Os métodos de PUT e DELETE devem, similarmente, verificar se a empresa que está tentando modificar/deletar a vaga é a dona da vaga.
    // TO-DO
    @PutMapping("/{id}")
    public ResponseEntity<Vaga> atualizar(@PathVariable Long id, @RequestBody Vaga vaga) {
        // TODO: Adicionar lógica de segurança para garantir que apenas o dono da vaga
        // pode editar.
        Vaga vagaExistente = vagaService.buscarPorId(id);
        if (vagaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        vaga.setId(id);
        vagaService.salvar(vaga);
        return ResponseEntity.ok(vaga);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        // TODO: Adicionar lógica de segurança para garantir que apenas o dono da vaga
        // pode excluir.
        Vaga vaga = vagaService.buscarPorId(id);
        if (vaga == null) {
            return ResponseEntity.notFound().build();
        }
        vagaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}