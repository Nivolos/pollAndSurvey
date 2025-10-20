# Dialogmodell

```text
[Login]
  -> Benutzer gibt E-Mail/Passwort
  -> Aktion "Anmelden" -> Erfolgreich? -> [Poll-Liste]
  -> Fehler (401/422) -> Fehlermeldung anzeigen, bleibt im Dialog
  -> Link "Registrieren" -> [Registrierung]

[Registrierung]
  -> Formular: Name, E-Mail, Passwort
  -> Aktion "Account anlegen" -> Erfolg -> [Poll-Liste]
  -> Fehler -> Validierungsfehler anzeigen

[Poll-Liste]
  -> Tabelle mit Filtern
  -> Aktionen: "Neu" -> [Poll-Editor]
                "Details" -> [Poll-Detail]
                "Einladungen" (als Teilnehmer) -> [Einladungs-Liste]

[Poll-Editor]
  -> Formular: Titel, Beschreibung, Teilnehmer (Chips), Slots (Tabelle)
  -> Aktionen: "Speichern" (POST /polls) -> Erfolg -> [Poll-Detail]
               "Abbrechen" -> [Poll-Liste]

[Poll-Detail]
  -> Matrix Slots x Teilnehmer
  -> Aktionen: "Öffnen" (POST /polls/{id}/open)
               "Finalisieren" (POST /polls/{id}/finalize)
               "Zurück" -> [Poll-Liste]

[Einladungs-Liste]
  -> zeigt offene Polls als Teilnehmer
  -> Aktion: "Antworten" -> [Antwort-Dialog]

[Antwort-Dialog]
  -> Matrix mit Buttons Ja/Nein/Vielleicht
  -> Aktion "Speichern" (PUT /polls/{id}/responses) -> Erfolg -> [Einladungs-Liste]
```
