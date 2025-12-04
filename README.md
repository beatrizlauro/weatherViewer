# WeatherViewer App - Consumo de Web Service de Previsão do Tempo

## 1. Informações do Projeto

| | |
| :--- | :--- |
| **Nome da Aluna** | Beatriz da Costa Lauro |
| **Curso** | Sistemas de Informação |
| **Período e Disciplina** | 6° - Programação III |

## 2. Descrição da Aplicação

O **WeatherViewer App** é um aplicativo Android desenvolvido em Java, seguindo a arquitetura de Views e o padrão de consumo de API apresentado no Capítulo 7 do livro "Android 6 for Programmers".

O aplicativo permite ao usuário consultar a previsão do tempo para uma cidade específica, consumindo um Web Service REST hospedado na AWS. Os dados são processados a partir de uma resposta JSON e exibidos em uma lista (ListView), utilizando o padrão ViewHolder para otimização de performance.

**Funcionalidades:**
*   Entrada de texto para o nome da cidade (formato: Cidade,Estado,País).
*   Botão de consulta (FloatingActionButton).
*   Requisição HTTP assíncrona (AsyncTask e HttpURLConnection).
*   Processamento de JSON (org.json).
*   Exibição dos dados (data, temperatura min/max, umidade, descrição e ícone emoji).
*   Tratamento básico de erros de rede e requisição.

## 3. Instruções para Execução

1.  **Clone o Repositório:**
    ```bash
    git clone https://github.com/beatrizlauro/weatherViewer.git
    ```
2.  **Abra no Android Studio:**
    *   Abra o Android Studio e selecione `File > Open`.
    *   Navegue até a pasta do projeto clonado e selecione-a.
3.  **Sincronize o Gradle:**
    *   Aguarde o Android Studio sincronizar o projeto e baixar as dependências necessárias.
4.  **Execute:**
    *   Selecione um emulador ou dispositivo físico.
    *   Clique no botão **Run** (Play verde) na barra de ferramentas.
5.  **Teste:**
    *   No campo de texto, digite a cidade no formato **Cidade,Estado,País** (Ex: `Passos,MG,BR`).
    *   Clique no botão flutuante (lupa) para buscar a previsão.

## 4. Exemplo da URL Utilizada na Requisição

A URL é construída dinamicamente, mas o formato utilizado para a consulta de 5 dias é:

```
http://agent-weathermap-env-env.eba-6pzgqekp.us-east-2.elasticbeanstalk.com/api/weather?city=[CIDADE,ESTADO,PAÍS]&days=5&APPID=AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k1l2m3n4o5p6
```

## 5. Observações Importantes

### Variação dos Dados da Previsão:

Foi observado que os dados retornados pela API (temperaturas, descrições) podem variar ligeiramente entre consultas consecutivas (tanto no aplicativo quanto no Swagger).

**Esta variação é uma característica da API dinâmica de previsão do tempo e não um erro de implementação do aplicativo.** O código do aplicativo lê e exibe com precisão os dados fornecidos pela API no momento exato da requisição. Testes de Logcat confirmaram que o aplicativo está lendo o JSON corretamente.

**Exemplo:**
<p align="center">
![Filtros Usados no Swagger](/consultaSwagger.png)
</p>

<p align="center">
  ![Retorno da Consulta no Swagger - JSON](/retornoJSON.png)
</p>

<p align="center">
  ![Retorno no Aplicativo](/aplicacao.png)
</p>
