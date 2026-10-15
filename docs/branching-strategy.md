# Branching Strategy

## Branches

| Branch | Purpose | Rules |
|---|---|---|
| `main` | Stable, release-ready code. Tagged versions live here | No direct commits. Updated only by merging `develop` |
| `develop` | Integration branch where finished work is combined and tested | Changes arrive through pull requests |
| `feature/<id>-<name>` | One user story or task | Created from `develop`, merged back into `develop` |
| `fix/<name>` | One bug fix | Same as feature branches |

## Naming

Lowercase, words separated by hyphens, user story ID first.

- `feature/us1-browse-catalogue`
- `feature/us8-login-roles`
- `fix/low-stock-threshold`

## Commit messages

Format: `type: short description` in present tense.

| Type | Use for |
|---|---|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | README, documents, evidence |
| `test` | Tests |
| `chore` | Setup, configuration, folders |

Example: `feat: add catalogue list page`

## Workflow

1. Pick an issue and create a branch from `develop`.
2. Commit small, meaningful changes and push the branch.
3. Open a pull request into `develop` that references the issue (for example `Closes #3`).
4. Review the changes and leave comments. Merge only when the build and tests pass.
5. Delete the merged branch.
6. When `develop` is stable, merge it into `main` and tag a release (for example `v1.0`).