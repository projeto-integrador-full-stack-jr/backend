
# Documentação de Endpoints para o Front-end

Este documento detalha os endpoints da API que o time de front-end precisará consumir, com foco nos endpoints de `Metas` e `Resumos` (especificamente o upload de PDF).

**Autenticação:** Todos os endpoints de usuário (`/minhas` e `/meus`) requerem autenticação. O token JWT deve ser enviado no cabeçalho `Authorization` de cada requisição.

```
Authorization: Bearer <seu-token-jwt>
```

---

## Módulo de Metas (`/metas`)

Endpoints para o gerenciamento de metas de carreira do usuário.

### 1. Listar Minhas Metas

- **Endpoint:** `GET /metas/minhas`
- **Descrição:** Retorna uma lista com todas as metas associadas ao usuário autenticado.
- **Ciclo de Vida:**
    1. O front-end envia uma requisição `GET` para `/metas/minhas` com o token de autenticação.
    2. O back-end identifica o usuário pelo token, busca no banco de dados todas as metas vinculadas a ele e retorna uma lista de objetos `MetaResponse`.
    3. Se o usuário não tiver metas, retorna uma lista vazia.
- **Exemplo de Implementação (React):**

```jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const MinhasMetas = () => {
    const [metas, setMetas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMetas = async () => {
            try {
                const token = localStorage.getItem('userToken'); // Ou de onde você gerencia o token
                const response = await axios.get('/api/metas/minhas', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                setMetas(response.data);
            } catch (err) {
                setError('Não foi possível carregar as metas.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchMetas();
    }, []);

    if (loading) return <p>Carregando metas...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h2>Minhas Metas</h2>
            {metas.length > 0 ? (
                <ul>
                    {metas.map(meta => (
                        <li key={meta.id}>
                            <strong>{meta.nomeMeta}</strong> - {meta.status}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>Você ainda não tem metas cadastradas.</p>
            )}
        </div>
    );
};

export default MinhasMetas;
```

### 2. Buscar Minha Meta por ID

- **Endpoint:** `GET /metas/minhas/{id}`
- **Descrição:** Retorna os detalhes de uma meta específica do usuário.
- **Ciclo de Vida:**
    1. O front-end envia uma requisição `GET` para `/metas/minhas/{id}`.
    2. O back-end verifica se a meta com o `id` fornecido pertence ao usuário autenticado.
    3. Se pertencer, retorna o objeto `MetaResponse` correspondente. Caso contrário, retorna um erro (ex: 404 Not Found).
- **Exemplo de Implementação (React):**

```jsx
// (Similar ao fetchMetas, mas para um único item)
const fetchMetaPorId = async (id) => {
    try {
        const token = localStorage.getItem('userToken');
        const response = await axios.get(`/api/metas/minhas/${id}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        // setMetaDetalhe(response.data);
    } catch (err) {
        // Tratar erro
    }
};
```

### 3. Criar Nova Meta

- **Endpoint:** `POST /metas/minhas`
- **Descrição:** Cria uma nova meta para o usuário autenticado.
- **Corpo da Requisição (`MetaRequest`):**
    ```json
    {
      "titulo": "Aprender React Avançado",
      "prazo": "2025-12-31T23:59:59Z"
    }
    ```
    - `prazo`: Deve ser uma data e hora no futuro, enviada como uma string no formato ISO 8601 (UTC).
- **Ciclo de Vida:**
    1. O usuário preenche um formulário no front-end, selecionando uma data e hora para o prazo.
    2. Ao submeter, o front-end envia uma requisição `POST` com o objeto `MetaRequest`. O `prazo` deve ser um `Instant` (string ISO 8601).
    3. O back-end valida os dados, cria uma nova `Meta` com status `AGUARDANDO`, associa ao usuário e salva no banco.
    4. Retorna o objeto `MetaResponse` da meta recém-criada.
- **Exemplo de Implementação (React):**

```jsx
const criarMeta = async (novaMeta) => {
    // Exemplo de novaMeta: { titulo: "Minha nova meta", prazo: "2025-01-01T00:00:00Z" }
    try {
        const token = localStorage.getItem('userToken');
        const response = await axios.post('/api/metas/minhas', novaMeta, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        // Adicionar a nova meta à lista de metas no estado
        // setMetas(prevMetas => [...prevMetas, response.data]);
    } catch (err) {
        // Tratar erro de validação ou outros
    }
};
```

### 4. Atualizar Minha Meta

- **Endpoint:** `PUT /metas/minhas/{id}`
- **Descrição:** Atualiza completamente uma meta existente do usuário.
- **Corpo da Requisição (`MetaRequest`):** Mesmo formato do `POST`.
- **Ciclo de Vida:**
    1. O usuário edita os dados de uma meta existente.
    2. O front-end envia uma requisição `PUT` para `/metas/minhas/{id}` com o objeto completo, incluindo o `prazo` no formato ISO 8601.
    3. O back-end busca a meta, verifica a permissão, atualiza todos os campos e salva.
    4. Retorna o objeto `MetaResponse` atualizado.
- **Exemplo de Implementação (React):**

```jsx
const atualizarMeta = async (id, metaAtualizada) => {
    try {
        const token = localStorage.getItem('userToken');
        const response = await axios.put(`/api/metas/minhas/${id}`, metaAtualizada, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        // Atualizar a meta na lista do estado
    } catch (err) {
        // Tratar erro
    }
};
```

### 5. Alterar Status da Meta

- **Endpoint:** `PATCH /metas/minhas/{id}`
- **Descrição:** Altera apenas o status de uma meta. O back-end não permite a alteração para os status `AGUARDANDO` ou `EXPIRADA`.
- **Corpo da Requisição:** Uma string de texto simples (`text/plain`) contendo o nome do status.
    - Exemplo de corpo: `"EM_ANDAMENTO"`
- **Valores de Status Possíveis:**
    - `AGUARDANDO` (Status inicial, não pode ser definido via PATCH)
    - `EM_ANDAMENTO`
    - `CONCLUIDO`
    - `EXPIRADA` (Definido automaticamente pelo sistema, não pode ser definido via PATCH)
- **Ciclo de Vida:**
    1. O usuário interage com um controle (ex: um botão ou drag-and-drop em um kanban) para mudar o status da meta.
    2. O front-end envia a requisição `PATCH` com o novo status como uma string no corpo da requisição.
    3. O back-end valida e atualiza apenas o campo `status` da meta.
    4. Retorna o objeto `MetaResponse` completo com o status modificado.
- **Exemplo de Implementação (React):**

```jsx
const alterarStatusMeta = async (id, novoStatus) => {
    // novoStatus deve ser "EM_ANDAMENTO" ou "CONCLUIDO"
    try {
        const token = localStorage.getItem('userToken');
        const response = await axios.patch(`/api/metas/minhas/${id}`, novoStatus, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'text/plain'
            }
        });
        // Atualizar o status da meta na lista do estado
    } catch (err) {
        // Tratar erro
    }
};
```

### 6. Deletar Minha Meta

- **Endpoint:** `DELETE /metas/minhas/{id}`
- **Descrição:** Remove uma meta do usuário.
- **Ciclo de Vida:**
    1. O usuário clica em um botão para deletar a meta.
    2. O front-end envia a requisição `DELETE`.
    3. O back-end verifica a permissão e remove a meta do banco de dados.
    4. Retorna uma resposta `204 No Content` em caso de sucesso.
- **Exemplo de Implementação (React):**

```jsx
const deletarMeta = async (id) => {
    if (window.confirm('Tem certeza que deseja deletar esta meta?')) {
        try {
            const token = localStorage.getItem('userToken');
            await axios.delete(`/api/metas/minhas/${id}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            // Remover a meta da lista no estado
            // setMetas(prevMetas => prevMetas.filter(meta => meta.id !== id));
        } catch (err) {
            // Tratar erro
        }
    }
};
```

---

## Módulo de Resumos (`/resumos`)

Endpoints para o gerenciamento de resumos de carreira gerados por IA.

### 1. Upload de Currículo (PDF)

- **Endpoint:** `POST /resumos/meus/cv`
- **Descrição:** Faz o upload de um arquivo PDF (currículo) para que a IA gere um resumo de carreira.
- **Corpo da Requisição:** `multipart/form-data`
    - **Chave:** `file`
    - **Valor:** O arquivo PDF selecionado pelo usuário.
- **Ciclo de Vida:**
    1. O usuário seleciona um arquivo PDF em um campo de upload.
    2. O front-end cria um objeto `FormData` e anexa o arquivo a ele.
    3. A requisição `POST` é enviada para `/resumos/meus/cv` com o `Content-Type` `multipart/form-data`.
    4. O back-end recebe o arquivo, o processa com a IA para extrair informações e gerar um texto de resumo.
    5. Um novo objeto `Resumo` é criado, associado ao usuário e salvo no banco de dados.
    6. Retorna o objeto `ResumoDTO` recém-criado.
- **Exemplo de Implementação (React):**

```jsx
import React, { useState } from 'react';
import axios from 'axios';

const UploadCV = () => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [isUploading, setIsUploading] = useState(false);
    const [error, setError] = useState(null);
    const [resumoGerado, setResumoGerado] = useState(null);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            setError('Por favor, selecione um arquivo PDF.');
            return;
        }

        setIsUploading(true);
        setError(null);
        setResumoGerado(null);

        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            const token = localStorage.getItem('userToken');
            const response = await axios.post('/api/resumos/meus/cv', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${token}`
                }
            });
            setResumoGerado(response.data);
        } catch (err) {
            setError('Ocorreu um erro durante o upload ou processamento do arquivo.');
            console.error(err);
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div>
            <h2>Gerar Resumo a partir do CV</h2>
            <input type="file" accept=".pdf" onChange={handleFileChange} />
            <button onClick={handleUpload} disabled={isUploading}>
                {isUploading ? 'Enviando...' : 'Enviar CV'}
            </button>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            {resumoGerado && (
                <div>
                    <h3>Resumo Gerado com Sucesso!</h3>
                    <p>{resumoGerado.texto}</p>
                </div>
            )}
        </div>
    );
};

export default UploadCV;
```
