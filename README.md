# 🎫 Coupons Service

API RESTful para gerenciamento de cupons de desconto, desenvolvida como desafio técnico seguindo boas práticas de arquitetura e testes.

## 📋 Sobre o Projeto

Este projeto implementa uma API completa de cupons com operações de criação, consulta e exclusão (soft delete), seguindo as especificações do contrato fornecido.

## 🏗 Arquitetura

O projeto adota uma **Arquitetura em Camadas (Layered Architecture)** seguindo o padrão clássico MVC do Spring Boot, priorizando:
- Simplicidade e pragmatismo
- Redução de boilerplate
- Clareza e facilidade de navegação
- Alinhamento com os padrões da comunidade Spring

### Estrutura de Camadas

```
src/main/java/com/onebrain/coupons/
├── controller/         # Camada REST (Entrada)
├── dto/           # Objetos de Transferência de Dados
├── service/            # Regras de Negócio
├── repository/         # Acesso a Dados
├── entity/             # Entidades JPA (Domínio + Persistência)
├── exception/          # Tratamento de Erros Global
├── config/             # Configurações do Spring
└── enums/              # Enumerações
```

### Camadas Explicadas

**Controller**: Ponto de entrada da aplicação. Recebe as requisições HTTP, valida os DTOs de entrada e delega o processamento para o Service. Retorna as respostas HTTP apropriadas.

**Service**: Contém toda a lógica de negócio (Business Logic). É responsável pelas validações de domínio, cálculos e orquestração das chamadas ao Repository.

**Repository**: Interface que estende `JpaRepository`, responsável pela comunicação com o banco de dados.

**Entity**: Representa a tabela no banco de dados e contém o estado a ser persistido. Nesta abordagem simplificada, a entidade JPA também atua como objeto de domínio.

## 🎯 Decisões Técnicas

### Por que Simplificar para MVC Padrão?

Para este desafio específico, a arquitetura anterior (Clean Architecture) introduzia complexidade acidental. A migração para um modelo MVC simplificado trouxe:

- **Menos código**: Remoção de mapeadores e classes duplicadas (Modelo de Domínio vs Entidade JPA).
- **Maior produtividade**: Uso direto de recursos do Spring Data e Validation.
- **Curva de aprendizado menor**: Padrão amplamente conhecido por desenvolvedores java/Spring.

### Trade-offs Considerados

- ✅ Desenvolvimento mais rápido e direto
- ✅ Menos arquivos para manter
- ✅ Estrutura familiar para a maioria dos devs Spring
- ⚠️ Acoplamento entre domínio e infraestrutura (anotações JPA na entidade)

## 🚀 Tecnologias

- **Java 25** - Versão LTS mais recente
- **Spring Boot 4.0.1** - Framework web e DI
- **H2 Database** - Banco em memória (conforme requisito)
- **JUnit 5 + Mockito** - Framework de testes
- **Lombok** - Redução de boilerplate
- **OpenAPI 3.0** - Documentação da API
- **Docker** - Containerização

## ✅ Testes

O projeto possui cobertura de testes superior a 80%:

- **Testes Unitários**: Validam regras de negócio no domínio
- **Testes de Use Cases**: Verificam orquestração da lógica de aplicação
- **Testes de Integração**: Validam endpoints REST completos

### Executar Testes

```bash
mvn test
```

### Relatório de Cobertura

```bash
mvn verify
# Relatório disponível em: target/site/jacoco/index.html
```
### 🚥 Testes de API (Postman/Newman)

Para validar o contrato e o fluxo da API de ponta a ponta, utilizamos o Newman.

**Pré-requisitos:**
- Node.js instalado
- `npm install -g newman newman-reporter-htmlextra`

**Executar testes:**
```bash
newman run coupon-api-tests.json \
  --env-var "base_url=http://localhost:8080" \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export report.html
```

> Mais detalhes em [TESTS.md](TESTS.md)

## 🏃‍♂️ Como Executar

### Pré-requisitos
- Java 25+
- Maven 3.8+
- Docker (opcional)

### Execução Local

```bash
# Compilar e executar
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### Execução com Docker

```bash
# Build e execução
docker compose up --build
```

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

**Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Endpoints Principais

- `POST /coupon` - Criar cupom
- `GET /coupon/{id}` - Buscar cupom por ID
- `DELETE /coupon/{id}` - Excluir cupom (soft delete)

## 📝 Regras de Negócio Implementadas

### Criação de Cupom
- ✅ Código alfanumérico com 6 caracteres (caracteres especiais removidos automaticamente)
- ✅ Valor de desconto mínimo de 0.5
- ✅ Data de expiração não pode ser no passado
- ✅ Código único (não pode duplicar)
- ✅ Possibilidade de criar cupom já publicado

### Exclusão de Cupom
- ✅ Soft delete (preserva dados no banco)
- ✅ Impede exclusão de cupom já deletado
- ✅ Registra data/hora da exclusão

## 🔄 Melhorias Futuras

Se tivesse mais tempo, implementaria:

- Metrics com Micrometer/Prometheus
- Eventos de domínio para auditoria
- Rate limiting por API key
- Cache com Redis para consultas frequentes
- Implementação de endpoint de listagem com paginação

## 📞 Contato

Projeto desenvolvido como desafio técnico por [Walyson Moises](https://linkedin.com/in/walysonmoises)

---

**Nota**: Este projeto prioriza qualidade de código, testes e arquitetura sobre entrega rápida, demonstrando capacidade de trabalhar em projetos de médio/longo prazo com equipes maiores.