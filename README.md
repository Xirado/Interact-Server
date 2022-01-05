# Interact


**(WARNING: This library is a WIP and not usable!)**

This library strives to abstract Discords Webhook Interactions.

It runs a Javalin HTTP-Server that does the complex work for you.

### Before using

Keep in mind that Discord does not allow the use of unsecured HTTP endpoints.
In order to use this library, you need to set up a reverse proxy, which has to support **HTTPS**!