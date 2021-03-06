(ns client.effects
  (:require [citrus.core :as citrus]
            [cognitect.transit :as t]))

(defn- ->rpc [method params]
  {:jsonrpc "2.0"
   :method method
   :params params
   :id (gensym "rpc")})

(defn- ->transit [data]
  (t/write (t/writer :json) data))

(defn- ->edn [data]
  (t/read (t/reader :json) data))

(defn rpc [r c {:keys [url method params on-success on-error]}]
  (-> (js/fetch url
        #js {:method "POST"
             :body (->transit (->rpc method params))
             :headers
             (doto (js/Headers.)
               (.append "Content-Type" "application/transit+json"))})
      (.then #(.text %))
      (.then ->edn)
      (.then #(:result %))
      (.then #(citrus/dispatch! r c on-success %))
      (.catch #(citrus/dispatch! r c on-error %))))
