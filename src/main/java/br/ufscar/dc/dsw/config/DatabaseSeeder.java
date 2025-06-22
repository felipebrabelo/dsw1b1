package br.ufscar.dc.dsw.config;

import br.ufscar.dc.dsw.model.Empresa;
import br.ufscar.dc.dsw.model.Vaga;
import br.ufscar.dc.dsw.repository.EmpresaRepository;
import br.ufscar.dc.dsw.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements ApplicationRunner {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Verifica se o banco de dados já foi populado para evitar duplicação de dados
        if (empresaRepository.count() == 0 && vagaRepository.count() == 0) {
            System.out.println("Populando o banco de dados com dados de teste...");
            populateDatabase();
            System.out.println("Banco de dados populado.");
        } else {
            System.out.println("O banco de dados já contém dados.");
        } 
    }


    private void populateDatabase() {
        // --- Criando e Salvando Empresas ---
        Empresa empresa1 = new Empresa();
        empresa1.setEmail("contato@google.com");
        empresa1.setSenha("123456"); // codificar a senha eventaulmente
        empresa1.setCnpj("06.990.590/0001-23");
        empresa1.setNome("Google Brasil");
        empresa1.setDescricao("Uma empresa de tecnologia multinacional.");
        empresa1.setCidade("São Paulo");

        Empresa empresa2 = new Empresa();
        empresa2.setEmail("rh@ifood.com.br");
        empresa2.setSenha("123456");
        empresa2.setCnpj("11.461.851/0001-90");
        empresa2.setNome("iFood");
        empresa2.setDescricao("Empresa brasileira de entrega de comida online.");
        empresa2.setCidade("São Carlos");

        // Salva as empresas primeiro para que elas tenham IDs gerados pelo banco
        empresaRepository.saveAll(Arrays.asList(empresa1, empresa2));

        // --- Criando e Salvando Vagas, associadas às empresas ---
        Vaga vaga1 = new Vaga();
        vaga1.setDescricao("Engenheiro de Software Backend");
        vaga1.setRemuneracao(new BigDecimal("9500.00"));
        vaga1.setDataLimiteInscricao(LocalDate.now().plusMonths(1));
        vaga1.setEmpresa(empresa1); // Associa a vaga à Google

        Vaga vaga2 = new Vaga();
        vaga2.setDescricao("Desenvolvedor Java Pleno (remoto)");
        vaga2.setRemuneracao(new BigDecimal("7000.00"));
        vaga2.setDataLimiteInscricao(LocalDate.now().plusDays(20));
        vaga2.setEmpresa(empresa2); // Associa a vaga ao iFood

        Vaga vaga3 = new Vaga();
        vaga3.setDescricao("Analista de Dados com foco em Python");
        vaga3.setRemuneracao(new BigDecimal("8000.00"));
        vaga3.setDataLimiteInscricao(LocalDate.now().plusMonths(2));
        vaga3.setEmpresa(empresa1); // Associa a vaga à Google em São Paulo

        Vaga vaga4 = new Vaga();
        vaga4.setDescricao("Estágio em Desenvolvimento de Software");
        vaga4.setRemuneracao(new BigDecimal("2200.00"));
        vaga4.setDataLimiteInscricao(LocalDate.now().plusDays(15));
        vaga4.setEmpresa(empresa2); // Associa a vaga ao iFood em São Carlos
        
        vagaRepository.saveAll(Arrays.asList(vaga1, vaga2, vaga3, vaga4));
    }
}