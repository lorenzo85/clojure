(ns tiny-web.stepone
  (:import (com.mblinn.oo.tinyweb HttpRequest HttpRequest$Builder)))

(defn test-builder
  []
  (HttpRequest$Builder/newBuilder))

(defn test-http-request
  []
  (.. (test-builder) (body "Mike") (path "/say-hello") build))

(defn test-controller-with-map
  [http-request]
  {:name (http-request :body)})

(defn test-controller
  [http-request]
  {:name (.getBody http-request)})
