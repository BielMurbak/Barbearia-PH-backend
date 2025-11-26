# Implementação de Segurança - Barbearia PH

## ✅ Requisitos Implementados

### 1. Criptografia de Senhas
- **BCrypt**: Senhas criptografadas usando `BCryptPasswordEncoder`
- **DataLoader**: Usuários iniciais criados com senhas criptografadas
- **Registro**: Endpoint `/api/auth/register/cliente` criptografa senhas automaticamente

### 2. Gerenciamento de Chaves JWT
- **Configuração Externa**: JWT secret configurado via `application.properties`
- **Variáveis de Ambiente**: Suporte a `${JWT_SECRET}` e `${JWT_EXPIRATION}`
- **Arquivo .env**: Exemplo criado em `.env.example`

### 3. Autenticação
- **Endpoint de Login**: `/api/auth/login`
- **Validação de Credenciais**: Verifica celular e senha
- **Token JWT**: Retorna token válido com role do usuário
- **Expiração**: Token expira em 24 horas (configurável)

### 4. Proteção de Rotas
- **JWT Filter**: `JwtAuthFilter` valida tokens em todas as requisições
- **Header Authorization**: Requer `Bearer <token>` no header
- **Sessão Stateless**: Configurado para não manter sessão

### 5. Autorização por Roles
- **ROLE_ADMIN**: Profissionais (acesso total)
- **ROLE_CLIENTE**: Clientes (acesso limitado)

## 🔐 Configuração de Rotas

### Rotas Públicas
- `POST /api/auth/login` - Login
- `POST /api/auth/register/cliente` - Registro de cliente

### Rotas ROLE_ADMIN (Profissionais)
- `GET/POST/PUT/DELETE /api/profissionais/**`
- `GET/POST/PUT/DELETE /api/servicos/**`
- `POST /api/profissionais/servicos/**`
- `POST/PUT/DELETE /api/clientes/**`
- `DELETE /api/agendamentos/{id}`
- `GET /api/agendamentos/cliente/{clienteId}`
- `GET /api/agendamentos/periodo`

### Rotas Autenticadas (Ambos os Roles)
- `GET /api/clientes/**` - Leitura de clientes
- `GET/POST /api/agendamentos/**` - Agendamentos (com filtro por role)

## 👥 Usuários de Teste

### Admin/Profissional
- **Celular**: `(45) 9857-3445`
- **Senha**: `Patrick123`
- **Role**: `ROLE_ADMIN`

### Cliente
- **Celular**: `(45) 99935-5808`
- **Senha**: `123456`
- **Role**: `ROLE_CLIENTE`

## 🚀 Como Testar

### 1. Login
```bash
POST /api/auth/login
{
  "celular": "(45) 9857-3445",
  "senha": "Patrick123"
}
```

### 2. Usar Token
```bash
Authorization: Bearer <seu_token_aqui>
```

### 3. Testar Autorização
- Tente acessar `/api/profissionais/listar` com token de cliente (deve dar 403)
- Tente acessar `/api/agendamentos` com token de admin (deve funcionar)

## ⚙️ Configuração de Ambiente

Crie um arquivo `.env` baseado no `.env.example`:
```env
JWT_SECRET=sua_chave_secreta_super_forte_aqui
JWT_EXPIRATION=86400000
```