<!-- ============================== -->
<!-- ⚡ CORE GATE — README Skeleton -->
<!-- ============================== -->
# ⚡ CoreGate — Arcabouço (Skeleton)

> 💳 Gateway de Pagamentos com Módulo Stand-In  
> 🧠 Arquitetura Hexagonal | 🧩 CQRS | 🛰️ SAGA | 🔗 gRPC + ISO8583  
> ☁️ Alta Disponibilidade | 🔒 Resiliência Total | 📊 Observabilidade Integrada

---

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-red?logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-green?logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Architecture-Hexagonal-blueviolet"/>
  <img src="https://img.shields.io/badge/Database-Oracle-blue?logo=oracle"/>
  <img src="https://img.shields.io/badge/Cache-Redis-red?logo=redis"/>
  <img src="https://img.shields.io/badge/Messaging-RabbitMQ-orange?logo=rabbitmq"/>
  <img src="https://img.shields.io/badge/Service%20Discovery-Consul-lightblue?logo=consul"/>
  <img src="https://img.shields.io/badge/Resilience-Resilience4j-yellow?logo=spring"/>
  <img src="https://img.shields.io/badge/Observability-Grafana%20%7C%20Kibana-orange?logo=grafana"/>
  <img src="https://img.shields.io/badge/Coverage-100%25-brightgreen?logo=sonarcloud"/>
  <img src="https://img.shields.io/badge/Container-Docker-blue?logo=docker"/>
  <img src="https://img.shields.io/badge/Orchestration-Kubernetes-blue?logo=kubernetes"/>
</p>

---

## 🚀 Visão Geral

O **CoreGate** é um *gateway de pagamentos com módulo Stand-In* e **arquitetura hexagonal multimódulo**.  
Foi concebido para operar tanto como **fintech parceira de bancos** quanto como **plataforma white label para lojistas**.

O foco principal é oferecer **resiliência**, **escalabilidade** e **observabilidade total**, com pilares sólidos de **Clean Architecture**, **DDD**, **SOLID** e **Design Patterns**.

---

## 🧭 Formas de Atuação

### 🏦 **Parceiro de Banco**
O CoreGate atua como **fintech integradora**, processando transações ISO8583 em tempo real e se comunicando diretamente com adquirentes e emissores.

### 🏷️ **White Label para Lojistas**
Permite que grandes varejistas utilizem o motor de pagamento CoreGate sob sua própria marca, com regras, conciliação e relatórios dedicados.

---

## ⚙️ Modos de Operação

| 🧭 Modo | Descrição | Cenário Ideal |
|----------|------------|----------------|
| **Gateway** | Processamento online via ISO8583/gRPC, roteando para adquirentes/emissores. | Operação normal em tempo real. |
| **Stand-In** | Fallback inteligente que autoriza localmente via cache + regras + limites. | Falhas temporárias do emissor/adquirente. |

---

## 🧱 Estrutura Modular

| Módulo | Descrição |
|--------|------------|
| **Ingress** | Camada de entrada: recebe ISO8583, REST ou gRPC, faz autenticação e roteamento inicial. Utiliza **Netty** para comunicação via **socket** e protocolos binários. |
| **Context (ISO8583 Engine)** | Faz *encode/decode* das mensagens ISO8583 e gerencia campos de bitmaps. |
| **Orchestrator** | Coordena o fluxo transacional e implementa o *SAGA Pattern*. |
| **Rules** | Motor de regras antifraude, scoring e políticas de autorização. |
| **Finalizer** | Fecha o ciclo de vida da transação, emitindo respostas e persistindo históricos. |
| **Advice** | Manipula mensagens de reversão e advice (0800, 0810, 0420, 0430). |
| **Data** | Camada de persistência (Oracle/Redis), CQRS, eventos de mensageria e *fallbacks*. |

---

## 💪 Resiliência

| Camada | Estratégia | Tecnologias |
|---------|-------------|-------------|
| **Infraestrutura** | Descoberta, balanceamento e failover automáticos | 🧭 *Consul*, 🧱 *HAProxy* |
| **Dados** | Controle de consistência e compensação distribuída | 🔄 *SAGA*, ⚙️ *CQRS* |
| **Aplicação** | Tolerância a falhas, *circuit breaker*, *retry* e *fallback* | 🧩 *Resilience4j* |

---

## 📊 Observabilidade e Qualidade

| Recurso | Descrição | Ferramentas |
|----------|------------|-------------|
| **Monitoramento** | Dashboards em tempo real, alertas e métricas | 📈 *Grafana* |
| **Logs e Tracing** | Coleta centralizada de logs, tracing distribuído | 🪶 *Kibana / Elastic Stack* |
| **Análise de Código** | SonarQube integrado em pipeline CI/CD | 🧠 *SonarQube / SonarCloud* |
| **Testes Automatizados** | Cobertura mínima exigida: **100%** | 🧪 *JUnit5*, *Mockito*, *Cucumber (BDD)* |

---

## 🧩 Princípios Arquiteturais

- 🧱 **Clean Architecture** — separação de domínios e camadas.
- 🧩 **Hexagonal (Ports & Adapters)** — isolamento completo entre core e infraestrutura.
- 🧠 **DDD (Domain-Driven Design)** — foco no domínio e ubiquidade de linguagem.
- ⚙️ **SOLID Principles** — código limpo, extensível e manutenível.
- 🧬 **Design Patterns** — *Factory*, *Strategy*, *Builder*, *Adapter*, *Observer* etc.
- 📜 **API First** — documentação automática via *Swagger/OpenAPI*.
- 🧰 **BDD** — especificações executáveis com *Cucumber*.

---

## 🧠 Fluxo Simplificado

---

## 📡 Fluxo de Autorização e Stand-In

O diagrama a seguir detalha o ciclo de vida completo de uma transação no **CoreGate**, cobrindo o caminho **online (gateway)** e o **modo fallback (stand-in)** em caso de indisponibilidade do emissor.


### mermaid
flowchart LR
  A[Cliente / POS / App] -->|ISO8583 / gRPC| B[Ingress]
  B --> C[Context (ISO8583)]
  C --> D[Orchestrator]
  D --> E[Rules]
  E --> F[Finalizer]
  D --> G[Advice]
  D --> H[Data]
  H -->|Oracle| O[(Oracle)]
  H -->|Redis| R[(Redis)]
  O --> I[(Banco / Stand-In)]
  R --> I
  I --> J[(Alta Disponibilidade)]


Pilares de HA/Resiliência:

☁️ Alta Disponibilidade e Escalabilidade

🧭 Service Discovery com Consul

🧱 Load Balancing e Failover com HAProxy

🐳 Containerização com Docker

☸️ Orquestração e AutoScale com Kubernetes

📫 Mensageria distribuída com RabbitMQ

📈 Horizontal Scaling por módulo de contexto

🧰 Setup Rápido
# Clone o repositório
git clone https://github.com/andersonfabio1976/CoreGate.git

# Build multimódulo
mvn clean package -DskipTests

# Executar aplicação
java -jar ingress/target/coregate.jar

🧭 Roadmap

 Estrutura multimódulo inicial
 Engine ISO8583 e Orquestrador
 Integração SAGA + CQRS + RabbitMQ
 Resiliência infra, dados e aplicação
 Observabilidade completa (Grafana + Kibana)
 Painel administrativo (CoreGate Console)
 Migração para microserviços
 Integração com adquirentes reais (Visa, Mastercard, Elo)

🖼️ Logo (Spring Boot Banner)
O logo é exibido no startup da aplicação (src/main/resources/banner.txt).

## 🖼️ **📜 banner.txt**

```text
   _____                  _____       _
  / ____|                / ____|     | |
 | |     ___  _ __ ___  | |  __  __ _| |_ ___
 | |    / _ \| '_ ` _ \ | | |_ |/ _` | __/ _ \
 | |___| (_) | | | | | || |__| | (_| | ||  __/
  \_____\___/|_| |_| |_| \_____|\__,_|\__\___|
          🔗  C O R E G A T E  🔗
   Gateway • Stand-In • Resilience • Observability
===================================================
 :: Initializing CoreGate Engine ::
 :: Loading Modules: Ingress | Context | Orchestrator | Rules ::
 :: Starting Consul, HAProxy, RabbitMQ, Redis... ::
 :: Monitoring via Grafana & Kibana ::
 :: System Ready 🚀 ::

📜 Licença
Distribuído sob licença MIT — consulte LICENSE para mais detalhes.


flowchart TB
  %% =======================
  %% FLUXO PRINCIPAL GATEWAY
  %% =======================
  subgraph Gateway Online
    A1[Cliente / POS / App] -->|ISO8583 / gRPC| B1[Ingress]
    B1 -->|TransactionCommand| C1[AuthorizeTransactionService]
    C1 -->|Consulta| D1[TenantRepositoryPort]
    C1 -->|Consulta| E1[MerchantRepositoryPort]
    C1 -->|Cria| F1[Transaction (Domain)]
    C1 -->|Autoriza| G1[Transaction.authorize()]
    C1 -->|Persiste| H1[TransactionRepositoryPort]
    C1 -->|Gera Advice| I1[AdviceGenerationService]
    I1 -->|Publica| J1[(RabbitMQ Advice Topic)]
  end

  %% =======================
  %% FLUXO STAND-IN Fallback
  %% =======================
  subgraph Stand-In Fallback
    B2[Ingress (Offline Detected)] -->|Reprocessa| C2[StandInEvaluationService]
    C2 -->|Carrega Política| D2[Tenant.StandInPolicy]
    D2 -->|Avalia Limites e Janela| E2[StandInDomainService]
    E2 -->|Decisão| F2[Transaction.applyStandInDecision()]
    F2 -->|Persiste| G2[TransactionRepositoryPort]
    F2 -->|Gera Advice Local| H2[AdviceGenerationService]
    H2 -->|Publica| I2[(RabbitMQ Advice Topic)]
  end

  %% =======================
  %% LIGAÇÕES VISUAIS
  %% =======================
  A1 -. Falha conexão emissor .-> B2
  G1 --> DB1[(Oracle / Redis)]
  G2 --> DB1
  J1 --> MON1[(Grafana / Kibana Monitor)]
  I2 --> MON1

  %% =======================
  %% ESTILOS
  %% =======================
  classDef ingress fill:#e0f7fa,stroke:#0097a7,stroke-width:2px;
  classDef app fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
  classDef domain fill:#fff3e0,stroke:#ef6c00,stroke-width:2px;
  classDef infra fill:#ede7f6,stroke:#6a1b9a,stroke-width:2px;

  class B1,B2 ingress;
  class C1,I1,C2,E2,H2 app;
  class F1,F2,D2 domain;
  class G1,G2,DB1,J1,I2,MON1 infra;

| Etapa                                  | Descrição                                                                                            |
| -------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| **Ingress**                            | Recebe a mensagem ISO8583 via socket (Netty).                                                        |
| **AuthorizeTransactionService**        | Caso normal de gateway: valida tenant e merchant, cria a transação e solicita autorização.           |
| **StandInEvaluationService**           | Caminho alternativo quando o emissor/adquirente está offline. Avalia regras locais e aplica decisão. |
| **Tenant.StandInPolicy**               | Define limites, janelas de tempo e marcas permitidas.                                                |
| **StandInDomainService**               | Avalia se o fallback pode aprovar a transação.                                                       |
| **Transaction.applyStandInDecision()** | Marca a transação com `standInApplied=true` e define o código de resposta.                           |
| **AdviceGenerationService**            | Publica mensagens de *clearing/advice* (RabbitMQ).                                                   |
| **Oracle / Redis**                     | Persistência e cache distribuído das transações.                                                     |
| **Grafana / Kibana**                   | Observabilidade e rastreabilidade dos fluxos.                                                        |

---

## 🧩 Arquitetura Hexagonal — CoreGate

A figura abaixo demonstra a separação de responsabilidades na arquitetura **Hexagonal / Clean Architecture** do CoreGate,  
onde o núcleo de domínio permanece isolado, e os módulos externos se comunicam com ele apenas através de **ports**.



flowchart TB
    %% =========================
    %% CAMADAS DO CORE
    %% =========================
    subgraph Domain["🏛️ Domain Layer"]
      D1[Transaction]:::domain
      D2[Tenant]:::domain
      D3[Merchant]:::domain
      D4[StandInPolicy]:::domain
      D5[Advice]:::domain
    end

    subgraph Application["🧠 Application Layer"]
      A1[AuthorizeTransactionService]:::application
      A2[StandInEvaluationService]:::application
      A3[AdviceGenerationService]:::application
      A4[Ports In / Out]:::application
    end

    subgraph Adapters_In["🌐 Adapters IN (Drivers)"]
      IN1[Ingress (Netty Socket)]:::adapter
      IN2[Context (ISO8583 Engine)]:::adapter
      IN3[Orchestrator (SAGA)]:::adapter
      IN4[Rules Engine (Fraude/Score)]:::adapter
    end

    subgraph Adapters_Out["💾 Adapters OUT (Driven)"]
      OUT1[Data (Oracle/Redis)]:::adapter
      OUT2[Advice Publisher (RabbitMQ)]:::adapter
      OUT3[Service Discovery (Consul)]:::adapter
      OUT4[Resilience Layer (HAProxy / Resilience4j)]:::adapter
    end

    %% =========================
    %% RELAÇÕES INTERNAS
    %% =========================
    IN1 --> A1
    IN2 --> A1
    A1 -->|fallback| A2
    A1 -->|gera advice| A3
    A2 --> D4
    A3 --> D5
    A1 --> D1
    D1 --> OUT1
    D5 --> OUT2
    A1 --> OUT4
    OUT1 --> OUT3

    %% =========================
    %% ESTILOS
    %% =========================
    classDef domain fill:#fff3e0,stroke:#ef6c00,stroke-width:2px;
    classDef application fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
    classDef adapter fill:#e0f7fa,stroke:#0097a7,stroke-width:2px;

| Camada                    | Responsabilidade                                                                 | Exemplos                                                                             |
| ------------------------- | -------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------ |
| **Adapters IN (Drivers)** | Interfaces de entrada — recebem requisições externas (POS, gRPC, REST, ISO8583). | `Ingress`, `Context`, `Orchestrator`, `Rules`                                        |
| **Application Layer**     | Core de orquestração e coordenação de casos de uso.                              | `AuthorizeTransactionService`, `StandInEvaluationService`, `AdviceGenerationService` |
| **Domain Layer**          | Núcleo puro do negócio, sem dependências externas.                               | `Transaction`, `Tenant`, `StandInPolicy`, `Advice`                                   |
| **Adapters OUT (Driven)** | Portas de saída que implementam persistência, mensageria, cache e resiliência.   | `Data`, `AdvicePublisher`, `Consul`, `HAProxy`, `Redis`, `Oracle`                    |

---

## 📦 Estrutura Modular — Dependências Maven

O gráfico abaixo representa as dependências entre os módulos Maven do **CoreGate**, evidenciando o isolamento entre o **Domínio (core)**, a **Aplicação (orquestração)** e os **Adapters (entrada e saída)** dentro da arquitetura Hexagonal.



flowchart TD
    %% =========================
    %% MÓDULOS PRINCIPAIS
    %% =========================
    CORE[CoreGate (Parent)]:::parent

    subgraph CORE_MODULES["🏗️ Módulos Principais"]
      DOMAIN[domain]:::domain
      APPLICATION[application]:::application
      INGRESS[ingress]:::adapter_in
      CONTEXT[context]:::adapter_in
      ORCHESTRATOR[orchestrator]:::adapter_in
      RULES[rules]:::adapter_in
      FINALIZER[finalizer]:::adapter_in
      ADVICE[advice]:::adapter_out
      DATA[data]:::adapter_out
      INFRA[infrastructure]:::infra
    end

    %% =========================
    %% RELAÇÕES
    %% =========================
    CORE --> DOMAIN
    CORE --> APPLICATION
    CORE --> INGRESS
    CORE --> CONTEXT
    CORE --> ORCHESTRATOR
    CORE --> RULES
    CORE --> FINALIZER
    CORE --> ADVICE
    CORE --> DATA
    CORE --> INFRA

    APPLICATION --> DOMAIN
    INGRESS --> APPLICATION
    CONTEXT --> APPLICATION
    ORCHESTRATOR --> APPLICATION
    RULES --> APPLICATION
    FINALIZER --> APPLICATION
    ADVICE --> APPLICATION
    DATA --> APPLICATION
    INFRA --> DATA
    INFRA --> ADVICE

    %% =========================
    %% ESTILOS
    %% =========================
    classDef parent fill:#e3f2fd,stroke:#1565c0,stroke-width:2px,color:#0d47a1;
    classDef domain fill:#fff3e0,stroke:#ef6c00,stroke-width:2px;
    classDef application fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
    classDef adapter_in fill:#e0f7fa,stroke:#0097a7,stroke-width:2px;
    classDef adapter_out fill:#ede7f6,stroke:#6a1b9a,stroke-width:2px;
    classDef infra fill:#fce4ec,stroke:#ad1457,stroke-width:2px;

| Camada             | Tipo de Módulo                        | Exemplo                                                    | Descrição                                     |
| ------------------ | ------------------------------------- | ---------------------------------------------------------- | --------------------------------------------- |
| **Parent**         | Gerenciador de dependências e plugins | `coregate (root pom)`                                      | Define BOM, versionamento e herança.          |
| **Domain**         | Núcleo puro do negócio                | `Transaction`, `Tenant`, `StandInPolicy`                   | Sem dependências externas.                    |
| **Application**    | Casos de uso, orquestração e ports    | `AuthorizeTransactionService`                              | Depende apenas do `domain`.                   |
| **Adapters IN**    | Pontos de entrada                     | `Ingress`, `Context`, `Orchestrator`, `Rules`, `Finalizer` | Usam Spring Boot, recebem requisições.        |
| **Adapters OUT**   | Pontos de saída                       | `Data`, `Advice`                                           | Implementam persistência, mensageria, advice. |
| **Infrastructure** | Suporte técnico comum                 | `Resilience4j`, `HAProxy`, `Consul`, `Grafana`             | Infra e observabilidade compartilhadas.       |

---

## ⚙️ Pipeline de Build e Execução — CoreGate Engine

O diagrama abaixo descreve o ciclo completo de build e execução do **CoreGate**, desde o pipeline Maven até a inicialização dos containers Docker e dos módulos principais da aplicação (Ingress, Context, Orchestrator, Application e Domain).


flowchart LR
    %% =========================
    %% CICLO DE BUILD / EXECUÇÃO
    %% =========================
    A1[👨‍💻 Dev / CI Pipeline] -->|commit & push| B1[🔧 Maven Build]
    B1 -->|mvn clean package| B2[📦 Multimódulo Build]
    B2 -->|gera artefatos JAR| C1[🏗️ target/coregate-*.jar]
    C1 -->|docker build| D1[🐳 Docker Image: coregate/ingress]
    D1 -->|docker-compose up| D2[🧩 Docker Compose Orchestrator]

    %% =========================
    %% CONTAINERS / INFRA
    %% =========================
    subgraph DOCKER["Docker Network"]
      D2 --> DB[(🧮 Oracle DB)]
      D2 --> RDS[(⚡ Redis Cache)]
      D2 --> MQ[(📬 RabbitMQ Broker)]
      D2 --> CSL[(🧭 Consul Service Discovery)]
      D2 --> PRX[(🧱 HAProxy Load Balancer)]
      D2 --> MON[(📈 Grafana / Kibana Observability)]
      D2 --> APP[🚀 CoreGate Ingress (Spring Boot)]
    end

    %% =========================
    %% BOOT SEQUENCE
    %% =========================
    subgraph BOOT["Spring Boot Startup"]
      APP -->|Inicializa Contexto| E1[🔗 Context (ISO8583 Engine)]
      E1 -->|Conecta| E2[🛰️ Orchestrator (SAGA Coordinator)]
      E2 -->|Invoca| E3[🧠 Application Services]
      E3 -->|Manipula| E4[🏛️ Domain Core]
      E3 -->|Persiste| DB
      E3 -->|Publica Eventos| MQ
      E3 -->|Atualiza Cache| RDS
      E3 -->|Reporta| MON
    end

    %% =========================
    %% ESTILOS
    %% =========================
    classDef dev fill:#fff3e0,stroke:#ef6c00,stroke-width:2px;
    classDef build fill:#e0f7fa,stroke:#0097a7,stroke-width:2px;
    classDef infra fill:#ede7f6,stroke:#6a1b9a,st

| Etapa                    | Descrição                                                                                                             |
| ------------------------ | --------------------------------------------------------------------------------------------------------------------- |
| **Maven Build**          | Compila todos os módulos (Domain, Application, Adapters) e gera artefatos JAR.                                        |
| **Docker Build**         | Cria a imagem `coregate/ingress` contendo o motor Spring Boot + módulos core.                                         |
| **Docker Compose**       | Orquestra containers de infraestrutura: Oracle, Redis, RabbitMQ, Consul, HAProxy e Observability.                     |
| **Spring Boot Startup**  | Inicia o **Ingress**, carrega o **Context (ISO8583)**, registra serviços no Consul e inicializa a aplicação CoreGate. |
| **Orchestrator (SAGA)**  | Coordena as transações e eventos de compensação.                                                                      |
| **Application Services** | Executam casos de uso (`AuthorizeTransaction`, `StandInEvaluation`, `AdviceGeneration`).                              |
| **Domain Core**          | Regras de negócio puras — sem dependências externas.                                                                  |
| **Observability Stack**  | Grafana e Kibana monitoram métricas, logs e tracing distribuído.                                                      |


---

## 📊 Observabilidade e Monitoramento — CoreGate Stack

O diagrama abaixo demonstra a integração completa de observabilidade do **CoreGate**,  
onde métricas, logs e traces fluem continuamente do motor transacional para o stack **Grafana + Prometheus + Kibana + Jaeger**,  
garantindo rastreabilidade total e suporte a análises em tempo real.


flowchart LR
    %% =========================
    %% CAMADAS PRINCIPAIS
    %% =========================
    subgraph CORE["🚀 CoreGate Application"]
      INGRESS[Ingress<br/>🧩 Netty / Spring Boot]
      CONTEXT[Context<br/>⚙️ ISO8583 Engine]
      ORCH[Orchestrator<br/>🛰️ SAGA / CQRS]
      APP[Application Services<br/>🧠 Use Cases]
      DOMAIN[Domain Core<br/>🏛️ Regras de Negócio]
    end

    subgraph INFRA["🧱 Infrastructure / Runtime"]
      DB[(🧮 Oracle DB)]
      REDIS[(⚡ Redis Cache)]
      MQ[(📬 RabbitMQ Broker)]
      CONSUL[(🧭 Consul)]
      HAPROXY[(🧱 HAProxy)]
    end

    subgraph OBS["📈 Observability Stack"]
      PROM[Prometheus<br/>📊 Metrics Collector]
      LOKI[Loki / Elastic<br/>🪶 Centralized Logs]
      TEMPO[Jaeger / Tempo<br/>🔍 Tracing Distributed]
      GRAF[Grafana<br/>📊 Dashboards]
      KIBANA[Kibana<br/>🧠 Log Analytics]
    end

    %% =========================
    %% FLUXOS DE OBSERVABILIDADE
    %% =========================
    INGRESS -->|Micrometer Metrics| PROM
    CONTEXT -->|Custom Metrics| PROM
    APP -->|Business Metrics| PROM
    DOMAIN -->|Domain KPIs| PROM

    INGRESS -->|Logs JSON| LOKI
    CONTEXT -->|Logs e Exceptions| LOKI
    APP -->|Log Events| LOKI
    DOMAIN -->|Audit Logs| LOKI

    INGRESS -->|Trace ID / Span| TEMPO
    ORCH -->|Distributed Traces| TEMPO
    APP -->|SAGA Correlation| TEMPO

    PROM --> GRAF
    LOKI --> GRAF
    TEMPO --> GRAF
    LOKI --> KIBANA

    %% =========================
    %% FLUXOS SECUNDÁRIOS
    %% =========================
    GRAF -->|Dashboards e Alertas| DEV[(👨‍💻 DevOps / SRE)]
    KIBANA --> DEV

    %% =========================
    %% ESTILOS
    %% =========================
    classDef core fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
    classDef infra fill:#ede7f6,stroke:#6a1b9a,stroke-width:2px;
    classDef obs fill:#fff3e0,stroke:#ef6c00,stroke-width:2px;
    classDef ext fill:#fce4ec,stroke:#ad14

| Tipo                     | Origem                  | Destino                  | Função                                                               |
| ------------------------ | ----------------------- | ------------------------ | -------------------------------------------------------------------- |
| **Métricas (Metrics)**   | Micrometer / Actuator   | Prometheus → Grafana     | KPIs de negócio e performance de módulos.                            |
| **Logs (Logging)**       | Logback / JSON Appender | Loki / Elastic → Kibana  | Centraliza logs por tenant, transação e módulo.                      |
| **Traces (Tracing)**     | Sleuth / OpenTelemetry  | Jaeger / Tempo → Grafana | Traça o caminho completo da transação (gateway → stand-in → advice). |
| **Dashboards / Alertas** | Grafana                 | DevOps / SRE             | Monitora throughput, latência, erros e fallback stand-in.            |

---

## 💪 Arquitetura de Resiliência — CoreGate

O diagrama abaixo demonstra o ecossistema de resiliência do **CoreGate**,  
com redundância em **três níveis** — infraestrutura, dados e aplicação — garantindo alta disponibilidade, tolerância a falhas e recuperação automática.


flowchart TB
    %% =========================
    %% CAMADAS DE RESILIÊNCIA
    %% =========================
    subgraph INFRA["☁️ Infraestrutura — Alta Disponibilidade"]
      CONSUL[(🧭 Consul<br/>Service Discovery)]
      HAPROXY[(🧱 HAProxy<br/>Load Balancer)]
      DOCKER[(🐳 Docker + ☸️ Kubernetes<br/>AutoScale & Failover)]
      MON[(📈 Grafana / Kibana<br/>Health Checks & Alerts)]
    end

    subgraph DADOS["🧮 Dados — Consistência e Compensação"]
      SAGA[(🔄 Saga Pattern<br/>Transações Distribuídas)]
      CQRS[(⚙️ CQRS<br/>Command / Query Separation)]
      REDIS[(⚡ Redis Cache<br/>Fallback Cache Layer)]
      ORACLE[(🏦 Oracle DB<br/>Persistência Transacional)]
    end

    subgraph APLICACAO["🧠 Aplicação — Tolerância a Falhas"]
      CB[(🧩 Circuit Breaker<br/>Resilience4j)]
      RETRY[(🔁 Retry Policy<br/>Exponential Backoff)]
      FALLBACK[(🪂 Fallback<br/>Stand-In Mode)]
      TIMEOUT[(⏱ Timeout + Bulkhead<br/>Isolamento de Threads)]
    end

    %% =========================
    %% FLUXOS DE PROTEÇÃO
    %% =========================
    APP[🚀 CoreGate Engine] --> APLICACAO
    APLICACAO --> DADOS
    DADOS --> INFRA
    INFRA -->|Health Status| APP

    CB -->|Erro Externo| FALLBACK
    FALLBACK -->|Stand-In Transaction| REDIS
    SAGA -->|Compensação| ORACLE
    CQRS -->|Sincronização| REDIS
    HAPROXY -->|Failover Requests| APP
    CONSUL -->|Service Registry| HAPROXY
    DOCKER -->|Escalabilidade| APP
    MON -->|Alertas / Dashboards| DEV[(👨‍💻 DevOps / NOC)]

    %% =========================
    %% ESTILOS
    %% =========================
    classDef infra fill:#e3f2fd,stroke:#1565c0,stroke-width:2px;
    classDef dados fill:#fff3e0,stroke:#ef6c00,stroke-width:2px;
    classDef aplicacao fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
    classDef core fill:#ede7f6,stroke:#6a1b9a,stroke-width:2px;
    classDef dev fill:#fce4ec,stroke:#ad1457,stroke-width:2px;

    class INFRA,HAPROXY,CONSUL,DOCKER,MON infra;
    class DADOS,SAGA,CQRS,REDIS,ORACLE dados;
    class APLICACAO,CB,RETRY,FALLBACK,TIMEOUT aplicacao;
    class APP core;
    class DEV dev;

| Camada             | Mecanismo                           | Objetivo                                                                    | Tecnologias                                             |
| ------------------ | ----------------------------------- | --------------------------------------------------------------------------- | ------------------------------------------------------- |
| **Infraestrutura** | Descoberta e failover automático    | Garante disponibilidade via *HAProxy + Consul + Kubernetes*.                | 🧭 *Consul*, 🧱 *HAProxy*, 🐳 *Docker*, ☸️ *Kubernetes* |
| **Dados**          | Consistência eventual e compensação | Evita corrupção e perda de dados com *Saga* e *CQRS*.                       | 🔄 *SAGA*, ⚙️ *CQRS*, 🧮 *Oracle*, ⚡ *Redis*            |
| **Aplicação**      | Resiliência lógica e operacional    | Isola falhas externas com *Resilience4j* (CircuitBreaker, Retry, Fallback). | 🧩 *Resilience4j*                                       |


---

## 📘 Referência Técnica do Arcabouço — CoreGate Skeleton

Esta seção consolida as tecnologias e padrões que sustentam o **CoreGate**,  
demonstrando seu caráter de arquitetura de referência (*Reference Architecture*)  
para soluções de **gateway de pagamentos com módulo Stand-In** e **resiliência total de infraestrutura**.

Abaixo está a relação das principais tecnologias, frameworks e padrões que compõem o CoreGate,
distribuídas por responsabilidade arquitetural dentro da estrutura Hexagonal + Clean Architecture + DDD + SOLID.

🧱 Camada de Domínio (Domain Layer)

Responsável pelo núcleo do negócio e invariantes de domínio.

☕ Java 21 — linguagem base com recursos de records, sealed classes e pattern matching.

📦 DDD (Domain-Driven Design) — modelagem de entidades ricas e value objects.

⚙️ Design Patterns — Factory, Strategy, Observer, Builder, Adapter.


🧠 Camada de Aplicação (Application Layer)

Coordena o fluxo entre os módulos, casos de uso e portas de comunicação.

🧩 Spring Boot 3.x (Core Context) — gerência de beans, injeção de dependência e ciclo de vida.

🔄 SAGA Pattern — compensação distribuída e consistência eventual.

⚙️ CQRS — separação entre comandos e queries de forma reativa.

🧱 Resilience4j — Circuit Breaker, Retry, Timeout e Fallback para tolerância a falhas.

🔌 gRPC (Protobuf) — comunicação binária performática entre módulos e serviços externos.

🧾 ISO8583 Engine (Context) — encoding/decoding binário das mensagens financeiras.



🌐 Camada de Entrada (Ingress / Drivers)

Responsável por receber transações, autenticar e rotear mensagens.

🧩 Netty (NIO) — servidor de socket assíncrono e não-bloqueante para tráfego ISO8583.

🚦 Spring WebFlux / Reactor — suporte reativo opcional para fluxos HTTP/gRPC.

🛡️ Spring Security + JWT / Keycloak (futuro) — autenticação e autorização OIDC.

📬 RabbitMQ (Ingress Listener) — fila de entrada para requisições assíncronas.


💾 Camada de Saída (Data / Driven Ports)

Responsável pela persistência, cache e mensageria de saída.

🧮 Oracle Database — persistência relacional de transações e tenants.

⚡ Redis — cache distribuído e suporte ao modo Stand-In offline.

🐇 RabbitMQ — publisher/subscriber para eventos e mensagens advice.

🧭 Consul — service discovery e configuração distribuída.

🧱 HAProxy — balanceamento de carga e health check automático.


🪶 Camada de Observabilidade (Monitoring / APM)

Responsável por rastreamento, monitoramento e análise de performance.

📈 Grafana — dashboards e visualização de métricas.

📊 Prometheus — coleta de métricas via Micrometer / Spring Actuator.

🪶 Loki / Elastic Stack (ELK) — coleta e indexação de logs centralizados.

🔍 Jaeger / Tempo — rastreamento distribuído (tracing).

🧠 SonarQube / SonarCloud — análise estática e cobertura de testes.


🧩 Camada de Containerização e Orquestração

Garante portabilidade, alta disponibilidade e auto-scale.

🐳 Docker / Docker Compose — empacotamento e execução isolada dos módulos.

☸️ Kubernetes — orquestração, health probes e auto-restart de pods.

🧠 CI/CD (Jenkins / GitHub Actions) — pipeline de integração e deploy automatizado.


🧬 Padrões Arquiteturais e Conceituais

🧩 Arquitetura Hexagonal (Ports & Adapters) — isolamento entre core e infraestrutura.

🧱 Clean Architecture — camadas concêntricas com dependência apenas do core.

🧠 DDD Tactical Patterns — Aggregates, Repositories, Value Objects, Domain Services.

🔗 API First / OpenAPI — documentação e geração de contratos automáticos.

🧰 BDD / Cucumber — cenários executáveis e especificações comportamentais.


| Categoria              | Tecnologia                           | Finalidade                           |
| ---------------------- | ------------------------------------ | ------------------------------------ |
| **Core / Framework**   | Spring Boot 3.x, Java 21             | Contexto de aplicação e boot reativo |
| **Domínio / Negócio**  | DDD, Clean Architecture              | Núcleo do domínio isolado            |
| **Mensageria**         | RabbitMQ                             | Eventos e Advice                     |
| **Banco de Dados**     | Oracle                               | Persistência principal               |
| **Cache / Stand-In**   | Redis                                | Fallback e cache distribuído         |
| **Resiliência**        | Resilience4j, CQRS, SAGA             | Tolerância e compensação             |
| **Service Discovery**  | Consul                               | Registro e balanceamento dinâmico    |
| **Balanceamento**      | HAProxy                              | Distribuição de requisições          |
| **Observabilidade**    | Prometheus, Grafana, Kibana, Jaeger  | Métricas, logs e tracing             |
| **Containerização**    | Docker, Kubernetes                   | Deploy e autoescala                  |
| **Qualidade / Testes** | JUnit5, Mockito, Cucumber, SonarQube | Cobertura 100% e BDD                 |
| **Comunicação**        | Netty, gRPC (Protobuf), ISO8583      | Tráfego binário performático         |


#   c o r e g a t e - v 1  
 