# Calculadora de Empréstimos com Ajuste de Feriados Bancários

Este projeto é uma aplicação web desenvolvida com **Java**, **Spring Boot** e **HTML/CSS/JavaScript vanilla** que realiza simulações detalhadas de parcelas de empréstimos (amortização, provisão de juros, saldo devedor, parcelas pagas, etc.). 

O grande diferencial deste sistema é a integração com um calendário customizado que **ajusta automaticamente as datas de pagamento** quando estas caem em finais de semana (sábado e domingo) ou feriados bancários brasileiros.

---

## 🛠️ Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot 4.1.0, Maven
* **Frontend:** HTML5, CSS3 (variáveis, design responsivo, transições), JavaScript vanilla
* **Biblioteca Externa:** `brazilian-calendar` (desenvolvida em Kotlin/Gradle) para verificação de feriados nacionais e bancários no Brasil.

---

## 📋 Pré-requisitos

Para rodar este projeto em sua máquina local, você precisará de:

1. **Java JDK 17** ou superior instalado e configurado nas variáveis de ambiente.
2. **Maven 3.x** instalado (ou utilize o Maven Wrapper `mvnw` incluso na raiz do projeto).

*(A biblioteca dependente `brazilian-calendar` já vem inclusa pré-compilada dentro do diretório `/libs` deste projeto, portanto não há necessidade de compilar ou baixar outros repositórios).*

---

## 🚀 Como Executar o Sistema

Siga os passos abaixo:

### Passo 1: Executar a Calculadora de Empréstimos
1. Abra o terminal na pasta raiz deste projeto (**loancalculator**).
2. Rode o servidor Spring Boot utilizando o Maven Wrapper incluso:
   * **No Windows (PowerShell/CMD):**
     ```cmd
     .\mvnw.cmd spring-boot:run
     ```
   * **No Linux/macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
3. Aguarde até ver a mensagem de sucesso no terminal indicando que a aplicação iniciou (geralmente na porta `8080`).

### Passo 2: Acessar a Interface Gráfica
1. Abra o seu navegador de preferência.
2. Acesse o endereço:
   ```text
   http://localhost:8080
   ```
3. Preencha os campos (Data Inicial, Data Final, Primeiro Pagamento, Valor e Taxa de Juros) e clique em **Calcular** para gerar a simulação.

---

## ⚙️ Detalhes da API (/api/loan/calculate)

Se desejar testar a API diretamente ou via ferramentas como Postman/Insomnia, ela aceita requisições `POST` no endpoint abaixo:

* **Endpoint:** `POST http://localhost:8080/api/loan/calculate`
* **Corpo da Requisição (JSON):**
  ```json
  {
    "initialDate": "2026-06-01",
    "finalDate": "2026-12-01",
    "firstPaymentDate": "2026-07-01",
    "loanValue": 100000.00,
    "interestRate": 7.00
  }
  ```

* **Resposta de Sucesso (JSON):** Retorna uma lista contendo a evolução diária/mensal do empréstimo com as colunas amortizadas, juros provisionados e os respectivos vencimentos ajustados por feriados.
