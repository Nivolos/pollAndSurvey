\# agents.md — Projektleitlinie für Code-Generierung („Terminumfrage“ / pollAndSurvey)

\> Dieses Dokument dient als Steuerdatei für automatische Code-Generierungen und Projektanpassungen

\> im Repository \*\*Nivolos/pollAndSurvey\*\*.

\> Ziel ist eine prüfungsreife Anwendung „Terminumfrage“ basierend auf

\> \*\*Angular + Spring Boot + Hibernate/JPA\*\*, gemäß den Lehrinhalten des Moduls \*Internet Anwendungsarchitektur\*.

\---

\## 1️⃣ Architekturüberblick

\- \*\*Frontend:\*\* Angular (TypeScript, Angular CLI global installiert)

\- \*\*Backend:\*\* Spring Boot (REST-API mit eingebettetem Tomcat)

\- \*\*Persistenz:\*\* Hibernate/JPA mit H2 (Dev) und optional PostgreSQL (Prod)

\- \*\*Build / Tools:\*\* Maven (global installiert), Angular CLI, npm

\- \*\*Schichtenmodell:\*\*

\- \`web\` → Controller-Layer (REST-Endpoints)

\- \`service\` → Geschäftslogik, Transaktionen

\- \`data\` → Repositories (Spring Data JPA)

\- \`model\` → Entities & DTOs

\---

\## 2️⃣ Technologien & Versionen

| Komponente | Version / Typ | Hinweis |

|-------------|----------------|----------|

| Java | 17 LTS | Pflicht |

| Spring Boot | 3.2.x / 3.3.x | Eingebetteter Tomcat |

| Node.js | 18 LTS | Kompatibel mit Angular 17/18 |

| Angular | 17 oder 18 | CLI global installiert |

| Datenbank | H2 (Dev), PostgreSQL (Prod optional) | |

| Buildsystem | Maven | global, \`mvn spring-boot:run\` |

| Testframeworks | JUnit 5, Mockito, Jasmine, Karma | Unit & Negativtests |

\---

\## 3️⃣ Entwicklungsprinzipien

\- Drei-Schichten-Architektur (Controller / Service / Repository)

\- Keine Geschäftslogik in Controllern oder Repositories

\- @Transactional auf Service-Ebene

\- DTO-Mapping sauber getrennt (MapStruct optional)

\- REST-Endpoints noun-basiert und semantisch konsistent

\- Fehlerbehandlung ausschließlich über Problem-Details-Format (422, 400, 401, 403)

\- Embedded Tomcat bleibt Standard (keine manuelle Serverinstallation)

\---

\## 4️⃣ Projektaufbau (bereits im Repo vorhanden)

\`\`\`

root/

documentation/

tasklogs/

terminumfrage-backend/

terminumfrage-frontend/

README.md

INSTALL.md

agents.md

\`\`\`\`

\*\*Frontend:\*\* Angular-Projekt mit CLI-Struktur (\`src/app/core\`, \`shared\`, \`features\`).

\*\*Backend:\*\* Spring Boot mit den Paketen \`web\`, \`service\`, \`data\`, \`model\`.

\---

\## 5️⃣ Tooling-Setup (verbindlich)

\### Angular

\`\`\`bash

npm install -g @angular/cli

npm ci

ng serve --proxy-config proxy.conf.json

\`\`\`\`

\### Maven / Spring Boot

\`\`\`bash

mvn clean package

mvn spring-boot:run

\`\`\`

Tomcat wird automatisch durch \`spring-boot-starter-web\` eingebunden.

Keine zusätzliche Installation erforderlich.

\---

\## 6️⃣ API-Vertrag

\*\*Basis:\*\* \`/api/v1\`

\### Auth

\* \`POST /auth/register\`

\* \`POST /auth/login\`

\### Polls (Organisator)

\* \`GET /polls?status=&q=\`

\* \`POST /polls\`

\* \`PUT /polls/{id}\`

\* \`POST /polls/{id}/open\`

\* \`POST /polls/{id}/finalize\`

\* \`GET /polls/{id}\`

\### Invitations / Responses

\* \`GET /invitations\`

\* \`PUT /polls/{id}/responses\`

\*\*Fehlerstruktur (Problem-Details):\*\*

\`\`\`json

{

"type": "https://example.com/problem/validation-error",

"title": "Validation failed",

"status": 422,

"errors": \[

{"field": "email", "message": "must be a valid email"}

\]

}

\`\`\`

\---

\## 7️⃣ Frontend-Blueprint (Angular)

\* \`features/auth\`: Login & Registrierung

\* \`features/polls\`: Liste, Editor, Detail-Matrix

\* \`features/invitations\`: Einladungsliste & Teilnahme

\* Reactive Forms, Guards, Interceptors, HttpClient

\* \`proxy.conf.json\` → \`/api\` → \`http://localhost:8080\`

\---

\## 8️⃣ Teststrategie

\### Backend

\* Service-Tests (JUnit, Mockito)

\* Controller-Tests (MockMvc)

\* Negativfälle verpflichtend

\### Frontend

\* Component-Tests (Jasmine/Karma)

\* Service-Tests (HttpTestingController)

\* Interceptor- & Guard-Tests

\---

\## 9️⃣ Qualitätsregeln (DoD)

\* Keine Compiler-Fehler oder Warnings

\* Konsistente Formatierung (Prettier / Google Java Format)

\* Alle Tests grün (\`mvn test\`, \`npm test\`)

\* Dokumentation vollständig (\`documentation/\` vorhanden)

\* Installierbarkeit < 5 Minuten

\* Proxy-Konfiguration vorhanden (\`proxy.conf.json\`)

\---

\## 🔟 Dokumentationspflichten

\`documentation/\` enthält:

\* \`dv-class-diagram.png\` — UML-Klassendiagramm

\* \`dv-dialog-flows.png\` — Dialogmodell

\* \`testcases.md\` — Testfallübersicht

\---

\## 1️⃣1️⃣ Nicht-funktionale Anforderungen

\* Startzeit < 5 Minuten

\* Port 8080 (Backend), 4200 (Frontend)

\* Logging: INFO-Level, keine Stacktraces im Client

\* Zeitzone: UTC in DB, lokale Anzeige im UI

\* JWT-Secret als Umgebungsvariable überschreibbar

\---

\## 1️⃣2️⃣ Anweisung an den Generator

\> Halte dich exakt an diese agents.md.

\> Nutze ausschließlich die definierten Technologien (Abschnitt 2).

\> Implementiere Schichten und API gemäß Abschnitt 3 & 6.

\> Verwende die vorhandene Projektstruktur (Abschnitt 4).

\> Keine Änderungen an Build- oder Laufzeitkonfigurationen außerhalb dieser Spezifikation.

\> Keine externen Frameworks oder Datenbanken ohne explizite Freigabe.

\---

\## 1️⃣3️⃣ Kurzdefinition der Kernbegriffe

| Begriff | Bedeutung |

| -------------- | ---------------------------------------------- |

| \*\*Poll\*\* | Umfrage zur Terminabstimmung |

| \*\*Slot\*\* | Zeitfenster (Start/Ende) |

| \*\*Invitation\*\* | Einladung zu einem Poll |

| \*\*Response\*\* | Antwort eines Teilnehmers (Ja/Nein/Vielleicht) |