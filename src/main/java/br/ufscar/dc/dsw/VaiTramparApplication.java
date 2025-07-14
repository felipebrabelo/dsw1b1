package br.ufscar.dc.dsw;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw.dao.ICandidaturaDAO;
import br.ufscar.dc.dsw.dao.IEmpresaDAO;
import br.ufscar.dc.dsw.dao.IProfissionalDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.dao.IVagaDAO;
import br.ufscar.dc.dsw.domain.Candidatura;
import br.ufscar.dc.dsw.domain.Empresa;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.Vaga;

@SpringBootApplication
public class VaiTramparApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaiTramparApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(
			IUsuarioDAO usuarioDAO,
			BCryptPasswordEncoder encoder,
			IEmpresaDAO empresaDAO,
			IVagaDAO vagaDAO,
			IProfissionalDAO profissionalDAO,
			ICandidaturaDAO candidaturaDAO) {
		return (args) -> {
			// --- Criando e Salvando Admin ---
			Usuario admin = new Usuario();
			admin.setEmail("admin@admin.com");
			admin.setSenha(encoder.encode("admin")); // codificar a senha eventualmente
			admin.setNome("Administrador");
			admin.setRole("ROLE_ADMIN");

			usuarioDAO.save(admin);

			// --- Criando e Salvando Empresas ---
			Empresa empresa1 = new Empresa();
			empresa1.setEmail("google@google.com");
			empresa1.setSenha(encoder.encode("google")); // codificar a senha eventaulmente
			empresa1.setCnpj("42.937.026/0001-00");
			empresa1.setNome("Google Brasil");
			empresa1.setDescricao("Uma empresa de tecnologia multinacional.");
			empresa1.setCidade("São Paulo");

			Empresa empresa2 = new Empresa();
			empresa2.setEmail("ifood@ifood.com");
			empresa2.setSenha(encoder.encode("ifood"));
			empresa2.setCnpj("14.231.798/0001-97");
			empresa2.setNome("iFood");
			empresa2.setDescricao("Empresa brasileira de entrega de comida online.");
			empresa2.setCidade("São Carlos");

			// Salva as empresas primeiro para que elas tenham IDs gerados pelo banco
			empresaDAO.saveAll(Arrays.asList(empresa1, empresa2));

			// --- Criando e Salvando Vagas, associadas às empresas ---
			Vaga vaga1 = new Vaga();
			vaga1.setTitulo("Engenheiro de Software Backend");
			vaga1.setDescricao("Desenvolvimento de sistemas backend em Java e Python.");
			vaga1.setRemuneracao(new BigDecimal("9500.00"));
			vaga1.setDataLimiteInscricao(LocalDate.now().plusMonths(1));
			vaga1.setEmpresa(empresa1); // Associa a vaga à Google

			Vaga vaga2 = new Vaga();
			vaga2.setTitulo("Desenvolvedor Java Pleno (remoto)");
			vaga2.setDescricao("Desenvolvimento de aplicações Java em ambiente remoto.");
			vaga2.setRemuneracao(new BigDecimal("7000.00"));
			vaga2.setDataLimiteInscricao(LocalDate.now().plusDays(20));
			vaga2.setEmpresa(empresa2); // Associa a vaga ao iFood

			Vaga vaga3 = new Vaga();
			vaga3.setTitulo("Analista de Dados com foco em Python");
			vaga3.setDescricao("Análise de dados e desenvolvimento de scripts em Python.");
			vaga3.setRemuneracao(new BigDecimal("8000.00"));
			vaga3.setDataLimiteInscricao(LocalDate.now().plusMonths(2));
			vaga3.setEmpresa(empresa1); // Associa a vaga à Google em São Paulo

			Vaga vaga4 = new Vaga();
			vaga4.setTitulo("Estágio em Desenvolvimento de Software");
			vaga4.setDescricao("Oportunidade de estágio para estudantes de Ciência da Computação.");
			vaga4.setRemuneracao(new BigDecimal("2200.00"));
			vaga4.setDataLimiteInscricao(LocalDate.now().plusDays(15));
			vaga4.setEmpresa(empresa2); // Associa a vaga ao iFood em São Carlos

			vagaDAO.saveAll(Arrays.asList(vaga1, vaga2, vaga3, vaga4));

			// --- Criando e Salvando Profissionais ---
			// Profissional 1
			Profissional profissional1 = new Profissional();
			profissional1.setNome("João Silva");
			profissional1.setEmail("joao@gmail.com");
			profissional1.setSenha(encoder.encode("joao"));
			profissional1.setCpf("167.881.330-34");
			profissional1.setTelefone("11987654321");
			profissional1.setSexo("MASCULINO");
			profissional1.setEnabled(true);
			profissional1.setDataNascimento(LocalDate.of(1995, 5, 20));

			profissionalDAO.save(profissional1);

			// --- Criando e Salvando Candidaturas ---
			Candidatura candidatura1 = new Candidatura();
			candidatura1.setProfissional(profissional1);
			candidatura1.setVaga(vaga1);
			candidatura1.setStatus("ENTREVISTA");
			candidatura1.setStatusDescription(
					"Parabéns! Você foi selecionado para a entrevista.");
			candidatura1.setEntrevistaLink("https://meet.google.com/abc-defg-hij");
			candidatura1.setEntrevistaData(LocalDateTime.now().plusDays(3));

			Candidatura candidatura2 = new Candidatura(
					profissional1, vaga2);

			// Salva as candidaturas
			candidaturaDAO.saveAll(Arrays.asList(candidatura1, candidatura2));
		};
	}
}