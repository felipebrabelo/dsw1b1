package br.ufscar.dc.dsw.controller.REST;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.service.spec.IEmpresaService;

@RestController
@RequestMapping("/api/empresas") // Define o caminho base para os endpoints REST
public class EmpresaRestController {

    @Autowired // 
    private IEmpresaService empresaService;

    // Retorna a lista de empresas [Read - CRUD] ou 204 No Content se não houver
    // nenhuma.
    @GetMapping
    public ResponseEntity<List<Empresa>> listarTodas() {
        List<Empresa> empresas = empresaService.buscarTodos();
        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empresas);
    }

    // Retorna a empresa de id = {id} [Read - CRUD] - Retorna a empresa encontrada
    // ou 404 Not Found.
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        Empresa empresa = empresaService.buscarPorId(id);
        if (empresa == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(empresa);
    }

    // Cria uma nova empresa [Create - CRUD]
    // Recebe os dados da empresa no corpo da requisição e retorna a empresa criada.
    @PostMapping
    public ResponseEntity<Empresa> cadastrar(@RequestBody Empresa empresa) {
        // Idealmente, a senha já viria criptografada do frontend
        // ou seria tratada no service. Por simplicidade, salvamos como está.
        empresaService.salvar(empresa);
        return ResponseEntity.ok(empresa);
    }

    // Atualiza a empresa de id = {id} [Update - CRUD]
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @RequestBody Empresa empresa) {
        Empresa empresaExistente = empresaService.buscarPorId(id);
        if (empresaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        // Garante que estamos atualizando a empresa com o ID correto
        empresa.setId(id);
        empresaService.salvar(empresa);
        return ResponseEntity.ok(empresa);
    }

    // Remove a empresa de id = {id} [Delete - CRUD  -> Retorna 204 No Content em caso
    // de sucesso ou 404 Not Found se a empresa não existir.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        Empresa empresaExistente = empresaService.buscarPorId(id);
        if (empresaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        empresaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    //Retorna a lista de todas as empresas da cidade de nome = {nome}
    @GetMapping("/cidades/{nome}")
    public ResponseEntity<List<Empresa>> buscarPorCidade(@PathVariable String nome) {
        System.out.println("Buscando empresas na cidade: " + nome);
        List<Empresa> empresas = empresaService.buscarPorCidade(nome);
        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(empresas);
    }
}

