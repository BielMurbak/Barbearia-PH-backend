# Testes Postman - Barbearia PH API

## 🔐 1. AUTENTICAÇÃO

### Login Admin/Profissional
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "celular": "(45) 9857-3445",
  "senha": "Patrick123"
}
```

### Login Cliente
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "celular": "(45) 99935-5808",
  "senha": "123456"
}
```

### Validar Token
```
POST http://localhost:8080/api/auth/validar
Content-Type: application/json

{
  "token": "SEU_TOKEN_AQUI"
}
```

## 👤 2. CLIENTES (Requer ROLE_ADMIN para escrita)

### Listar Clientes (Qualquer usuário autenticado)
```
GET http://localhost:8080/api/clientes/listar
Authorization: Bearer SEU_TOKEN_AQUI
```

### Criar Cliente (Apenas ADMIN)
```
POST http://localhost:8080/api/clientes/save
Authorization: Bearer TOKEN_ADMIN
Content-Type: application/json

{
  "nome": "João",
  "sobrenome": "Silva",
  "celular": "(45) 99999-9999",
  "senha": "123456"
}
```

### Buscar Cliente por ID
```
GET http://localhost:8080/api/clientes/1
Authorization: Bearer SEU_TOKEN_AQUI
```

### Atualizar Cliente (Apenas ADMIN)
```
PUT http://localhost:8080/api/clientes/1
Authorization: Bearer TOKEN_ADMIN
Content-Type: application/json

{
  "nome": "João Atualizado",
  "sobrenome": "Silva",
  "celular": "(45) 99999-9999",
  "senha": "novasenha123"
}
```

## 👨‍💼 3. PROFISSIONAIS (Apenas ROLE_ADMIN)

### Listar Profissionais
```
GET http://localhost:8080/api/profissionais/listar
Authorization: Bearer TOKEN_ADMIN
```

### Criar Profissional
```
POST http://localhost:8080/api/profissionais/save
Authorization: Bearer TOKEN_ADMIN
Content-Type: application/json

{
  "nome": "Carlos",
  "sobrenome": "Barbeiro",
  "celular": "(45) 98888-8888",
  "senha": "carlos123",
  "especializacao": "BARBEIRO"
}
```

### Buscar por Especialização
```
GET http://localhost:8080/api/profissionais/buscar/especializacao?especializacao=BARBEIRO
Authorization: Bearer TOKEN_ADMIN
```

## 💼 4. SERVIÇOS (Apenas ROLE_ADMIN)

### Listar Serviços
```
GET http://localhost:8080/api/servicos/listar
Authorization: Bearer TOKEN_ADMIN
```

### Criar Serviço
```
POST http://localhost:8080/api/servicos/save
Authorization: Bearer TOKEN_ADMIN
Content-Type: application/json

{
  "descricao": "Corte Moderno",
  "minDeDuracao": 45
}
```

### Buscar por Descrição
```
GET http://localhost:8080/api/servicos/buscar/descricao?descricao=Cabelo
Authorization: Bearer TOKEN_ADMIN
```

## 🔗 5. PROFISSIONAL-SERVIÇO (Apenas ROLE_ADMIN)

### Associar Profissional a Serviço
```
POST http://localhost:8080/api/profissionais/servicos/save
Authorization: Bearer TOKEN_ADMIN
Content-Type: application/json

{
  "profissionalEntity": {
    "id": 1
  },
  "servicoEntity": {
    "id": 1
  },
  "preco": 50.00
}
```

## 📅 6. AGENDAMENTOS

### Listar Agendamentos (Filtrado por Role)
```
GET http://localhost:8080/api/agendamentos
Authorization: Bearer SEU_TOKEN_AQUI
```
- **ADMIN**: Vê todos os agendamentos
- **CLIENTE**: Vê apenas seus próprios agendamentos

### Criar Agendamento
```
POST http://localhost:8080/api/agendamentos
Authorization: Bearer SEU_TOKEN_AQUI
Content-Type: application/json

{
  "clienteEntity": {
    "id": 1
  },
  "profissionalServicoEntity": {
    "id": 1
  },
  "dataAgendamento": "2025-02-15",
  "horaAgendamento": "14:30:00",
  "local": "Barbearia PH"
}
```

### Buscar por Data
```
GET http://localhost:8080/api/agendamentos/data?data=2025-02-15
Authorization: Bearer SEU_TOKEN_AQUI
```

### Buscar por Cliente (Apenas ADMIN)
```
GET http://localhost:8080/api/agendamentos/cliente/1
Authorization: Bearer TOKEN_ADMIN
```

### Deletar Agendamento (Apenas ADMIN)
```
DELETE http://localhost:8080/api/agendamentos/1
Authorization: Bearer TOKEN_ADMIN
```

## 🧪 7. TESTES DE AUTORIZAÇÃO

### Teste 1: Cliente tentando acessar rota de ADMIN (deve dar 403)
```
GET http://localhost:8080/api/profissionais/listar
Authorization: Bearer TOKEN_CLIENTE
```

### Teste 2: Cliente tentando criar outro cliente (deve dar 403)
```
POST http://localhost:8080/api/clientes/save
Authorization: Bearer TOKEN_CLIENTE
Content-Type: application/json

{
  "nome": "Teste",
  "sobrenome": "Unauthorized",
  "celular": "(45) 99999-0000",
  "senha": "123456"
}
```

### Teste 3: Acesso sem token (deve dar 401)
```
GET http://localhost:8080/api/agendamentos
```

## 📋 COLLECTION POSTMAN

Para importar no Postman, crie uma collection com:

### Variables:
- `baseUrl`: `http://localhost:8080`
- `adminToken`: (cole o token do admin após login)
- `clienteToken`: (cole o token do cliente após login)

### Headers Globais:
- `Content-Type`: `application/json`
- `Authorization`: `Bearer {{adminToken}}` (ou `{{clienteToken}}`)

## 🔑 Fluxo de Teste Recomendado:

1. **Login Admin** → Copiar token
2. **Login Cliente** → Copiar token  
3. **Testar rotas com token admin**
4. **Testar rotas com token cliente**
5. **Testar rotas sem token**
6. **Verificar autorização por role**