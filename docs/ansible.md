# Ansible (Tasks 13 and 14)

Ansible runs inside a Docker container (`ansible/Dockerfile`) and manages the Docker host
through the mounted Docker socket with the `community.docker` collection. Nothing is
installed on Windows.

| File | Purpose |
|---|---|
| `ansible/deploy.yml` | Provision the network and run the catalogue container, then wait for `/login` |
| `ansible/rollback.yml` | Redeploy the version recorded in the container's `previous_version` label |
| `ansible/inventory.ini` | Inventory (local connection) |

Passwords are passed with `-e` and never stored in the repo (`no_log` hides them in output).

## Commands (PowerShell, from the project root)

Build the Ansible runner image once:

```
docker build -t ansible-runner ansible
```

Deploy (run it twice to show idempotency: second run reports `changed=0`):

```
docker run --rm -v //var/run/docker.sock:/var/run/docker.sock -v "${PWD}\ansible:/ansible" ansible-runner ansible-playbook -i inventory.ini deploy.yml -e app_version=1.0 -e admin_password=<pass> -e user_password=<pass>
```

Upgrade: same command with `-e app_version=1.1` (`changed=1`, container replaced).

Roll back:

```
docker run --rm -v //var/run/docker.sock:/var/run/docker.sock -v "${PWD}\ansible:/ansible" ansible-runner ansible-playbook -i inventory.ini rollback.yml -e admin_password=<pass> -e user_password=<pass>
```

The app is served at http://localhost:8083/.
