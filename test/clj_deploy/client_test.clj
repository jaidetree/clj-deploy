(ns clj-deploy.client-test
  (:require [clojure.test :refer :all]
            [clj-deploy.client :refer :all]))

(deftest now-test
  (testing "now returns a numeric time"
    (is (int? (now)))))

(deftest create-hash-test
  (testing "create-hash should return a hash string"
    (is (= (create-hash ["key-one" "key-two"] "key-three")
           "key-one|key-two|key-three"))))

(deftest select-hash-keys-test
  (testing "select-hash-keys should return a vector of values"
    (is (= (select-hash-keys [:a :b] {:a 1 :b 2 :c 3}) [1 2]))))

(deftest sign-params-test
  (testing "sign-params returns a hash-map of signature data"
    (let [params (sign-params "test/fixtures/private_key.pem"
                            (+ (now) 5)
                            ["test" "sign-params"])]
      (is (int? (:timeout params)))
      (is (string? (:hash params)))
      (is (= (:hash params) (str "test|sign-params|" (:timeout params))))
      (is (string? (:signature params))))))

(deftest create-deploy-params-test
  (testing "create-deploy-params merges signature with post params"
    (let [params (create-deploy-params "test/fixtures/private_key.pem"
                                       [:a :b]
                                       {:a 1 :b 2 :c 3})]
      (is (contains? params :a))
      (is (contains? params :b))
      (is (contains? params :c))
      (is (contains? params :timeout))
      (is (contains? params :hash))
      (is (contains? params :signature)))))

(deftest create-post-params-test
  (testing "create-post-params returns a hash-map for clj-http"
    (is (= (create-post-params {:a 1 :b 2 :c 3})
           {:as :json
            :content-type :json
            :form-params {:a 1 :b 2 :c 3}}))))
