# UML-Klassenmodell

```text
+----------------+        1        *   +----------------+
|    User        |-------------------->|     Poll       |
+----------------+ organizer           +----------------+
| id             |                     | id             |
| email          |                     | title          |
| fullName       |                     | description    |
| passwordHash   |                     | status         |
| createdAt      |                     | createdAt      |
+----------------+                     +----------------+
                                            |1
                                            | has
                                            v
                                      +--------------+
                                      |  TimeSlot    |
                                      +--------------+
                                      | id           |
                                      | startDateTime|
                                      | endDateTime  |
                                      +--------------+
                                            ^
                                            | responses for
                                            | 1..*
+----------------+ invitee     +----------------+      participant
|  Invitation    |-------------|   Response     |------------------+
+----------------+             +----------------+                  |
| id             |             | id             |                  |
| inviteeEmail   |             | value          |                  |
| token          |             | participantEmail (optional)       |
| state          |             | participantUser (optional)        |
+----------------+             +----------------+                  |
        |1                                                       0..1|
        | belongs to                                               v
        |                                                         +-----+
        |                                                         |User |
        v                                                         +-----+
        +----------------+
        |     Poll       |
        +----------------+

FinalSelection (0..1) links Poll 1..1 -> TimeSlot 1..1 when finalized.
```
