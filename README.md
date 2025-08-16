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

Spring Boot: Backend estruturado com API RESTful

Java 17: Programação orientada a objetos

MySQL: Banco de dados relacional

Postman: Testes de requisições HTTP

IntelliJ IDEA: IDE utilizada no desenvolvimento

Maven: Gerenciamento de dependências e build

.

🛠 Requisitos Funcionais

API com endpoints CRUD (Create, Read, Update, Delete)

Associação entre entidades via relacionamento JPA

Requisições testadas e documentadas via Postman

Persistência de dados no MySQL

Validações básicas e tratamento de erros

Uso de anotações Spring (como @RestController, @Entity, @Repository, @Service)

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

Use o Postman para fazer requisições nos endpoints, como:

POST /api/clientes

POST /api/profissionais

POST /api/servicos

POST /api/agendamentos

GET /api/agendamentos

.

🛠 Modelo Entidade-Relacionamento (DER)

Cliente (1) ⟶ (N) Agendamentos

Profissional (1) ⟶ (N) ProfissionalServico

Servico (1) ⟶ (N) ProfissionalServico

ProfissionalServico (1) ⟶ (N) Agendamentos

https://lucid.app/lucidchart/421d03fd-b098-464b-a9c6-e7eaaf58106f/edit?viewport_loc=-210%2C-11%2C2268%2C1101%2C0_0&invitationId=inv_4cbc99b5-9981-47e7-9e88-b94a0f07358a

.

👨‍💻 Integrantes

Rafael Carlos Scarabelot 

Gabriel Murbak Scarabelot

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

