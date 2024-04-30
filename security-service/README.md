### Security-Service

###  

* obtain the token endpoint: _**GET /security/{application-id}/token`**_

Call example:

```commandline 
curl http://localhost:8070/security/some-app/token`
```

* validate the token endpoint: **GET _/security/{application-id}/valid/{token}_**

Call example:

```commandline
 curl http://localhost:8070/security/some-app/valid/sa12345678
 ```