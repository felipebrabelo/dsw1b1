# Sistema VaiTrampar - Oferta de Vagas de Estágio e Emprego

Este projeto é um sistema web para oferta de vagas de estágio e emprego, desenvolvido para a disciplina de Desenvolvimento de Software para a Web 1. O objetivo é criar uma plataforma que conecte profissionais em busca de oportunidades com empresas que desejam contratar, implementando funcionalidades de cadastro, listagem e gerenciamento através de uma aplicação web e uma API REST.

## Requisitos Implementados

Abaixo está o status de implementação dos requisitos funcionais, baseados nos documentos de especificação (`Requisitos-B1.pdf` e `Requisitos-B2.pdf`).


### Funcionalidades da Aplicação (AA-1)
- [x] **(R1 & R2) Gerenciamento via API:** O CRUD de Profissionais e Empresas está implementado via os endpoints REST.
- [~] **(R3) Cadastro de Vagas:** A API (`POST /api/vagas`) para uma empresa logada cadastrar vagas está implementada. O fluxo de login e o formulário web precisam ser conectados.
- [x] **(R4) Listagem Pública de Vagas:** A listagem de vagas abertas com filtro por cidade está funcional através da página web (`/vagas`) e da API (`GET /api/vagas`).
- [x] **(R5, R7) Fluxo de Candidatura:** A candidatura de um profissional a uma vaga e o acompanhamento do status ainda não foram implementados.
- [x] **(R6) Listagem de Vagas por Empresa:** A funcionalidade está implementada via o endpoint `GET /api/vagas/empresas/{id}`.
- [x] **(R8) Análise de Candidatos:** Upload de currículo implementado e pode ser baixado pela empresa
- [x] **(R9) Internacionalização:** O sistema ainda não foi internacionalizado.
- [x] **(R10) Validação e Tratamento de Erros:** A validação de dados (`@Valid`) e o tratamento de erros de servidor (`ErrorViewController`) estão implementados.


### Funcionalidades da API REST (AA-2)
- [x] **API de Empresas:** CRUD completo para empresas e busca por cidade.
  - `POST /api/empresas`
  - `GET /api/empresas`
  - `GET /api/empresas/{id}`
  - `GET /api/empresas/cidades/{nome}`
  - `PUT /api/empresas/{id}`
  - `DELETE /api/empresas/{id}`
- [x] **API de Profissionais:** CRUD completo para profissionais.
  - `POST /api/profissionais` (Cadastro Público)
  - `GET /api/profissionais`
  - `GET /api/profissionais/{id}`
  - `PUT /api/profissionais/{id}`
  - `DELETE /api/profissionais/{id}`
- [x] **API de Vagas:** Listagem e consulta de vagas.
  - `GET /api/vagas` (com filtros por cargo e cidade)
  - `GET /api/vagas/{id}`
  - `GET /api/vagas/empresas/{id}`



## Arquitetura e Tecnologias

O projeto segue o padrão de arquitetura **Modelo-Visão-Controlador (MVC)**.

- **Backend:** Java, Spring Boot, Spring MVC, Spring Data JPA, Spring Security
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript, Bootstrap
- **Banco de Dados:** SQLite (arquivo `vaitrampar_db.db` na raiz do projeto)
- **Build e Dependências:** Maven

### Principais Diretórios



.
└── src/

    ├── main/
    │   ├── java/br/ufscar/dc/dsw/
    │   │   ├── domain/      # Entidades JPA (Empresa, Vaga, etc.)
    │   │   ├── controller/  # Controllers MVC e REST
    │   │   ├── service/     # Camada de Serviços
    │   │   ├── dao/         # Repositórios JPA
    │   │   └── config/      # Configurações (Security)
    │   └── resources/
    │       ├── templates/   # Templates Thymeleaf
    │       ├── static/      # Arquivos estáticos (CSS, JS)
    │       └── application.properties
    └── test/


## Como Executar o Projeto

#### Pré-requisitos
- Java Development Kit (JDK) 17 ou superior
- Apache Maven

#### Passos
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/felipebrabelo/dsw1b1](https://github.com/felipebrabelo/dsw1b1)
    cd dsw1b1
    ```
2. **Configuração do Banco de Dados**:
Este projeto não envia o arquivo de configuração do banco de dados (application.properties) para o repositório por razões de segurança.

Navegue até src/main/resources/.
1. Crie uma cópia do arquivo application.properties.template e renomeie-a para application.properties.
2. Edite o application.properties com as suas credenciais de acesso ao banco de dados local.
3. Compile o projeto com o Maven:
Execute o comando a seguir na raiz do projeto para baixar as dependências e compilar o código.

3.  **Compile o projeto com o Maven:**
    Execute o comando a seguir na raiz do projeto para baixar as dependências e compilar o código.
    ```bash
    ./mvn clean install
    ```

4.  **Execute a aplicação:**
    Após a compilação bem-sucedida, inicie a aplicação com o seguinte comando:
    ```bash
    ./mvn spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080`.

## Equipe
- [João Vitor Azevedo](https://github.com/JoaoVitorAzevedo)
- [Fábricio Rodrigues](https://github.com/devfabri)

### Divisão de Tarefas

## João
-  API's
    - Profissionais
    - Empresas
    - Vagas
    - Testes no Postman
- /domain
    - Entidades JPA(Vaga, Empresa, Profissional) exceto Candidatura
- /dao
    - Repositórios JPA
- /service - Interfaces, exceto ICandidaturaService


## Fabrício
- Validação de Dados
- Internacionalização
- Controllers do site
- Lógica de Candidatura à uma vaga
- Upload de Currículo
- Implementação das paginas com thymeleaf

## Licença
Ver arquivo `LICENSE`
