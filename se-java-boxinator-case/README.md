# Boxinator API 📦

[![standard-readme compliant](https://img.shields.io/badge/standard--readme-OK-green.svg?)](https://github.com/RichardLitt/standard-readme)
[![web](https://img.shields.io/static/v1?logo=heroku&message=Online&label=Heroku&color=430098)](https://lededwinnison-boxinator-api.herokuapp.com/swagger-ui/index.html)
[![web](https://img.shields.io/static/v1?logo=gitlab&message=Frontend&label=Gitlab&color=c2681f)](https://gitlab.com/TheNeonLeon/se-java-boxinator-frontend)


## Table of Contents

- [Background](#background)
- [Prerequisites](#prerequisites)
- [Install](#install)
- [Usage](#usage)
- [API](#api)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

[//]: # (## Security)

## Background
This software is a case for the candidates of Experis Academy.
The primary purpose of the software to be a capstone experience for the candidates; they
are expected to utilize a selection of possible development options to produce a single
software solution that demonstrates their capabilities as developers.

The candidates must produce a software solution that is considered a final product. The software is to be produced over a period of three weeks.

##### Technologies used: Java, Spring Boot, Hibernate, Keycloak, Docker

## Description
Boxinator API is a RESTful API constructed with Spring Boot and Hibernate. The API uses a Postgres database in conjunction with the ORM Hibernate to specify entities and their relations.
It also uses a Keycloak instance for authentication in accordance with OAuth2.0 which is then processed by Spring Security to restrict access to certain endpoints.

The database stores data about shipments, accounts, countries, and statuses.
The application is designed for shipping mystery packages between countries around the world, with countries having different multipliers based on their distance from Oslo, Norway.

The full product is both a front- and backend application, where this is the backend part. The frontend part can be found [here](https://gitlab.com/TheNeonLeon/se-java-boxinator-frontend).

## Prerequisites
To run this locally, a postgres database and a keycloak instance is necessary.

To do so, the `docker-compose.yml` file is provided, but requires some setup. For starters, Docker needs to be installed on the device. An ``.env`` file must also be created with the following information within it:
- `POSTGRES_DB` &mdash; name of the postgres database
- `POSTGRES_PASSWORD` &mdash; required by the Postgres image
- `KEYCLOAK_USER` &mdash; username to access the Keycloak admin page
- `KEYCLOAK_PASSWORD` &mdash; password to access the Keycloak admin page. Without these you won't be able to manage Keycloak.

Once this is done, you can start the Postgres and Keycloak server by running the following commands respectively:
```shell
docker-compose up -d postgres
```
```shell
docker-compose up -d keycloak
```

## Install
Gradle will automatically initialize itself and download necessary dependencies the first time the wrapper is run. 
No explicit installation necessary.

## Usage

For Linux/macOS users, open a terminal and run:
```shell
./gradlew bootRun
```
For Windows users, use `gradlew.bat` instead of `gradlew` in PowerShell.

## API

[API Documentation](https://gitlab.com/DMasso/se-java-boxinator/-/wikis/API-Documentation)

## Maintainers

Edwin Eliasson [@edwineliasson98](https://gitlab.com/edwineliasson98),
Leon Listo [@TheNeonLeon](https://github.com/TheNeonLeon),
Dennis Massoumnataj [@DMasso](https://gitlab.com/DMasso)

## Contributing

PRs accepted.

Small note: If editing the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

## License

MIT © 2022 Edwin Eliasson, Dennis Massoumnataj, Leon Listo
