# Projeto-Barbearia-PH

 💈 Projeto Mensal 01 - Sistema de Agendamento para Barbearia

Curso: Análise e Desenvolvimento de Sistemas

Semestre: 4º semestre

.

🔍 Descrição

Este projeto consiste em um sistema completo de agendamento para barbearias, desenvolvido com foco em organização, praticidade e controle de serviços. O sistema permite que clientes agendem horários com profissionais, associando serviços específicos, tudo de forma simples e eficiente.

.

🧩 Funcionalidades

👤 Cadastro de clientes e profissionais

💼 Cadastro de serviços (ex: corte de cabelo, barba, etc.)

🔗 Associação entre profissionais e serviços com preços individuais

🗓️ Agendamento de horários com local, data e profissional

📑 Listagem, edição e exclusão de agendamentos

🔐 Controle de dados por meio de API

.

⚙️ Tecnologias

Spring Boot 3.4.1: Backend estruturado com API RESTful

Java 17: Programação orientada a objetos

Spring Data JPA: Persistência e relacionamentos

Bean Validation: Validações de entrada

MySQL: Banco de dados relacional

Lombok: Redução de código boilerplate

Postman: Testes de requisições HTTP

IntelliJ IDEA: IDE utilizada no desenvolvimento

Maven: Gerenciamento de dependências e build

.

🛠 Requisitos Funcionais

✅ API com endpoints CRUD (Create, Read, Update, Delete)

✅ Associação entre entidades via relacionamento JPA

✅ Requisições testadas e documentadas via Postman

✅ Persistência de dados no MySQL

✅ Validações com Bean Validation (@NotBlank, @NotNull)

✅ Uso de anotações Spring (@RestController, @Entity, @Repository, @Service)

✅ Tratamento de exceções personalizadas

✅ Relacionamentos 1-N mapeados corretamente

.

🛠 Requisitos para rodar o projeto

✅ Instalar o Java 17

✅ Instalar o MySQL

✅ Instalar o Maven

✅ Instalar o IntelliJ IDEA

✅ Instalar o Postman

.

⚙️ Configuração do MySQL

Crie um banco de dados com o nome: barbearia

Edite o arquivo application.properties com seu usuário e senha do MySQL:

spring.datasource.url=jdbc:mysql://localhost:3306/barbearia

spring.datasource.username=SEU_USUARIO

spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

.

▶️ Como executar o projeto

Clone o repositório:

git clone https://github.com/SEU_USUARIO/barbearia-springboot.git


Abra o projeto no IntelliJ IDEA

Configure o MySQL no application.properties

Rode o projeto pela classe BarbeariaApplication.java

Use o Postman para fazer requisições nos endpoints:

.

Clientes

| Método   | Endpoint                                                        | Descrição                |
| -------- | --------------------------------------------------------------- | ------------------------ |
| `POST`   | `/api/clientes/save`                                            | Criar um novo cliente    |
| `GET`    | `/api/clientes/listar`                                          | Listar todos os clientes |
| `GET`    | `/api/clientes/{id}`                                            | Buscar cliente por ID    |
| `PUT`    | `/api/clientes/{id}`                                            | Atualizar cliente por ID |
| `DELETE` | `/api/clientes/{id}`                                            | Deletar cliente por ID   |
| `GET`    | `/api/clientes/buscar/nome?nome=João`                           | Buscar por nome          |
| `GET`    | `/api/clientes/buscar/celular?celular=999999999`                | Buscar por celular       |
| `GET`    | `/api/clientes/buscar/nome-completo?nomeCompleto=João da Silva` | Buscar por nome completo |

.

Profissionais

| Método   | Endpoint                                                           | Descrição                                                               |
| -------- | ------------------------------------------------------------------ | ----------------------------------------------------------------------- |
| `POST`   | `/api/profissionais/save`                                          | Criar um novo profissional                                              |
| `GET`    | `/api/profissionais/listar`                                        | Listar todos os profissionais                                           |
| `GET`    | `/api/profissionais/{id}`                                          | Buscar profissional por ID                                              |
| `PUT`    | `/api/profissionais/{id}`                                          | Atualizar profissional por ID                                           |
| `DELETE` | `/api/profissionais/{id}`                                          | Deletar profissional por ID                                             |
| `GET`    | `/api/profissionais/buscar/nome?nome=Lucas`                        | Buscar por nome                                                         |
| `GET`    | `/api/profissionais/buscar/especializacao?especializacao=BARBEIRO` | Buscar por especialização (ex: BARBEIRO, CABELEIREIRO, COLORISTA, etc.) |

.

Serviços

| Método   | Endpoint                                                         | Descrição                   |
| -------- | ---------------------------------------------------------------- | --------------------------- |
| `POST`   | `/api/servicos/save`                                             | Criar um novo serviço       |
| `GET`    | `/api/servicos/listar`                                           | Listar todos os serviços    |
| `GET`    | `/api/servicos/{id}`                                             | Buscar serviço por ID       |
| `PUT`    | `/api/servicos/{id}`                                             | Atualizar serviço por ID    |
| `DELETE` | `/api/servicos/{id}`                                             | Deletar serviço por ID      |
| `GET`    | `/api/servicos/buscar/descricao?descricao=Corte`                 | Buscar por descrição        |
| `GET`    | `/api/servicos/buscar/duracao-maxima?duracao=30`                 | Buscar por duração máxima   |
| `GET`    | `/api/servicos/buscar/duracao-range?duracaoMin=15&duracaoMax=45` | Buscar por faixa de duração |

.

Profissional e Serviço

| Método | Endpoint                           | Descrição                                    |
| ------ | ---------------------------------- | -------------------------------------------- |
| `POST` | `/api/profissionais/servicos/save` | Associar profissional a um serviço com preço |

.

Agendamentos

| Método   | Endpoint                                                                    | Descrição                       |
| -------- | --------------------------------------------------------------------------- | ------------------------------- |
| `POST`   | `/api/agendamentos/save`                                                    | Criar um novo agendamento       |
| `GET`    | `/api/agendamentos/listar`                                                  | Listar todos os agendamentos    |
| `GET`    | `/api/agendamentos/{id}`                                                    | Buscar agendamento por ID       |
| `DELETE` | `/api/agendamentos/{id}`                                                    | Deletar agendamento por ID      |
| `GET`    | `/api/agendamentos/buscar/data?data=2025-08-20`                             | Buscar agendamentos por data    |
| `GET`    | `/api/agendamentos/buscar/cliente/{clienteId}`                              | Buscar agendamentos por cliente |
| `GET`    | `/api/agendamentos/buscar/periodo?dataInicio=2025-08-01&dataFim=2025-08-31` | Buscar agendamentos por período |

.

🔗 Relacionamentos:

Cliente (1) ⟶ (N) Agendamento
Um cliente pode realizar vários agendamentos.

Profissional (1) ⟶ (N) ProfissionalServico
Um profissional pode oferecer vários serviços.

Servico (1) ⟶ (N) ProfissionalServico
Um serviço pode ser oferecido por vários profissionais.

ProfissionalServico (1) ⟶ (N) Agendamento
Um agendamento sempre referencia um par profissional/serviço.

.

🛠 Modelo Entidade-Relacionamento (DER)

https://lucid.app/lucidchart/421d03fd-b098-464b-a9c6-e7eaaf58106f/edit?viewport_loc=-210%2C-11%2C2268%2C1101%2C0_0&invitationId=inv_4cbc99b5-9981-47e7-9e88-b94a0f07358a

.

👨‍💻 Integrantes

Rafael Carlos Scarabelot 

Gabriel Murbak Scarabelot

Adryan de Souza Furchi

Gabriel de Campos Wegher

.

🧰 Tecnologias utilizadas

Sistema Operacional: Windows

IDE: IntelliJ IDEA

Linguagem: Java 17

Framework: Spring Boot

Banco de Dados: MySQL

Testes de API: Postman

Gerenciador de build: Maven

.



