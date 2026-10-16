# Online Product Catalogue

A role-based web application for managing and browsing a product catalogue with stock tracking and low-stock alerts, delivered through a full CI/CD pipeline (Git, Jenkins, Selenium, Docker, Ansible).

## Problem

Small retailers and local suppliers often manage products, prices and stock in spreadsheets or on paper. This leads to outdated prices, stock mismatches, slow lookups and no warning when an item is running out. This catalogue gives them one searchable, always-current source of truth.

## Features

- Register and log in as a USER; ADMIN accounts are seeded
- Browse the product catalogue
- Search by name and filter by category and price
- See each item's stock status: In stock, Low stock, Out of stock
- Admin can add and update items
- Admin sees alerts for low and zero stock (per-item threshold, default 5)
- Role-based access: users can only view, admins can manage

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot (Web, Security, Data JPA, Thymeleaf, Validation, Actuator) |
| Build | Maven |
| Database | H2 (dev), MySQL (prod) |
| Deployment | Tomcat (WAR), Docker |
| Testing | Selenium WebDriver, JUnit 5 |
| CI/CD | Jenkins |
| Config management | Ansible |

## Prerequisites

- JDK 17
- Maven 3.9 or newer
- Git

## Run Locally

```
git clone https://github.com/ghanshyam1011/online-product-catalogue.git
cd online-product-catalogue
mvn spring-boot:run
```

Open http://localhost:8080 in your browser.

## Project Structure

## Branching

See [docs/branching-strategy.md](docs/branching-strategy.md) for branch names, commit conventions and the pull request workflow.

```
.
├── docs/            documentation and task evidence
├── src/             application source code
└── pom.xml          Maven build file
```

## Roadmap

| # | Task | Status |
|---|---|---|
| 1 | Problem definition and scope | Done |
| 2 | Agile planning and DevOps workflow | Done |
| 3 | Requirements, architecture and setup | Done |
| 4 | Git and GitHub repository | Done |
| 5 | Feature 1 with branching and pull request | Planned |
| 6 | MVP completion, merge conflict, release tag | Planned |
| 7 | Jenkins installation and CI job | Planned |
| 8 | Jenkinsfile and Tomcat deployment | Planned |
| 9 | Selenium tests (local) | Planned |
| 10 | Continuous testing in Jenkins | Planned |
| 11 | Docker image and container lifecycle | Planned |
| 12 | Jenkins and Docker continuous deployment | Planned |
| 13 | Ansible configuration management | Planned |
| 14 | Provisioning, idempotency and rollback | Planned |
| 15 | Final release, documentation and viva | Planned |

