# Vai Trampar
Atividade AA-1: Sistema para oferta de vagas de estágios/empregos

## Sistema de Vagas de Estágio e Emprego
Este documento descreve o projeto de um sistema para oferta e candidatura de vagas de estágio e emprego, desenvolvido como parte da disciplina de Desenvolvimento de Software para a Web 1 da UFSCar.

## Sobre o Projeto
O objetivo é criar uma plataforma web que conecte profissionais em busca de oportunidades com empresas que desejam contratar. O sistema permitirá que empresas publiquem vagas e gerenciem candidaturas, e que profissionais busquem vagas e se candidatem a elas. A plataforma contará com três perfis de usuário: Administrador, Empresa e Profissional.

## Funcionalidades Principais
### Gerais
- [ ] Internacionalização: O sistema deve suportar pelo menos dois idiomas (ex: Português e Inglês).
- [ ] Validação de Dados: Todas as informações submetidas através de formulários devem ser validadas em termos de formato, tamanho, etc.
- [ ] Tratamento de Erros: Erros como cadastros duplicados ou falhas técnicas devem ser tratados, exibindo uma página de erro amigável e registrando o erro no console do servidor.
### Funcionalidades do Administrador
- [ ] Gerenciamento de Profissionais (CRUD): O administrador pode criar, ler, atualizar e deletar os cadastros de profissionais.
- [ ] Gerenciamento de Empresas (CRUD): O administrador pode criar, ler, atualizar e deletar os cadastros de empresas.
- [ ] Acesso: O acesso do administrador requer um login e senha que devem ser populados no banco de dados na inicialização do sistema.
### Funcionalidades da Empresa
- [ ] Cadastro e Login: Empresas podem se cadastrar com e-mail, senha, CNPJ, nome, descrição e cidade.
- [ ] Cadastro de Vagas: Após o login, a empresa pode cadastrar vagas de estágio/trabalho. O cadastro da vaga inclui CNPJ, descrição do perfil, habilidades, remuneração e data limite para inscrição.

- [ ] Listagem de Vagas Próprias: Após o login, a empresa pode visualizar todas as suas vagas cadastradas.
- [ ] Análise de Candidatos: Ao final do período de inscrição de uma vaga , a empresa pode analisar os currículos dos candidatos e atualizar o status da candidatura para "NÃO SELECIONADO" ou "ENTREVISTA".

- [ ] Comunicação com Candidatos: O sistema deve notificar os candidatos por e-mail sobre a mudança de status. Para o status "ENTREVISTA", a empresa deve informar o horário e o link da videoconferência na notificação.

### Funcionalidades do Profissional
- [ ] Cadastro e Login: Profissionais podem se cadastrar com e-mail, senha, CPF, nome, telefone, sexo e data de nascimento.
- [ ] Listagem de Vagas: Qualquer visitante (mesmo sem login) pode ver a lista de vagas abertas e filtrá-las por cidade.
- [ ] Candidatura a Vagas: Após o login, o profissional pode se candidatar a uma vaga , enviando suas qualificações (por exemplo, via upload de currículo em PDF). Só é permitida uma candidatura por vaga para cada profissional.


- [ ] Acompanhamento de Candidaturas: Após o login, o profissional pode ver a lista de todas as suas candidaturas e seus respectivos status: 
ABERTO: A inscrição ainda está dentro do prazo ou em análise pela empresa.
NÃO SELECIONADO: O perfil do profissional não foi considerado adequado para a vaga pela empresa.
ENTREVISTA: O profissional foi pré-selecionado para uma entrevista.

## Arquitetura e Tecnologias
### Arquitetura
O projeto segue o padrão de arquitetura Modelo-Visão-Controlador (MVC).

#### Tecnologias Utilizadas
##### Back-end (Lado Servidor):
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
##### Front-end (Lado Cliente):
- Javascript
- CSS

## Ambiente de Desenvolvimento:
- Build e Deploy: Obrigatoriamente com *Apache Maven*.
- Controle de Versão: Obrigatoriamente com Git e hospedado em um [repositório *GitHub*](https://github.com/JoaoVitorAzevedo/iWork).

# Como Executar o Projeto
Siga os passos abaixo para configurar e executar o ambiente de desenvolvimento local.

## Pré-requisitos
- Java Development Kit (JDK) 11 ou superior
- Apache Maven
- Git

## Passos para Instalação
Clone o repositório:
```Bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

## Configuração do Banco de Dados:
Este projeto não envia o arquivo de configuração do banco de dados (application.properties) para o repositório por razões de segurança.

Navegue até src/main/resources/.
1. Crie uma cópia do arquivo application.properties.template e renomeie-a para application.properties.
2. Edite o application.properties com as suas credenciais de acesso ao banco de dados local.
3. Compile o projeto com o Maven:
Execute o comando a seguir na raiz do projeto para baixar as dependências e compilar o código.

```Bash
mvn clean install
```

Execute a aplicação:
Após a compilação bem-sucedida, inicie a aplicação com o seguinte comando:

```Bash
mvn spring-boot:run
```
A aplicação estará disponível em http://localhost:8080.

## Equipe
[João Vitor Azevedo](https://github.com/JoaoVitorAzevedo)
Felipe
Fábricio
