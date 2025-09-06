# MentorIA - Back-end

## Sobre o projeto
O **MentorIA - Back-end** é a API responsável por fornecer os serviços e a lógica de negócio do projeto **MentorIA**, uma plataforma inovadora que busca transformar o mercado da educação e apoiar profissionais em início ou meio de carreira.  

Através da integração com **Inteligência Artificial (IA)**, a API oferece endpoints para gerenciamento de usuários, cursos, perfis profissionais e geração de mentorias personalizadas.  

Construído em **Java 21** com **Spring Boot**, o sistema é escalável, seguro e preparado para integração com bancos de dados relacionais como **PostgreSQL** e bancos em memória como **H2** para testes e desenvolvimento.

---

## Tecnologias utilizadas

- [Java 21](https://openjdk.org/projects/jdk/21/) - Linguagem principal usada no desenvolvimento do back-end.
- [Spring Boot](https://spring.io/projects/spring-boot) - Framework que simplifica a criação de aplicações Java com configuração mínima.
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Para persistência e manipulação de dados em bancos relacionais.
- [Spring Security](https://spring.io/projects/spring-security) - Para autenticação, autorização e segurança da aplicação.
- [Spring AI](https://spring.io/projects/spring-ai) - Integração com modelos de IA, incluindo leitura de documentos PDF e modelos da OpenAI.
- [H2 Database](https://www.h2database.com/) - Banco de dados em memória utilizado em ambiente de desenvolvimento e testes.
- [PostgreSQL](https://www.postgresql.org/) - Banco de dados relacional utilizado em ambiente de produção.
- [Maven](https://maven.apache.org/) - Gerenciador de dependências e build da aplicação.
- [JUnit](https://junit.org/) - Framework de testes unitários.

---

## Estrutura do projeto

```
back-end-mentoria/
│── src/
│   ├── main/
│   │   ├── java/com/mentoria/back_end_mentoria/   # Código-fonte principal
│   │   └── resources/                             # Arquivos de configuração (application.yml/properties)
│   └── test/                                      # Testes automatizados
│── pom.xml                                        # Configuração do Maven e dependências
```

---

## Como executar o projeto

### 1. Pré-requisitos
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) instalado.
- [Maven](https://maven.apache.org/download.cgi) instalado (caso não use o embutido no IntelliJ).
- [PostgreSQL](https://www.postgresql.org/download/) configurado (opcional, apenas para ambiente de produção).

### 2. Clone o repositório
```bash
git clone https://github.com/projeto-integrador-full-stack-jr/back-end.git
cd back-end-mentoria
```

### 3. Rodando no IntelliJ IDEA
1. Abra o IntelliJ IDEA.
2. Vá em **File > Open** e selecione a pasta `back-end-mentoria`.
3. O IntelliJ reconhecerá o projeto Maven automaticamente e baixará as dependências.
4. Configure o SDK do projeto para **Java 21** em  
   `File > Project Structure > Project > SDK`.
5. Execute a classe principal (geralmente `BackEndMentoriaApplication.java`) clicando em **Run**.

### 4. Rodando pelo terminal
```bash
./mvnw spring-boot:run
```

### 5. Acessando a aplicação
Por padrão, a API ficará disponível em:  
👉 `http://localhost:8080`

---

## Bancos de dados

### Ambiente de desenvolvimento/testes
O projeto já vem configurado para usar **H2 Database** em memória.  
Acesse o console do H2 em:  
👉 `http://localhost:8080/h2-console`  
JDBC URL (padrão): `jdbc:h2:mem:testdb`

### Ambiente de produção
Para usar **PostgreSQL**, configure o arquivo `application.properties` ou `application.yml`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mentoria
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

---

## Testes
Para rodar os testes:
```bash
./mvnw test
```

---

## Licença
Este projeto é distribuído sob a licença MIT.
