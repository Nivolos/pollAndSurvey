# Terminumfrage

Die Anwendung "Terminumfrage" besteht aus einem Spring-Boot-Backend und (geplantem) Angular-Frontend. Dieses Repository enthält den aktuellen Backend-Stand sowie DV-Artefakte.

## Schnellstart Backend

```bash
cd terminumfrage-backend
mvn spring-boot:run
```

Der Server startet auf `http://localhost:8080` mit H2-In-Memory-Datenbank.

## Authentifizierung
- Standard-Organizer: `organizer@example.com` / `password123`
- Standard-Teilnehmer: `participant@example.com` / `password123`

## API-Auszug
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/polls`
- `POST /api/v1/polls`
- `POST /api/v1/polls/{id}/open`
- `POST /api/v1/polls/{id}/finalize`
- `GET /api/v1/invitations`
- `PUT /api/v1/polls/{id}/responses`

Weitere Details siehe Quellcode der Controller.
