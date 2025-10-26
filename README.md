# The Gaming Room — Draw It or Lose It (Web)

*Clean repo, Gus tone. This is the browser-based version of the Android party game The Gaming Room already built: **Draw It or Lose It**. Fire it up locally, share a room code, and play.*

---

## What this is

The goal: move “Draw It or Lose It” from Android-only to the web so anyone on a phone, tablet, or desktop can join with a link—no installs, no drama. Gameplay stays the same: teams watch drawings reveal over ~60 seconds, then get a single guess window at the end. This repo holds the API + a minimal client + docs.

> TL;DR: Start API → open client → create room → share code → play.

---

## Tech stack (lightweight on purpose)

* **Language:** Java 17
* **Framework:** Spring Boot (REST for CRUD, WebSocket for real-time)
* **Build:** Gradle (Wrapper included)
* **Client:** Minimal HTML/JS (progressive enhancement OK)
* **Testing:** JUnit + Spring Test
* **Lint/Style:** Checkstyle (optional)

If your grading template expected strictly-REST, we still hit that—but we also add WebSockets for live drawings/timers because real-time UX matters.

---

## Core features (MVP)

* Create and manage games (unique names)
* Teams per game, players per team (no dupes)
* Join/leave via room code
* Server-enforced state via a **singleton GameService** (one source of truth)
* Real-time events: drawing frames, countdown ticks, lock/unlock guess window (WebSocket)

Stretch

* JWT auth for host/player roles
* Persistence (H2 → Postgres)
* Web UI polish/touch gestures

---

## Project layout

```
.
├─ api/                 # Spring Boot app (src, tests, resources)
│  ├─ src/main/java/... # Controllers, services, models
│  ├─ src/test/java/... # Unit/integration tests
│  └─ build.gradle.kts  # Build config
├─ client/              # Dumb-simple web client
│  ├─ index.html
│  └─ app.js
├─ docs/                # Diagrams, notes, screenshots
├─ .gitignore
└─ README.md            # You’re here
```

---

## Quick start

### 1) Prereqs

* Java 17 (Temurin or Oracle)
* Git
* (Optional) IntelliJ IDEA Community/Ultimate

### 2) Clone

```bash
git clone https://github.com/<your-username>/the-gaming-room.git
cd the-gaming-room
```

### 3) Run the API

```bash
cd api
./gradlew bootRun          # macOS/Linux
# or
gradlew.bat bootRun        # Windows
```

Server comes up on **[http://localhost:8080](http://localhost:8080)**.

Health check:

```bash
curl http://localhost:8080/actuator/health
```

### 4) Open the client

Open `client/index.html` directly or serve it locally:

```bash
python -m http.server 5500 -d client
# visit http://localhost:5500/
```

### 5) Try it

* Create a room from the client
* Share the room code
* Open a second tab/window to join and watch real-time updates

---

## API sketch (REST)

* `POST /api/rooms` → create room `{ name }` (unique)
* `GET /api/rooms/{id}` → room details
* `POST /api/rooms/{id}/teams` → add team `{ name }`
* `POST /api/rooms/{id}/players` → add player `{ username, teamId }`
* `DELETE /api/rooms/{id}/players/{playerId}` → remove player
* `DELETE /api/rooms/{id}` → delete room

## Real-time channels (WebSocket)

* `/ws/rooms/{id}`: server broadcasts drawing frames, timer ticks, and state changes
* Heartbeats + backoff reconnect on the client

> **Fair play**: server is the timekeeper. Client clocks are untrusted.

---

## Config

* `api/src/main/resources/application.yml`

  * `server.port` (default 8080)
  * CORS: allow `http://localhost:*` for dev
  * Logging: JSON or pattern—your call

Profiles

* `dev`: in-memory data, loud logs
* `prod`: Postgres + stricter CORS + rate limits

---

## Testing

```bash
cd api
./gradlew test      # (Windows: gradlew.bat test)
```

Reports in `api/build/reports/tests`.

---

## Architecture notes (matches my design doc)

* **Singleton GameService**: coordinates IDs and global lists for games/teams/players. Thread-safe methods and unique name checks.
* **Stateless web tier**: REST for state changes, WebSocket for live updates. Easy to scale horizontally behind a load balancer.
* **Data**: relational DB with unique indexes for names; cache read-mostly stuff (lobby names) for speed.
* **Security**: validate inputs, rate-limit guesses/name checks, short-lived session tokens.

**Operating platforms**

* **Server**: Linux in the cloud (cost-effective, standard). macOS for dev only. Windows fine if it’s your shop, but heavier.
* **Client**: modern browsers on desktop/mobile. Prioritize quick loads and touch-friendly UI.


---

## Dev tips (from me to you)

* Gradle tantrum? `./gradlew clean build --refresh-dependencies`
* Port busy? set `server.port=8081`
* Keep controllers skinny; do logic in services
* Cap WebSocket FPS—batch frames to protect the server

---

## Roadmap (short + realistic)

* [ ] JWT auth + roles (host/player)
* [ ] Postgres persistence + Flyway
* [ ] WebSocket room activity (presence, typing/guess indicators)
* [ ] UI polish (Tailwind) + accessibility sweep


---

## Credits

Built by me (Gus). Class project. Thanks to the rubric gods.

