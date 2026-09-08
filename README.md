# Taller - Branch Capa Web: Servicios Web

## Autenticación

La autenticación es el proceso mediante el cual se verifica la identidad de un usuario o sistema antes de permitirle
acceder a recursos o servicios.

## JSON Web Token (JWT)

- JWT (JSON Web Token) es un estándar abierto que define un método seguro para transmitir información de entre partes.
- Se puede firmar digitalmente, con lo cual si es alterado podríamos verificarlo. Y ahora también cifrar.
    - Con la firma sola, el contenido es visible para cualquiera que tenga el token, pero no puede ser alterado sin
      invalidar la firma.
    - Si lo ciframos, ademas de proteger la integridad, también protegemos la confidencialidad del contenido.
- JWT es una json que contiene tres partes: header, payload y signature.
    - Header: contiene información sobre el tipo de token, el algoritmo de firma.
    - Payload: contiene la información que queremos transmitir/compartir de forma segura, como el Id del usuario, sus
      permisos. Si se designa una fecha de expiración, también se incluye en el payload.
    - Signature: es la firma digital que se genera a partir del header y el payload, utilizando una clave secreta.

## Autenticación con JWT: Capa Servicio/Modelo

- `AgendaTelefonica#registrarUsuario (String username, String password)`: crea un nuevo usuario.
- `AgendaTelefonica#login (String username, String password)`: verifica credenciales y si estan ok, devuelve un JWT.
- `AgendaTelefonica#verificarTokenAndGetIdUsuario (String token)`: verifica el token y devuelve el Id del usuario si es
  válido, o lanza una excepción si no lo es.

## Autenticación con JWT: Capa Web

- `AgendaTelefonicaController#registrarUsuario (@RequestBody CredencialesUsuario credencialesUsuario)`: simplemente
  delega en la capa de servicio.
- `AgendaTelefonicaController#login (@RequestBody CredencialesUsuario credencialesUsuario)`: Delega en la capa de
  servicio y si va todo ok, genera la cookie y la devuelve en la respuesta.

### Segurizar Servicios Web con JWT

- Todo servicio web que requiera autenticación debe verificar el token JWT en la cabecera de la petición. El `idUsuario`
  se **debe obtener** del token, nunca confiar en lo que viene por parámetro respecto a la autenticación.

## Diagrama de secuencia: Sesión tradicional (PHP 5) vs JWT

```text
SESION TRADICIONAL (stateful, previo a JWT y las aplicaciones SPA)
==================================================================
Participantes: Cliente | Servidor | BD Usuarios | Store de Sesiones (persistente)

Cliente                Servidor                BD Usuarios              Store de Sesiones
   |                       |                         |                            |
   |--- login(user,pass) ->|                         |                            |
   |                       |--- validar credenciales- > |
   |                       |<-- credenciales OK ----- |
   |                       |--- generar sessionId unico (UUID/random)            |
   |                       |--- persistir (sessionId -> datosUsuario) ---------->|
   |                       |<-- persistido OK -----------------------------------|
   |<-- Set-Cookie: sessionId=abc123 --|                                         |
   |                       |                         |                            |
   |--- request + Cookie(sessionId=abc123) --------->|                            |
   |                       |--- buscar sessionId ------------------------------->|
   |                       |<-- datosUsuario ------------------------------------|
   |<-- respuesta autorizada ------------|                                       |

- `sessionId` es un identificador unico.
- La relacion `sessionId -> datosUsuario` DEBE persistirse (BD relacional, disco, memoria compartida, etc).


JWT (stateless)
===============
Participantes: Cliente | Servidor | BD Usuarios

Cliente                Servidor                BD Usuarios
   |                       |                         |
   |--- login(user,pass) ->|                         |
   |                       |--- validar credenciales- > |
   |                       |<-- credenciales OK ----- |
   |                       |--- generar JWT con claims (idUsuario, roles, exp)  |
   |                       |--- firmar JWT (HMAC/RSA)                            |
   |<-- token JWT firmado (en cookie) --|                                       |
   |                       |                         |
   |--- request + JWT ---->|                         |
   |                       |--- verificar firma + exp del JWT                    |
   |                       |--- leer claims del token (idUsuario, etc)           |
   |<-- respuesta autorizada --|                                                 |

Clave del modelo:
- El JWT NO se persiste como sesion en servidor.
- La informacion de usuario viaja dentro del token firmado.
```
