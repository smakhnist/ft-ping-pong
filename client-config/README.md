### Client-Config-Service

###

* get client config: _**GET /client-config/{client-id}`**_

```text
headers:
  Authorization: Basic {auth-id}
params:
  client-id: the client id  
```

Call example: 
```commandline
curl -H "Authorization: Basic cc12345678" http://localhost:8090/client-config/123
```

Response sample:

```json
{
  "clientId": "123",
  "canPing": true,
  "canPong": false,
  "canConnect": true,
  "authId": "abcd"
}
```