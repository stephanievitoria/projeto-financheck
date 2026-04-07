# FinanCheck — Backend

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em%20andamento-yellow?style=for-the-badge)

API REST para controle de finanças pessoais. Permite ao usuário registrar receitas e despesas manualmente, categorizar transações, acompanhar o histórico por período e gerenciar metas financeiras.

---

## Tecnologias

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- Banco de dados H2 (in-memory)
- Maven
- Lombok

---

## Arquitetura

O projeto segue a **arquitetura hexagonal** (Ports & Adapters), separando as responsabilidades em três camadas:

```
adapter/         → entrada (controllers) e saída (repositórios JPA)
application/     → casos de uso (interfaces) e serviços (regras de negócio)
domain/          → entidades e portas — sem dependência de framework
config/          → configurações de segurança e banco
```

A regra principal é que o domínio não conhece ninguém — toda dependência aponta para dentro.

---

## Funcionalidades

- Cadastro, edição e desativação de usuários
- Registro e categorização de receitas e despesas
- Busca de transações por período
- Cadastro de categorias personalizadas por usuário
- Cadastro e acompanhamento de metas financeiras com cálculo de progresso

---

## Como rodar localmente

**Pré-requisitos:** Java 17+ e Maven instalados.

```bash
# Clone o repositório
git clone https://github.com/stephanievitoria/projeto-financheck.git
cd financheck

# Execute a aplicação
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

O console do H2 fica disponível em `http://localhost:8080/h2-console` com as configurações:

```
JDBC URL:  jdbc:h2:mem:financheck
Username:  sa
Password:  (deixar em branco)
```

---

## Estrutura de pacotes

```
src/main/java/com/financheck/
├── adapter/
│   ├── in/web/          # Controllers e DTOs
│   └── out/persistence/ # Repositórios JPA e Entities
├── application/
│   ├── usecase/         # Interfaces dos casos de uso
│   └── service/         # Implementações com regras de negócio
├── domain/
│   ├── model/           # Entidades puras do domínio
│   └── port/            # Interfaces de porta (entrada e saída)
└── config/              # SecurityConfig, H2Config
```

---

## Membros do projeto

| Nome | GitHub |
|------|--------|
| Adrielle Moreira | https://github.com/adri-oliver |
| Lívia Novais | https://github.com/Lns14 |
| Stephanie Soares | https://github.com/stephanievitoria |

---

## Status do projeto

🚧 Em desenvolvimento
