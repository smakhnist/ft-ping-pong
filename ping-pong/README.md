### Ping-Pong-Service

###

* **POST _/acceptance/{client-id}/ping_**

Call example:

```commandline 
curl -X POST http://localhost:8080/acceptance/123/ping
```

* **POST _/acceptance/{client-id}/pong_**

Call example:

```commandline
 curl -X POST http://localhost:8080/acceptance/123/pong
 ```

* **PUT _/connection/{client-id}/drop_**

Call example:

```commandline
curl -X PUT http://localhost:8080/connection/123/drop
```

###

Implementation details:

* WebSocketHandshakeInterceptor intercepts the handshake request,
  validates the client id and auth id, checks if the client is entitled to connect.
* WebSocketHandshakeInterceptor implies communication with the Security-Service and Client-Config-Service. To prevent
  abuse of Ping-Pong-Service there was implemented DDoSAttackerDetector service that detects DDoS attacks (max of 10 connections per minute for a one
  client connection) and blocks that client. In real-world applications, we'll use API Gateway to handle these kinds of vulnerabilities.
* WebSocketEventListener listen '/acceptance/ping-pong' topic subscription events and respond with the connection
  acknowledgement to the client.
  Persist sessionId with the sessions state management service (InMemoryClientSessionManager).
* Session state management (clientId -> SessionId) is implemented in InMemoryClientSessionManager with a thread-safe
  Map.
  In real-world applications, it should be replaced with a distributed cache (Redis).