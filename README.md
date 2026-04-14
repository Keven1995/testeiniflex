# Gestão de Funcionários - Iniflex

Sistema de gestão de funcionários desenvolvido em Java com Spring Boot e banco de dados H2.

## Descrição

Este projeto foi desenvolvido seguindo os princípios de boas práticas em Java, princípios SOLID e arquitetura MVC. O sistema permite:

- Cadastro e gerenciamento de funcionários
- Cálculo de aumentos salariais
- Agrupamento por função
- Relatórios diversos (aniversariantes, funcionário mais velho, etc.)
- Formatação de dados conforme padrões brasileiros

## Tecnologias

- **Java 21**
- **Spring Boot 3.5.13**
- **Spring Data JPA**
- **Banco H2** (em memória)
- **Maven**

## Arquitetura

O projeto segue a arquitetura **MVC** com as seguintes camadas:

```
src/main/java/com/iniflex/gestao/funcionarios/
├── model/          # Entidades (Pessoa, Funcionario)
├── repository/     # Acesso a dados (interfaces Repository)
├── service/        # Lógica de negócio
├── controller/     # Endpoints REST
├── util/           # Classes utilitárias
└── Application.java # Classe principal do Spring Boot
```

## Funcionalidades

O sistema executa as seguintes operações:

1. **Inserir funcionários** - Cadastro de 10 funcionários
2. **Remover funcionário** - Remove o funcionário "João"
3. **Imprimir funcionários** - Lista com formatação (data: dd/mm/aaaa, valores: 1.000,00)
4. **Aumentar salário** - 10% de aumento para todos
5. **Agrupar por função** - Map com função e lista de funcionários
6. **Imprimir por função** - Listagem agrupada
7. **Aniversariantes** - Funcionários que fazem aniversário em outubro e dezembro
8. **Funcionário mais velho** - Nome e idade
9. **Ordem alfabética** - Lista ordenada por nome
10. **Total de salários** - Soma de todos os salários
11. **Salários mínimos** - Quantos salários mínimos (R$ 1.212,00) cada um ganha

## Como Executar

### Pré-requisitos

- JDK 21 ou superior
- Maven 3.6.0 ou superior
- `JAVA_HOME` apontando para uma JDK 21+ 

Verifique as versões em uso:

```bash
java -version
mvn -version
```

O comando `mvn -version` também deve exibir Java 21 ou superior.

### Compilar

```bash
mvn clean compile
```

### Executar

```bash
mvn spring-boot:run
```

Segue também o link do swagger, acesse após rodar a aplicação:
```
http://localhost:8080/swagger-ui/index.html#/
```

### Console H2

Após iniciar a aplicação, acesse o console H2 em:
```
http://localhost:8080/h2-console
```

**Credenciais:**
- URL JDBC: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (deixe em branco)

## Endpoints REST

```bash
# Listar todos os funcionários
GET /api/funcionarios

# Buscar funcionário por ID
GET /api/funcionarios/{id}

# Criar novo funcionário
POST /api/funcionarios
Content-Type: application/json
{
  "nome": "João",
  "dataNascimento": "1990-05-12",
  "salario": 2284.38,
  "funcao": "Operador"
}

# Atualizar funcionário
PUT /api/funcionarios/{id}

# Deletar funcionário
DELETE /api/funcionarios/{id}

# Aplicar aumento de 10%
POST /api/funcionarios/aumentar-salario

# Agrupar por função
GET /api/funcionarios/agrupar-por-funcao

# Ordenar por nome
GET /api/funcionarios/ordenados

# Funcionário mais velho
GET /api/funcionarios/mais-velho

# Aniversariantes (outubro e dezembro)
GET /api/funcionarios/aniversariantes

# Total de salários
GET /api/funcionarios/total-salarios
```

## Estrutura de Dados

### Classe Pessoa
```java
- id: Long (FK)
- nome: String
- dataNascimento: LocalDate
```

### Classe Funcionario
```java
- salario: BigDecimal
- funcao: String

Métodos:
- aumentarSalario(): void
- calcularSalariosMinimos(BigDecimal): BigDecimal
```
