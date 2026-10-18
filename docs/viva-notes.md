# Viva Notes

## One-minute summary
Online Product Catalogue: a Spring Boot web app (Thymeleaf, Spring Security, JPA/H2) where users browse and search products and admins manage stock with low-stock alerts. It is delivered by a Jenkins pipeline that builds, tests with Selenium, packages a WAR, deploys to Tomcat, builds a Docker image and runs it, and is managed with Ansible playbooks.

## Likely questions
| Question | Answer |
|---|---|
| Why Git branching? | `main` is stable, `develop` integrates, feature/fix branches isolate work; merged by pull request |
| Why a Jenkinsfile? | Pipeline as code: versioned, reviewable, reproducible |
| Why was Verify failing at first? | Tomcat needs about 60 s to start the WAR; the check only waited 60 s. Extended to 3 minutes |
| Why were Selenium tests flaky on Jenkins? | They read the page right after a click. Fixed with explicit waits (WebDriverWait) |
| How are secrets handled? | Passwords come from environment variables / Jenkins credentials, never from git or the image |
| What does idempotent mean here? | Running `deploy.yml` again with the same version reports `changed=0` |
| How does rollback work? | The container label `previous_version` stores the last version; `rollback.yml` redeploys it |
| Why Ansible in Docker? | Ansible does not run natively on Windows, and this needs no WSL |
| Ports | 8080 Jenkins, 8081 Tomcat, 8082 Jenkins-deployed container, 8083 Ansible-deployed container |

## Demo order
1. Show the app and roles (user vs admin).
2. Show the Jenkins stage view and test results.
3. Open the Tomcat deployment (8081) and the container (8082).
4. Run the Ansible deploy twice, upgrade, then rollback (8083).
