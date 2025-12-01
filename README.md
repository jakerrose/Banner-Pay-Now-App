# Pay Now App
## Java Spring Boot app using JDK 17 with Maven
Acts as SP, so far is configured with Keycloak as Idp. I am running Keycloak on https://localhost:6771 and Spring on https://localhost:7171. Will incorporate Flywire's checkout integration once I have the credentials. I am also testing the app using auth0 as Idp and hope to also test with Entra and Oka. Some notes on getting this app up and running:

### Set up Keycloak
Download Keycloak 26.2.5
Unzip to local machine
Use command prompt, go to bin directory, run command to start Keycloak
https:
.\kc.bat start-dev --log-level=DEBUG ^
  --https-port=6771 ^
  --https-key-store-file="C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot\keystore\mycert.pfx" ^
  --https-key-store-password=yourpassword ^
  --https-key-store-type=PKCS12 ^
  --hostname-strict=false ^
  --spi-hostname-strict-https=false
Console will show e.g. Listening on: https://0.0.0.0:6771
Got to https://localhost:6771

Sign in with username admin, password admin, create new password once logged in
Create new realm, go to realm settings, click on link Endpoints: Saml 2.0 Indentity Provider Metadata
Will open in https://localhost:6771/realms/{realmName}/ protocol/saml/descriptor
This is the metadata

### Spring Boot program
Spring Boot Program running on Java 17 with Maven
In application.yml provide 

signing:
  credentials:
    - private-key-location: classpath:rp-key.key
      certificate-location: classpath:rp-cert.crt

verification.credentials:
  - certificate-location: classpath:mycert.pfx

for rp-key.key and rp-cert.cert (signed pair)
run command:

openssl req -newkey rsa:2048 -nodes -keyout rp-key.key -x509 -days 365 -out rp-cert.pem -subj "/CN=your-sp-entity-id"

Create certs with subject alternative names for sso (“mycert.pfx”)
Create a file named openssl-san.cnf with the following contents:

[req]
default_bits       = 2048
prompt             = no
default_md         = sha256
req_extensions     = req_ext
distinguished_name = dn

[dn]
C = US
ST = State
L = City
O = Your Company
OU = Dev
CN = localhost

[req_ext]
subjectAltName = @alt_names

[alt_names]
DNS.1 = localhost

Run this command:

openssl req -x509 -nodes -days 365 -newkey rsa:2048 ^
  -keyout key.pem -out cert.pem ^
  -config openssl-san.cnf

Create pfx file:
openssl pkcs12 -export ^
  -out mycert.pfx ^
  -inkey key.pem ^
  -in cert.pem ^
  -passout pass:yourpassword

Import pfx to Windows certmgr.msc

While running SP, go to https://localhost:7171/saml2/service-provider-metadata/keycloak
Will download xml file
Go to Keycloak, Clients, import Client, import xml and it will fill out information
In Client, Keys, click Import Key, import public cert. Certificate string will match what is in xml
ClientId in Keycloak = SP entity ID
In Client->Client Details->settings, make sure to check on Sign Documents, Sign Assertions, Force Post Binding. In Advanced, check Assertion Consumer Service POST Binding URL set to https://localhost:{port}/login/saml2/sso/{registration-Id}
RegistrationId = end part of IDP metadata entityID
Make sure Sign Documents and Sign Assertions are checked.

Run app and it should bring up index.html in browser.


