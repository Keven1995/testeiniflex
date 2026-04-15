 Gestão de Funcionários - Iniflex

Sistema de gestão de funções desenvolvido em Java com Spring Boot e banco de dados H2.

## Descrição

Este projeto foi desenvolvido seguindo os requisitos solicitados. O sistema permite:

- Cadastro e gerenciamento de funções
- Cálculo de aumentos salariais
- Agrupamento por função
- Relacionamentos diversos (aniversariantes, funcionário mais velho, etc)
- Formatação de dados conformes padrões brasileiros

## Tecnologias

- **Java 21**
- **Bota de mola 3.5.13**
- **Dados de primavera JPA**
- **Banco H2** (em memória)
- **Especialista**

## Arquitetura

O projeto segue uma arquitetura **MVC** com as seguintes câmeras:

```
src/main/java/com/iniflex/gestao/funcionários/
├── modelo/ # Entidades (Pessoa, Funcionário)
├── repositório/ # Acesso a dados (Repositório de interfaces)
├── serviço/ # Lógica de negócio
├── controlador/ # Pontos finais REST
├── util/ # Classes utilitárias
└── Application.java # Classe principal do Spring Boot
```

## Funcionalidades

O sistema executado como seguintes operações:

1. **Inserir funções** - Cadastro de 10 funções
2. **Função removedora** - Remova o funcionário "João"
3. **Imprimir funções** - Lista com formação (dados: dd/mm/aaaa, valores: 1.000,00)
4. **Aumentar salão** - 10% de aumento para todos
5. **Agrupar por função** - Mapa com função e lista de funções
6. **Imprimir por função** - Listagem agitada
7. **Aniversariantes** - Funcionários que fazem aniversário em outubro e dezembro
8. **Funcionário mais velho** - Nome e idade
9. **Ordem alfabética** - Lista ordenada por nome
10. **Total de vendas** - Soma de todos os salários
11. **Salários mínimos** - Quantos salários mínimos (R$ 1.212,00) cada um ganha

## Como Executivo

### Pré-requisitos

- JDK 21 ou superior
- Maven 3.6.0 ou superior
- `JAVA_CASA` apontando para uma JDK 21+ 

Verifique as versões em uso:

```batedor
versão Java
mvn -versão
```

### Compilar

```batedor
compilação limpa mvn
```

### Executar

```batedor
mvn spring-boot:executar
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
- Usuário: `sa`
- Senha: (deixe em branco)

## Pontos finais REST

```batedor
 - 10% de aumento para todos
OBTER/api/funcionários

# Função de ônibus por ID
OBTER/api/funcionários/{id}

# Criar novo funcional
POST/api/funcionários
 - Listagem agitada
{
 "nome": "João",
 "dataNascimento": "12/05/1990",
 "salário": 2284,38,
 "funcao": "Operador"
}

# Atualizar funcional
PUT/api/funcionários/{id}

# Deletar funcional
EXCLUIR/api/funcionários/{id}

# Aplicar aumento de 10%
POST/api/funcionários/aumentar-salario

# Agrupar por diversidade
GET/api/funcionários/agrupar-por-funcao

# Ordenar por nome
OBTER/api/funcionários/ordenados

# Funcionário mais velho
GET/api/funcionários/mais-velho

# Aniversariantes (outubro e dezembro)
GET/api/funcionários/aniversariantes

# Total de salões
GET/api/funcionários/salários totais
```

## Estrutura de Dados

### Classe Pessoa
```java
- id: Longo (FK)
- nome: String
- dataNascimento: LocalDate
```

### Classe Funciionário
```java
- salário: BigDecimal
- funcao: Corda

Métodos:
- aumentarSalario(): vazio
- calcularSalariosMinimos(BigDecimal): BigDecimal
```
