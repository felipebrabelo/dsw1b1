package br.ufscar.dc.dsw.controller.REST;

import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.service.spec.IProfissionalService;
import jakarta.validation.Valid;

import org.hibernate.Remove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalRestController {

	@Autowired
	private IProfissionalService profissionalService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// Endpoint PÚBLICO para um novo profissional se cadastrar.
	// Cria um novo profissional [Create - CRUD]
	@PostMapping
	public ResponseEntity<Profissional> cadastrarProfissional(@Valid @RequestBody Profissional profissional) {
		// Criptografa a senha antes de salvar
		profissional.setSenha(passwordEncoder.encode(profissional.getSenha()));
		// Definir o perfil e ativar o usuário
		profissional.setRole("ROLE_PROFISSIONAL");
		profissional.setEnabled(true);

		profissionalService.salvar(profissional);

		// Retorna 200 OK com o objeto criado (sem a senha, graças ao @JsonIgnore)
		return ResponseEntity.ok(profissional);
	}

	// para admin: Retorna a lista de profissionais [Read - CRUD]
	@GetMapping
	public ResponseEntity<List<Profissional>> listarTodos() {
		List<Profissional> profissionais = profissionalService.buscarTodos();
		if (profissionais.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(profissionais);
	}

	// para ADMIN para buscar um profissional por ID.
	// Atende parte do Requisito R1.
	// Retorna o profissional de id = {id} [Read - CRUD]
	@GetMapping("/{id}")
	public ResponseEntity<Profissional> buscarPorId(@PathVariable Long id) {
		Profissional profissional = profissionalService.buscarPorId(id);
		if (profissional == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(profissional);
	}

	// para ADMIN atualizar um profissional.
	// Atende parte do Requisito R1.
	// Atualiza o profissional de id = {id} [Update - CRUD]
	@PutMapping("/{id}")
	public ResponseEntity<Profissional> atualizar(@PathVariable Long id,
			@Valid @RequestBody Profissional profissionalDados) {
		Profissional profissionalExistente = profissionalService.buscarPorId(id);
		if (profissionalExistente == null) {
			return ResponseEntity.notFound().build();
		}

		// Atualiza os dados do profissional existente com os novos dados
		// (assume que o corpo do JSON não contém ID, ou o ignora)
		profissionalExistente.setNome(profissionalDados.getNome());
		profissionalExistente.setCpf(profissionalDados.getCpf());
		profissionalExistente.setEmail(profissionalDados.getEmail());

		// Se a senha for fornecida, criptografa e atualiza (checar isso)
		if (profissionalDados.getSenha() != null && !profissionalDados.getSenha().isEmpty()) {
			// Criptografa a nova senha se fornecida
			profissionalExistente.setSenha(passwordEncoder.encode(profissionalDados.getSenha()));
		}

		profissionalExistente.setTelefone(profissionalDados.getTelefone());
		profissionalExistente.setDataNascimento(profissionalDados.getDataNascimento());
		profissionalExistente.setSexo(profissionalDados.getSexo());

		profissionalService.salvar(profissionalExistente);

		return ResponseEntity.ok(profissionalExistente);
	}

	// para ADMIN para remover um profissional.
	// Atende parte do Requisito R1.
	// Remove o profissional de id = {id} [Delete - CRUD]
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) { // O método DELETE foi corrigido para retornar
																	// ResponseEntity<Void> com o status 204 No Content,
																	// que é a prática padrão para uma exclusão
																	// bem-sucedida.
		if (profissionalService.buscarPorId(id) == null) {
			return ResponseEntity.notFound().build();
		}
		profissionalService.excluir(id);
		// Retorna 204 No Content, indicando que a remoção foi bem-sucedida
		return ResponseEntity.noContent().build();
	}
}