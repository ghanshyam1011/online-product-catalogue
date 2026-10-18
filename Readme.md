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
- Chrome (for the Selenium tests)
- Docker Desktop (for the container and Ansible parts)

## Run Locally

```
git clone https://github.com/ghanshyam1011/online-product-catalogue.git
cd online-product-catalogue
mvn spring-boot:run
```

Open http://localhost:8080 in your browser.

Accounts are seeded only when passwords are provided as environment variables:
`ADMIN_PASSWORD` (admin@catalogue.local) and `USER_PASSWORD` (user@catalogue.local). Emails can be changed with `ADMIN_EMAIL` and `USER_EMAIL`. Anyone can also register as a USER.

## Tests

```
mvn test
```

Runs the Spring context test and 12 Selenium WebDriver tests (headless Chrome) covering login, registration, search, category filter, stock status badges, role-based access, admin add item and logout.

## CI/CD Pipeline

The `Jenkinsfile` runs these stages on every build of `develop`:

| Stage | What it does |
|---|---|
| Checkout | Pulls the code from GitHub |
| Build | `mvn clean compile` |
| Test | `mvn test` (Selenium tests) and publishes JUnit results |
| Package | Builds the executable WAR and archives it |
| Deploy | Copies the WAR to Tomcat (`catalogue-dev`) on port 8081 |
| Verify | Waits for `/login` to return 200 |
| Docker Build | Builds `product-catalogue:<build number>` |
| Docker Deploy | Runs the container on port 8082 with passwords from Jenkins credentials |
| Docker Verify | Waits for the container's `/login` to return 200 |

Jenkins credentials needed: `catalogue-admin-password` and `catalogue-user-password` (secret text).

## Docker

```
mvn -B package -DskipTests
docker build -t product-catalogue:1.0 .
docker run -d --name catalogue -p 8082:8080 -e ADMIN_PASSWORD=<pass> -e USER_PASSWORD=<pass> product-catalogue:1.0
```

See [docs/docker-commands.md](docs/docker-commands.md).

## Ansible

Playbooks in `ansible/` deploy and roll back the container. Ansible runs inside a Docker container, so nothing is installed on Windows. See [docs/ansible.md](docs/ansible.md).

## Branching

See [docs/branching-strategy.md](docs/branching-strategy.md) for branch names, commit conventions and the pull request workflow.

## Project Structure

```
.
├── ansible/         Ansible deploy and rollback playbooks
├── docs/            documentation and task evidence
├── src/             application source and Selenium tests
├── Dockerfile       container image
├── Jenkinsfile      CI/CD pipeline
└── pom.xml          Maven build file
```

## Roadmap

| # | Task | Status |
|---|---|---|
| 1 | Problem definition and scope | Done |
| 2 | Agile planning and DevOps workflow | Done |
| 3 | Requirements, architecture and setup | Done |
| 4 | Git and GitHub repository | Done |
| 5 | Feature 1 with branching and pull request | Done |
| 6 | MVP completion, merge conflict, release tag | Done |
| 7 | Jenkins installation and CI job | Done |
| 8 | Jenkinsfile and Tomcat deployment | Done |
| 9 | Selenium tests (local) | Done |
| 10 | Continuous testing in Jenkins | Done |
| 11 | Docker image and container lifecycle | Done |
| 12 | Jenkins and Docker continuous deployment | Done |
| 13 | Ansible configuration management | Done |
| 14 | Provisioning, idempotency and rollback | Done |
| 15 | Final release, documentation and viva | Done |

