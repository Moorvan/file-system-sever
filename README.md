# File System Server For ChatMC

## Introduction

This project provides a file system HTTP service as a dependency for the [ChatMC](https://github.com/Moorvan/ChatMC) project. This service allows users to perform various file operations via HTTP requests.

## Demo CURLs

These demo curls showcase how to interact with the File System Server:

```shell
# Get a list of files in a directory
curl -X GET --location "http://localhost:8081/chatmc/list-files?dirName=Memory"

# Write content to a file
curl -X POST --location "http://localhost:8081/chatmc/write-file" \
    -H "Content-Type: application/json" \
    -d '{
          "dir_name": "Memory",
          "file_name": "a.txt",
          "content": "hello world"
        }'
```

## Configuration

To configure the File System Server, modify the following properties in FileSystemSever/src/main/resources/application.properties:

```properties
quarkus.http.host=0.0.0.0
quarkus.http.port=8081

# Specify the base path for file operations
mctools.basepath=/path/to/project/ChiselFV
#mctools.basepath=/path/to/project/RISCV-Formal-Chisel
```
The base path should point to the directory where your project utilizing ChiselFV is located.

For more information about the ChiselFV project, visit [ChiselFV](https://github.com/Moorvan/ChiselFV). Similarly, more details about the RISCV-Formal-Chisel project can be found at [RISCV-Formal-Chisel](https://github.com/Moorvan/RISCV-Formal-Chisel)

## Build & Run

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

### Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

### Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/file-system-sever-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.