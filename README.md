> ## 💭 Nota sobre Arquitetura
>
> Este projeto foi desenvolvido com Clean Architecture para demonstrar:
> - Capacidade de trabalhar com padrões avançados
> - Código altamente testável e manutenível
> - Separação clara de responsabilidades
>
> **Reconheço que para o escopo específico deste desafio, uma arquitetura
> em 3 camadas (Controller → Service → Repository) seria suficiente.**
>
> A escolha foi intencional para mostrar versatilidade técnica, mas em
> contextos reais, eu avaliaria:
> - Tamanho e complexidade do projeto
> - Tamanho da equipe
> - Horizonte de manutenção
> - Trade-offs de tempo vs qualidade

# 🎫 Coupons Service

API RESTful para gerenciamento de cupons de desconto, desenvolvida como desafio técnico seguindo boas práticas de arquitetura e testes.

## 📋 Sobre o Projeto

Este projeto implementa uma API completa de cupons com operações de criação, consulta e exclusão (soft delete), seguindo as especificações do contrato fornecido.

## 🏗 Arquitetura

O projeto adota uma arquitetura em camadas baseada em **Clean Architecture**, priorizando:
- Separação clara de responsabilidades
- Testabilidade
- Manutenibilidade
- Independência de frameworks

### Estrutura de Camadas

```
src/main/java/com/onebrain/coupons/
├── domain/              # Regras de negócio e entidades
│   ├── model/          # Entidades do domínio
│   ├── interfaces/     # Contratos (portas)
│   ├── exceptions/     # Exceções de negócio
│   └── enums/         # Enumerações
├── application/        # Casos de uso
│   └── usecases/      # Lógica de aplicação
└── infra/             # Detalhes de implementação
    ├── controllers/   # API REST
    ├── repositories/  # Implementação de persistência
    ├── persistence/   # Entidades JPA
    └── config/        # Configurações
```

### Camadas Explicadas

**Domain**: Contém as regras de negócio puras, sem dependências de frameworks. A entidade `Coupon` encapsula todas as validações necessárias (código alfanumérico, valor mínimo, data de expiração).

**Application**: Implementa os casos de uso (`CreateCouponUseCase`, `GetCouponByIdUseCase`, `DeleteCouponUseCase`), orquestrando as operações entre o domínio e a infraestrutura.

**Infrastructure**: Camada de adaptadores que integra com frameworks externos (Spring Boot, JPA, validações, etc.).

## 🎯 Decisões Técnicas

### Por que Clean Architecture?

Embora reconheça que uma arquitetura mais simples seria suficiente para o escopo do desafio, optei por demonstrar conhecimento de padrões arquiteturais avançados. Os principais benefícios incluem:

- **Testabilidade**: Regras de negócio podem ser testadas sem dependências externas
- **Flexibilidade**: Mudanças em frameworks ou banco de dados não afetam o núcleo do sistema
- **Organização**: Estrutura clara facilita navegação e manutenção do código

### Trade-offs Considerados

- ✅ Maior cobertura de testes e qualidade de código
- ✅ Código mais expressivo e autodocumentado
- ⚠️ Mais arquivos e camadas para navegar
- ⚠️ Curva de aprendizado inicial maior

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

- `POST /api/coupons` - Criar cupom
- `GET /api/coupons/{id}` - Buscar cupom por ID
- `DELETE /api/coupons/{id}` - Excluir cupom (soft delete)

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

- Cache com Redis para consultas frequentes
- Paginação nos endpoints de listagem
- Eventos de domínio para auditoria
- Metrics com Micrometer/Prometheus
- Rate limiting por API key

## 📞 Contato

Projeto desenvolvido como desafio técnico por [Walyson Moises](https://linkedin.com/in/walysonmoises)

---

**Nota**: Este projeto prioriza qualidade de código, testes e arquitetura sobre entrega rápida, demonstrando capacidade de trabalhar em projetos de médio/longo prazo com equipes maiores.