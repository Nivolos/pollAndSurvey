# Testfallübersicht

## Authentifizierung
- **TC-AUTH-01**: Registrierung mit neuen Daten -> Token wird ausgegeben, Benutzer kann sich anmelden.
- **TC-AUTH-02**: Registrierung mit bereits verwendeter E-Mail -> Fehler 400 "Email already registered".
- **TC-AUTH-03**: Login mit falschem Passwort -> 401 Fehlermeldung, Formular zeigt Hinweis.

## Poll-Verwaltung
- **TC-POLL-01**: Organizer legt Poll mit 2 Slots und 3 Einladungen an -> Poll im Status DRAFT, Detailansicht zeigt Slots & Einladungen.
- **TC-POLL-02**: Organizer versucht Poll ohne Slots zu öffnen -> Validierungsfehler 422.
- **TC-POLL-03**: Organizer öffnet Poll und finalisiert Slot -> Status FINALIZED, weitere Antworten gesperrt.
- **TC-POLL-04**: Organizer ruft fremden Poll auf -> 422 Fehlermeldung "Poll not found" (keine Daten offengelegt).

## Antworten als Teilnehmer
- **TC-RESP-01**: Eingeladener Teilnehmer ruft Liste der Einladungen auf -> Polls werden angezeigt.
- **TC-RESP-02**: Teilnehmer sendet Antworten (Ja/Nein/Möglich) -> Persistiert, Summen aktualisiert.
- **TC-RESP-03**: Teilnehmer versucht nach Finalisierung zu antworten -> 422 Fehlermeldung "Poll finalized".

## Validierung & Fehlerfälle
- **TC-VAL-01**: Slot-Ende vor Slot-Beginn -> Fehler bei Erstellung.
- **TC-VAL-02**: Einladungen mit ungültiger E-Mail -> Frontend Formular zeigt Fehler, Backend 422 bei API-Aufruf.
- **TC-VAL-03**: Ungültiges JWT -> Anfrage wird abgewiesen, Nutzer muss sich neu anmelden.
