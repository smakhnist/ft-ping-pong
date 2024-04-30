### Client App

###

This is a simple client console app that connects to ping-pong server.

runs in 2 modes:

* no args. Parameters are hardcoded in the app (e.g. clientId=123, authId=abcd).
* 2 program args: the first is clientId, the second is authId.

To shut down the app, type 'exit' and press enter.

###

Implementation details:

* The app connects to endpoint: `ws://localhost:8080/socket/ping-pong`
* the app subscribes to the topic: `/acceptance/ping-pong`
* The app is self-reconnectable and automatically reconnects on session termination.
