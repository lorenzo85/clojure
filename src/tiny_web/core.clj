(ns tiny-web.core
  (:require [clojure.string :as str])
  (:import (com.mblinn.oo.tinyweb RenderingException ControllerException))
  (:gen-class))

(defn- render [view model]
  (try
    (view model)
    (catch Exception e
      (throw (RenderingException. e)))))


(defn- execute-request [http-request handler]
  (let [controller (handler :controller)
        view (handler :view)]
    (try
      {:status-code 200
       :body (render view (controller http-request))}
      (catch ControllerException e {:status-code (.getStatusCode e) :body ""})
      (catch RenderingException e {:status-code 500 :body "Exception while rendering"})
      (catch Exception e (.printStackTrace e) {:status-code 500 :body ""}))))

(defn- apply-filters
  [filters http-request]
    (let [composed-filter (reduce comp (reverse filters))]
      composed-filter http-request))

(defn tinyweb [request-handlers filters]
  (fn [http-request]
    (let [filtered-request (apply-filters filters http-request)
          path (http-request :path)
          handler (request-handlers path)]
      (execute-request filtered-request handler))))

(defn make-greeting
  [name]
  (let [greetings ["Hello" "Greetings" "Salutations" "Hola"]
        greeting-count (count greetings)]
    (str (greetings (rand-int greeting-count)) ", " name)))

(defn handle-greeting
  [http-request]
  {:greetings (map make-greeting (str/split (:body http-request) #","))})

(def request {:path "/greeting" :body "Mike, Joe, John, Steve"})

(defn render-greeting
  [greeting]
  (str "<h2>" greeting "</h2>"))

(defn greeting-view
  [model]
  (let [rendered-greetings (str/join " " (map render-greeting (:greetings model)))]
    (str "<h1>Friendly Greetings</h1> " rendered-greetings)))

;; Note that logging filter, logs the request and RETURNS the http-request.
(defn logging-filter
  [http-request]
  (println (str "In logging Filter - request for path: " (:path http-request)))
  http-request)

(def request-handlers {"/greeting" {:controller handle-greeting :view greeting-view}})
(def filters [logging-filter])

(defn -main
  []
  (let [tiny-web-instance (tinyweb request-handlers filters)
        response (tiny-web-instance request)]
    (println response)))