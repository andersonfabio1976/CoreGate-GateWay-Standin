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

### 🏦 Parceiro de Banco
O CoreGate atua como **fintech integradora**, processando transações ISO8583 em tempo real e se comunicando diretamente com adquirentes e emissores.

### 🏷️ White Label para Lojistas
Permite que grandes varejistas utilizem o motor de pagamento CoreGate sob sua própria marca, com regras, conciliação e relatórios dedicados.

---

## ⚙️ Modos de Operação

| 🧭 Modo | Descrição | Cenário Ideal |
|:--------|:-----------|:---------------|
| **Gateway** | Processamento online via ISO8583/gRPC, roteando para adquirentes/emissores. | Operação normal em tempo real. |
| **Stand-In** | Fallback inteligente que autoriza localmente via cache + regras + limites. | Falhas temporárias do emissor/adquirente. |

---

## 💡 Fluxo de Autorização e Stand-In

O diagrama a seguir detalha o ciclo de vida completo de uma transação no **CoreGate**, cobrindo o caminho **online (gateway)** e o **modo fallback (stand-in)** em caso de indisponibilidade do emissor.

```mermaid
flowchart LR
  A[Cliente / POS / App] -->|ISO8583 / gRPC| B[Ingress]
  B --> C[Context ISO8583]
  C --> D[Orquestrator]
  D --> E[Rules]
  E --> F[Finalizer]
  D --> G[Advice]
  D --> H[Data]
  H -->|Oracle| O[Oracle DB]
  H -->|Redis| R[Redis Cache]
  O --> I[Banco / Stand-In]
  R --> I
  I --> J[Alta Disponibilidade]
