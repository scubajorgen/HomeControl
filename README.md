# Home Control
## Goal
This is a experimental project regarding usage of the Tado Home Control (zone heating). 
The goal is to provide a basis to build your own website for conrol of your Tado zone
heating, using the Tado API. The focus is to create a Tado REST API client.

## Setup
The project is setup as a Springboot web application offering a web site and a 
corresponding API. Underwater the project offers a REST client to the Tado API.

## Prerequiste
An account is required at tado.nl.

## Usage
Use http://localhost:8080/main for an example of a website based on thymeleaf templates.
Use http://localhost:8080/test.html for an example of a static website using javascript 
and the HomeControl API (http://localhost:8080/api).
Use your Tado credentials to log in. Authentication takes place against the OAuth
authentication server of Tado.
