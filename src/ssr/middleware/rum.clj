(ns ssr.middleware.rum
  (:require [rum.core :as rum]
            [citrus.core :as citrus]))

;; render web app
(defn wrap-rum [handler ui-root resolver render-page]
  (fn [req]
    (let [res (handler req)]
      (if-not (:ui/route req)
        res
        (assoc res
          :body
          (let [state (atom {})
                reconciler
                (citrus/reconciler
                  {:state state
                   :resolvers (resolver req)})]
            (-> reconciler
                (ui-root)
                (rum/render-html)
                (render-page @state))))))))
