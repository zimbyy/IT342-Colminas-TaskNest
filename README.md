# IT342-Colminas-TaskNest

Full-stack TaskNest demo built with a Spring Boot backend (`backend/tasknest`) and a Vite + React frontend (`frontend`).

## Project Layout
- `backend/tasknest` – Spring Boot 3.5 API for auth and persistence, packaged with Maven wrapper.
- `frontend` – React SPA (Vite) that consumes the `/api/auth` endpoints exposed by the backend.

## Running Locally (H2 dev profile)
1. **Backend**
   ```bash
   cd backend/tasknest
   ./mvnw spring-boot:run
   ```
   The API boots on `http://localhost:8080` against the in-memory H2 database defined in `application.properties`.
2. **Frontend**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   Open the Vite dev server URL (usually `http://localhost:5173`).

## Connecting the backend to Supabase Postgres
The backend now ships with a dedicated `supabase` Spring profile defined in `application-supabase.properties`. This profile expects a TLS-enabled JDBC URL (Supabase requires SSL) plus credentials coming from environment variables (or an imported `.env` file).

### Required environment variables
| Variable | Description |
| --- | --- |
| `SUPABASE_DB_URL` | JDBC URL, e.g. `jdbc:postgresql://db.aodmivtwxejbgacvvhje.supabase.co:5432/postgres?sslmode=require` |
| `SUPABASE_DB_USER` | Usually `postgres` unless you created another database user |
| `SUPABASE_DB_PASSWORD` | The database password you generated in the Supabase dashboard |
| `SPRING_PROFILES_ACTIVE` | Set to `supabase` when you want the backend to talk to Supabase |

#### Option A – `.env.supabase`
`application.properties` imports `backend/tasknest/.env.supabase` automatically (thanks to `spring.config.import`). The file is git-ignored, so you can safely place secrets there:
```
spring.profiles.active=supabase
SUPABASE_DB_URL=jdbc:postgresql://db.aodmivtwxejbgacvvhje.supabase.co:5432/postgres?sslmode=require
SUPABASE_DB_USER=postgres
SUPABASE_DB_PASSWORD=<your-db-password>
```
With that file in place, start the backend normally:
```bash
cd backend/tasknest
./mvnw spring-boot:run
```

#### Option B – export variables in the shell
On **PowerShell** (temporary for the current shell):
```powershell
$env:SUPABASE_DB_URL="jdbc:postgresql://db.aodmivtwxejbgacvvhje.supabase.co:5432/postgres?sslmode=require"
$env:SUPABASE_DB_USER="postgres"
$env:SUPABASE_DB_PASSWORD="<your-db-password>"
$env:SPRING_PROFILES_ACTIVE="supabase"
cd backend/tasknest
./mvnw spring-boot:run
```

On **Command Prompt**:
```cmd
set SUPABASE_DB_URL=jdbc:postgresql://db.aodmivtwxejbgacvvhje.supabase.co:5432/postgres?sslmode=require
set SUPABASE_DB_USER=postgres
set SUPABASE_DB_PASSWORD=<your-db-password>
set SPRING_PROFILES_ACTIVE=supabase
cd backend\tasknest
mvnw spring-boot:run
```

When these variables are present, Spring Boot will pick up `application-supabase.properties`, connect over SSL, and Hibernate will auto-create/update the `users` table in your Supabase Postgres instance.

### Verifying the connection
1. Start the backend with the `supabase` profile as shown above.
2. Watch the console log – you should see Hibernate issuing `create table if not exists ...` against Supabase.
3. You can also open the Supabase SQL editor and run:
   ```sql
   select id, username, first_name, last_name, created_at from users;
   ```
   New registrations from the app should appear here.

## Frontend API base URL
`frontend/src/api.js` now reads the API base from the Vite env variable `VITE_API_BASE_URL` (fallback: `http://localhost:8080/api/auth`). Create a `frontend/.env` file when deploying or when the backend is hosted elsewhere:
```
VITE_API_BASE_URL=https://<your-backend-host>/api/auth
```
Restart `npm run dev` after adding or changing Vite env files.

## Using the Supabase JS client (optional)
If you also want the frontend to call Supabase services directly (storage, realtime, etc.), install the client:
```bash
cd frontend
npm install @supabase/supabase-js
```
Create `frontend/src/lib/supabaseClient.js`:
```js
import { createClient } from "@supabase/supabase-js";

const supabaseUrl = import.meta.env.VITE_SUPABASE_URL;
const supabaseKey = import.meta.env.VITE_SUPABASE_ANON_KEY;

export const supabase = createClient(supabaseUrl, supabaseKey);
```
Then add the Publishable (anon) key and project URL to `frontend/.env`:
```
VITE_SUPABASE_URL=https://aodmivtwxejbgacvvhje.supabase.co
VITE_SUPABASE_ANON_KEY=sb_publishable_kDIk4QL2V3Pk6qfOhTV4Bw_TzU1nW0p
```
This is optional—the existing flow keeps the Supabase credentials on the backend, but the hook above is ready when you want to use other Supabase products.

## Deployment checklist
1. Provision a place to host the Spring Boot JAR (Render, Railway, Azure, etc.). Configure its environment variables with the Supabase values above (or provide a `.env.supabase` file on the server and keep it outside version control).
2. Build the frontend (`npm run build`) and serve the `dist/` folder via any static host, making sure `VITE_API_BASE_URL` points to the deployed backend URL.
3. Keep your Supabase service role key and DB password secret; never commit them to source control.
