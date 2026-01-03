# 🧪 Testes

## 📊 Cobertura

**Total: 88%**

| Camada | Cobertura |
|--------|-----------|
| Use Cases | 100% |
| Domain Model | 90% |
| Controllers | 81% |
| Repositories | 100% |

**41 testes | 0 falhas**

---

## 🏃 Como Executar

```bash
# Todos os testes
mvn test

# Com relatório de cobertura
mvn clean verify

# Ver relatório HTML
open target/site/jacoco/index.html
```

---

## 📝 O Que Foi Testado

### ✅ Regras de Negócio (Domain)

**Sanitização de Código:**
- Remove caracteres especiais: `ABC-123` → `ABC123`
- Converte para maiúsculas: `abc123` → `ABC123`
- Valida tamanho de 6 caracteres exatos (após sanitização)
- Códigos inválidos lançam exceção

**Validações:**
- Valor mínimo de desconto: 0.5
- Data de expiração no futuro
- Soft delete preserva dados

**Exemplo de teste:**
```java
@Test
void shouldSanitizeCodeRemovingSpecialCharacters() {
    // Given
    String code = "ABC-123";

    // When
    Coupon coupon = new Coupon(code, "Description",
        new BigDecimal("1.0"), futureDate, false);

    // Then
    assertEquals("ABC123", coupon.getCode());
}
```

### ✅ Casos de Uso (Application)

**CreateCouponUseCase:**
- Criação com dados válidos
- Rejeita desconto < 0.5
- Rejeita data no passado
- Aplica sanitização no código

**GetCouponByIdUseCase:**
- Retorna cupom existente
- Lança exceção quando não encontrado

**DeleteCouponUseCase:**
- Soft delete de cupom ativo
- Impede deletar cupom já deletado

### ✅ API REST (Integration)

**POST /coupon**
- 201 quando dados válidos
- 400 quando dados inválidos

**GET /coupon/{id}**
- 200 quando cupom existe
- 404 quando não existe

**DELETE /coupon/{id}**
- 204 quando deleta com sucesso
- Cupom não pode mais ser encontrado após delete

**Exemplo de teste:**
```java
@Test
void deleteCoupon_ShouldReturnNoContent_WhenExists() throws Exception {
    // Given - Criar cupom
    String id = createTestCoupon("DELTES");

    // When - Deletar
    mockMvc.perform(delete("/coupon/" + id))
            .andExpect(status().isNoContent());

    // Then - Verificar que não existe mais
    mockMvc.perform(get("/coupon/" + id))
            .andExpect(status().isNotFound());
}
```

---

## 🎯 Distribuição dos Testes

- **18 testes** - Domain (regras de negócio)
- **18 testes** - Use Cases (lógica de aplicação)
- **5 testes** - Integration (API completa)

---

## 📋 Checklist de Cobertura

### Sanitização de Código
- [x] Remove caracteres especiais
- [x] Converte para maiúsculas
- [x] Valida tamanho exato (6 caracteres)
- [x] Rejeita códigos inválidos

### Validações de Negócio
- [x] Valor mínimo de desconto (0.5)
- [x] Rejeita valores negativos
- [x] Rejeita data no passado
- [x] Aceita cupom publicado

### Soft Delete
- [x] Marca como deletado
- [x] Preenche deletedAt
- [x] Preserva dados originais
- [x] Impede deletar novamente

### API REST
- [x] Criar cupom (POST)
- [x] Buscar cupom (GET)
- [x] Deletar cupom (DELETE)
- [x] Validações retornam 400
- [x] Não encontrado retorna 404

---

## 🛠 Tecnologias

- **JUnit 5** - Framework de testes
- **Mockito** - Mocks e stubs
- **Spring MockMvc** - Testes de API
- **H2** - Banco em memória
- **JaCoCo** - Cobertura de código

---

## 📌 Observações

**Padrão AAA:** Todos os testes seguem Arrange-Act-Assert

**Isolamento:** Use Cases testados com mocks, sem dependências externas

**Integração:** Testes de API usam H2 em memória, simulando ambiente real

**Os 12% não cobertos incluem:**
- Método `main()` da aplicação
- Construtores de exceções
- Casos extremos de error handling