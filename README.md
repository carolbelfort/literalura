# 📚 LiterAlura - Catálogo de Livros

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)
![Status](https://img.shields.io/badge/Status-Concluído-success)

Aplicação de catálogo de livros desenvolvida em Java com Spring Boot, que consome a API Gutendex para buscar informações sobre livros e autores, armazenando-os em um banco de dados PostgreSQL.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Como Usar](#como-usar)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Demonstração](#demonstração)
- [Autor](#autor)

---

## 🎯 Sobre o Projeto

O **LiterAlura** é uma aplicação de console desenvolvida como parte do desafio do programa ONE (Oracle Next Education) da Alura. O projeto tem como objetivo criar um catálogo interativo de livros onde o usuário pode:

- Buscar livros pela API Gutendex
- Armazenar livros e autores no banco de dados
- Consultar estatísticas sobre os livros cadastrados
- Filtrar livros por idioma
- Listar autores vivos em determinado ano

---

## ⚙️ Funcionalidades

### 1️⃣ Buscar livro por título
- Busca livros na API Gutendex pelo título
- Salva automaticamente no banco de dados
- Evita duplicação de livros e autores

### 2️⃣ Listar livros registrados
- Exibe todos os livros salvos no banco de dados
- Mostra título, autor, idioma e número de downloads

### 3️⃣ Listar autores registrados
- Lista todos os autores cadastrados
- Exibe nome, ano de nascimento e falecimento

### 4️⃣ Listar autores vivos em determinado ano
- Consulta autores que estavam vivos em um ano específico
- Valida entrada do usuário
- Usa Derived Queries para consulta eficiente

### 5️⃣ Listar livros por idioma
- Filtra livros por idioma (pt, en, es, fr)
- Exibe estatísticas usando Java Streams:
  - Total de livros
  - Total de downloads
  - Média de downloads
  - Livro mais baixado
  - Total de autores únicos

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.1.5**
  - Spring Data JPA
  - Spring Boot Starter
- **Hibernate** (ORM)
- **PostgreSQL** (Banco de dados)

### Bibliotecas
- **Jackson** 2.16.0 (Manipulação JSON)
- **HttpClient** (Consumo de API)

### API Externa
- **Gutendex API** - https://gutendex.com/

---

## 📦 Pré-requisitos

Antes de começar, você precisa ter instalado em sua máquina:

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL 18+](https://www.postgresql.org/download/)
- [Git](https://git-scm.com/)
- IDE de sua preferência (recomendado: IntelliJ IDEA)

---

## 🚀 Instalação

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/literalura.git
cd literalura
```

### 2. Configure o banco de dados

Crie um banco de dados no PostgreSQL:
```sql
CREATE DATABASE literalura_db;
```

### 3. Configure as credenciais

Edite o arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 4. Compile o projeto
```bash
mvn clean install
```

### 5. Execute a aplicação
```bash
mvn spring-boot:run
```

Ou execute diretamente pela IDE:
- Abra a classe `LiterAluraApplication.java`
- Execute o método `main`

---

## 💻 Como Usar

Ao executar a aplicação, você verá o menu principal:

```
╔════════════════════════════════════════╗
║       CATÁLOGO DE LIVROS - LITERALURA  ║
╚════════════════════════════════════════╝

1 - Buscar livro por título
2 - Listar livros registrados
3 - Listar autores registrados
4 - Listar autores vivos em determinado ano
5 - Listar livros por idioma

0 - Sair
```

### Exemplos de uso:

**Buscar um livro:**
```
Escolha uma opção: 1
Digite o título do livro: Dom Casmurro
```

**Listar autores vivos em 1900:**
```
Escolha uma opção: 4
Digite o ano: 1900
```

**Ver estatísticas de livros em português:**
```
Escolha uma opção: 5
Digite o idioma: pt
```

---

## 📁 Estrutura do Projeto

```
literalura/
├── src/
│   ├── main/
│   │   ├── java/com/literalura/
│   │   │   ├── model/
│   │   │   │   ├── Autor.java
│   │   │   │   ├── Livro.java
│   │   │   │   ├── DadosAutor.java
│   │   │   │   ├── DadosLivro.java
│   │   │   │   └── RespostaApi.java
│   │   │   ├── repository/
│   │   │   │   ├── AutorRepository.java
│   │   │   │   └── LivroRepository.java
│   │   │   ├── service/
│   │   │   │   ├── ConsumoApi.java
│   │   │   │   ├── ConverteDados.java
│   │   │   │   └── IConverteDados.java
│   │   │   ├── LiterAluraApplication.java
│   │   │   └── Principal.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

---

## 🎬 Demonstração

### Busca de livro
```
✅ 1 livro(s) encontrado(s):

═══════════════════════════════════════════════════════
📖 Título: Dom Casmurro
👤 Autor: Machado de Assis
🌐 Idioma: pt
📥 Downloads: 1478
═══════════════════════════════════════════════════════
```

### Estatísticas por idioma
```
📊 ESTATÍSTICAS DE LIVROS NO IDIOMA 'PT'
═══════════════════════════════════════════════════════
📚 Total de livros: 3
📥 Total de downloads: 2645
📊 Média de downloads: 881.67
🏆 Livro mais baixado: Dom Casmurro (1478 downloads)
👤 Total de autores: 1
═══════════════════════════════════════════════════════
```

---

## 🎓 Aprendizados

Durante o desenvolvimento deste projeto, foram aplicados conceitos de:

- ✅ Consumo de APIs REST
- ✅ Manipulação de JSON com Jackson
- ✅ Persistência de dados com JPA/Hibernate
- ✅ Relacionamentos entre entidades (OneToMany, ManyToOne)
- ✅ Derived Queries e JPQL
- ✅ Java Streams e expressões lambda
- ✅ Tratamento de exceções
- ✅ Validação de entrada do usuário
- ✅ Boas práticas de código

---

## 👨‍💻 Autor

**Ana Carolina Belfort de Oliveira**

- GitHub: [@carolbelfort](https://github.com/carolbelfort)
- Email: anacarolinabelfort@outlook.com

---

## 📄 Licença

Este projeto foi desenvolvido como parte do programa ONE (Oracle Next Education) da Alura.

---

## 🙏 Agradecimentos

- [Alura](https://www.alura.com.br/) - Pela oportunidade e conhecimento
- [Oracle](https://www.oracle.com/) - Pelo programa ONE
- [Gutendex](https://gutendex.com/) - Pela API gratuita de livros

---

⭐ **Se este projeto te ajudou, deixe uma estrela!** ⭐
