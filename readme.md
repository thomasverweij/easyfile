# EasyFile

Minimalistic E2E-encrypted file sharing web service written in Java + Spring Boot and Javascript + Svelte.
(Work in progress, does not yet work on browsers that don't support WriteableStream)

## How does it work?

1. User enters a password to create a bucket or to log in to an existing bucket.
2. Server responds with a jwt token, stored by the client in localstorage.
3. The password is also used to generate an AES-GCM encryption key using PBKDF2, which is also stored in localstorage.
4. To prevent extensive memory usage, big files are encrypted using streaming encryption and uploads will happen in chunks using [tus](https://tus.io).
5. Downloads are decrypted using streaming decryption and then downloaded from a service worker to prevent storing the file in memory.

## Technologies used

- Javascript
- Java
- Svelte
- Spring Boot
- wormhole-crypto
- tus-js-client
- streamsaver
- tus-java-server
- Flyway
- Postgres
- Minio
- Traefik
- Docker

## Run locally

````sh
./ops/dev/up.sh
````

Requirements:

- java 11
- maven
- docker
