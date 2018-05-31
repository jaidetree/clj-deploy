# clj-deploy

A general-purpose library for sending deploy params to a server.

## Installation
Leiningen/Boot

```
[clj-deploy "0.1.0"]
```

Clojure CLI/deps.edn

```
clj-deploy {:mvn/version "0.1.0"}
```

Gradle

```
compile 'clj-deploy:clj-deploy:0.1.0'
```

Maven

```
<dependency>
  <groupId>clj-deploy</groupId>
  <artifactId>clj-deploy</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Usage

```clojure
(ns my-app.core
  (:require [clj-deploy.client :refer [deploy!]]))

(->> (deploy! "https://my-server-url.com/deploy"
              "path/to/private_key.pem"
              [:repo :branch]
              {:repo "my-repo.git"
               :branch "staging"
               :src-dir "dist/"
               :dest-dir "www/my-app"})
     (clojure.pprint/pprint)
```

## Params

**server-url**

A server URL to send the POST request to. It will receive a JSON post body with the params you provide plus:

1. timeout - A int timeout of 5 minutes from time of deploy
2. hash - A string of joined values from the hash-keys you specify like `my-repo.git|staging|1234567890`
3. signature - An openssl RSA signature of the hash string

**private-key-file**

A filename string of a private key file.

**hash-keys**

A vector of keywords to hash from the post params.

**params**

A hash-map of params to put in the post body of the deploy request.

### Bugs

If you encounter any bugs or have feature requests please share them at https://github.com/jayzawrotny/clj-deploy/issues.

## License

Copyright © 2018 Jay

Distributed under the BSD 3-Clause "New" or "Revised" License.
