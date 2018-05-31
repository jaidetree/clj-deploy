(ns clj-deploy.client
  (:require [clj-http.client :as client]
            [clj-sign.core :refer [sign]]
            [clojure.string :refer [join]])
  (:gen-class))

(defn now
  "Get the current time in unix seconds. Used to set the timeout.
  Returns a number."
  []
  (quot (System/currentTimeMillis) 1000))

(defn create-hash
  "Create a hash-string to sign.
  Takes a repo string, branch, and extra values
  Returns a string."
  [hash-values & extra]
  (join "|" (concat hash-values extra)))

(defn select-hash-keys
  "Transforms a map of keys into an array of values.
  Accepts vector of keywords and hash-map.
  Returns vector of values."
  [hash-keys params]
  (map #(get params %) hash-keys))

(defn sign-params
  "Creates a hash-map with message signature keys and a timeout.
  Accepts private key file string, timeout in seconds, and vector of values.
  Returns a hash-map with :timeout :hash and :signature keys."
  [private-key-file timeout hash-values]
  (let [hash (create-hash hash-values timeout)
        signature (sign hash private-key-file)]
    {:timeout timeout
     :hash hash
     :signature signature}))

(defn create-deploy-params
  "Generates a hash, signature and timeout and merges it with post params.
  Accepts private key file string, vector of post param keywords, and
  hash-map of post params.
  Returns a hash map of post params with :hash :timeout and :signature"
  [private-key-file hash-keys params]
  (->> params
       (select-hash-keys hash-keys)
       (sign-params private-key-file (+ (now) 5))
       (merge params)))

(defn create-post-params
  "Creates a config hash-map for clj-http.client.
  Accepts hash-map of form data
  Returns hash-map of request params"
  [form-params]
  {:as :json
   :content-type :json
   :form-params form-params})

(defn deploy!
  "Requests a deploy from the target deploy server-url with an openssl
  RSA signature.
  Accepts string server-url, private-key-file string, vector of keys to sign,
  and hash-map of params to post to server.
  Returns hash-map of JSON server response."
  [server-url private-key-file hash-keys params]
  (->> params
       (create-deploy-params private-key-file hash-keys)
       (create-post-params)
       (client/post server-url)
       :body))
