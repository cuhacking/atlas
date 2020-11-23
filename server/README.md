# Atlas Server

This is a basic server that serves the data file that the Atlas app uses.
It serves the file with the appropriate `Last-Modified` header that contains a timestamp of the last time the data file was updated.

## Running the server
```shell script
java -jar server.jar <data-file>
```

As a docker image, the data file can be mounted in `/usr/src/app`. e.g. in a docker-compose file:

```yml
server:
    image: "atlas-server:latest"
    volumes:
        - ./myfile.json:/usr/src/app/data.json
    ports:
        - "8080:8080"
```