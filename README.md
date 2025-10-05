# API do Projeto Mentoria

Esta é a API back-end para o Projeto Mentoria, uma plataforma projetada para auxiliar no desenvolvimento e planejamento de carreira de profissionais de tecnologia. A API oferece um conjunto de endpoints para gerenciar usuários, perfis, metas, notas e gerar resumos de carreira personalizados com o auxílio de Inteligência Artificial.

---

## ✨ Funcionalidades Principais

- **Autenticação e Autorização**: Sistema de login seguro com JSON Web Tokens (JWT) e controle de acesso baseado em papéis (`USER` e `ADMIN`).
- **Gerenciamento de Usuários**: Endpoints para criação, visualização, atualização e exclusão de usuários.
- **Perfis Profissionais**: Criação e gestão de perfis detalhados, incluindo cargo, carreira, experiência e objetivos.
- **Gestão de Metas**: Definição e acompanhamento de metas de carreira.
- **Notas Pessoais**: Funcionalidade para criar e gerenciar notas rápidas.
- **Resumos com IA**: Geração automática de resumos de carreira e planos de desenvolvimento utilizando a API da OpenAI, com base nos dados do perfil do usuário.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**: Versão mais recente da linguagem Java.
- **Spring Boot 3.3.0**: Framework principal para a construção da aplicação.
- **Spring Security**: Para a camada de autenticação e autorização.
- **Spring Data JPA**: Para a persistência de dados com o banco de dados.
- **Spring AI**: Para integração com a API da OpenAI.
- **Maven**: Gerenciador de dependências e build do projeto.
- **PostgreSQL**: Banco de dados relacional para o ambiente de produção.
- **H2**: Banco de dados em memória para os testes.
- **Swagger (OpenAPI 3)**: Para documentação interativa da API.

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para configurar e executar a aplicação em seu ambiente local.

### 1. Pré-requisitos

- **JDK 21** (ou superior)
- **Apache Maven 3.9+**
- Uma **chave de API da OpenAI**

### 2. Configuração do Ambiente

A aplicação requer duas variáveis de ambiente para funcionar corretamente:

- `JWT_SECRET`: Uma chave secreta forte para a assinatura dos tokens JWT. 
- `OPENAI-API-KEY`: A sua chave de API da OpenAI.

Você pode configurar essas variáveis diretamente no seu sistema operacional ou criar um arquivo `run.env` na raiz do projeto com o seguinte conteúdo:

```sh
JWT_SECRET=seu_segredo_super_secreto_e_longo_aqui
ALEF_API_KEY=sua_chave_da_openai_aqui
```

**Observação sobre o Banco de Dados:** O perfil de produção (`prod`) está configurado para usar um banco de dados PostgreSQL na nuvem (Neon). As credenciais estão no arquivo `application-prod.properties`. Para usar um banco de dados local, você pode alterar este arquivo ou criar um novo perfil no Spring.

### 3. Executando a Aplicação

Com as variáveis de ambiente configuradas, execute o seguinte comando na raiz do projeto:

```bash
# No Windows
./mvnw spring-boot:run

# No Linux ou macOS
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

---

## 📚 Documentação da API (Swagger)

Após iniciar a aplicação, a documentação completa e interativa da API, gerada com Swagger, pode ser acessada no seu navegador através do seguinte endereço:

[**http://localhost:8080/swagger-ui/index.html**](http://localhost:8080/swagger-ui/index.html)

Lá você encontrará todos os endpoints, seus parâmetros, corpos de requisição e respostas, além de poder testá-los diretamente.

---

## ✅ Executando os Testes

O projeto possui uma suíte de testes unitários e de integração para garantir a qualidade e o correto funcionamento do código. Para executar todos os testes, utilize o comando:

```bash
# No Windows
./mvnw test

# No Linux ou macOS
./mvnw test
```