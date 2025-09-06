package org.example.ecoswapbackend.mobile

/**
 * Dokumentacja API dla aplikacji mobilnej - Generowanie tokenów
 * 
 * Ten plik zawiera dokumentację wszystkich endpointów związanych z autoryzacją
 * dla aplikacji mobilnej EcoSwap.
 */

/*
ENDPOINTY AUTORYZACYJNE DLA APLIKACJI MOBILNEJ:

1. LOGOWANIE MOBILNE
   POST /api/mobile/login
   
   Request Body:
   {
     "email": "user@example.com",
     "password": "password123"
   }
   
   Response (200 OK):
   {
     "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "tokenType": "Bearer",
     "expiresIn": 86400000,
     "user": {
       "id": "1",
       "email": "user@example.com",
       "username": "jdoe",
       "firstName": "John",
       "lastName": "Doe",
       "points": 120
     }
   }
   
   Response (401 Unauthorized):
   {
     "error": "Invalid credentials"
   }

2. ODŚWIEŻANIE TOKENU
   POST /api/refresh
   
   Request Body:
   {
     "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   }
   
   Response (200 OK):
   {
     "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "tokenType": "Bearer",
     "expiresIn": 86400000
   }

3. WALIDACJA TOKENU
   GET /api/mobile/validate
   
   Headers:
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   
   Response (200 OK):
   {
     "valid": true,
     "user": {
       "id": "1",
       "email": "user@example.com",
       "username": "jdoe",
       "firstName": "John",
       "lastName": "Doe",
       "points": 120
     },
     "expiresIn": 72000000
   }

4. PROFIL UŻYTKOWNIKA
   GET /api/mobile/profile
   
   Headers:
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   
   Response (200 OK):
   {
     "user": {
       "id": "1",
       "email": "user@example.com",
       "username": "jdoe",
       "firstName": "John",
       "lastName": "Doe",
       "points": 120
     },
     "tokenInfo": {
       "expiresIn": 72000000,
       "tokenType": "Bearer"
     }
   }

5. AKTUALIZACJA PROFILU
   PUT /api/mobile/profile
   
   Headers:
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   
   Request Body:
   {
     "firstName": "Jan",
     "lastName": "Kowalski"
   }
   
   Response (200 OK):
   {
     "user": {
       "id": "1",
       "email": "user@example.com",
       "username": "jdoe",
       "firstName": "Jan",
       "lastName": "Kowalski",
       "points": 120
     },
     "tokenInfo": {
       "expiresIn": 72000000,
       "tokenType": "Bearer"
     }
   }

6. INFORMACJE O TOKENIE
   GET /api/mobile/token-info
   
   Headers:
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   
   Response (200 OK):
   {
     "valid": true,
     "expired": false,
     "expiresIn": 72000000,
     "isRefreshToken": false,
     "tokenType": "Bearer"
   }

7. WYLOGOWANIE
   POST /api/logout
   
   Headers:
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   
   Response (200 OK):
   {
     "message": "Successfully logged out"
   }

KONFIGURACJA JWT:
- Secret Key: mySecretKey123456789012345678901234567890
- Access Token Expiration: 24 godziny (86400000 ms)
- Refresh Token Expiration: 7 dni (604800000 ms)
- Algorithm: HS256

BEZPIECZEŃSTWO:
- Wszystkie endpointy wymagające autoryzacji muszą zawierać header "Authorization: Bearer <token>"
- Tokeny są podpisywane kluczem HMAC-SHA256
- Refresh tokeny mają dłuższy czas życia niż access tokeny
- CORS jest skonfigurowany dla wszystkich domen (w środowisku produkcyjnym należy to ograniczyć)

PRZYKŁAD UŻYCIA W APLIKACJI MOBILNEJ:

1. Logowanie:
   - Wyślij POST do /api/mobile/login z email i hasłem
   - Zapisz accessToken i refreshToken w bezpiecznym miejscu (np. Keychain/Keystore)
   - Używaj accessToken w headerze Authorization dla wszystkich kolejnych żądań

2. Automatyczne odświeżanie tokenu:
   - Gdy otrzymasz 401 Unauthorized, użyj refreshToken do uzyskania nowego accessToken
   - Wyślij POST do /api/refresh z refreshToken
   - Zaktualizuj zapisane tokeny

3. Walidacja tokenu:
   - Przed każdym ważnym żądaniem możesz sprawdzić czy token jest nadal ważny
   - Użyj GET /api/mobile/validate

4. Wylogowanie:
   - Wyczyść zapisane tokeny z lokalnego storage
   - Opcjonalnie wyślij POST do /api/logout
*/
