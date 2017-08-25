(ns client.core
  (:require [rum.core :as rum]
            [citrus.core :as citrus]
            [goog.dom :as dom]
            [cognitect.transit :as t]
            [ui.core :refer [App]]
            [ui.routes :refer [routes]]
            [client.router :as r]
            [client.effects :as effects]
            [client.controllers.router :as router]
            [client.controllers.top-posts :as top-posts]
            [client.controllers.new-posts :as new-posts]
            [client.controllers.show-posts :as show-posts]
            [client.controllers.ask-posts :as ask-posts]
            [client.controllers.job-posts :as job-posts]
            [client.controllers.user :as user]
            [client.controllers.post :as post]

            [alandipert.storage-atom :refer [local-storage]]))

(def state (local-storage (atom {}) :state))

(add-watch state :watch-change
           (fn [key a old-val new-val]
             ;;(js/console.log new-val)
             ))

;; reconciler
(def r
  (citrus/reconciler
    {:state state
     :controllers
     {:router router/control
      :top-posts top-posts/control
      :new-posts new-posts/control
      :show-posts show-posts/control
      :ask-posts ask-posts/control
      :job-posts job-posts/control
      :post post/control
      :user user/control}
     :effect-handlers
     {:rpc effects/rpc}}))

;; load controller data on navigation change
(defn- watch-router! []
  (add-watch
    (citrus/subscription r [:router])
    :router
    (fn [_ _ _ {:keys [handler route-params]}]
      (when-let
        [ctrl
         (case handler
           :top :top-posts
           :new :new-posts
           :show :show-posts
           :ask :ask-posts
           :jobs :job-posts
           :user :user
           :post :post
           nil)]
        (citrus/dispatch! r ctrl :load route-params)))))

(defn render []
  (rum/mount (App r)
             (dom/getElement "app")))

(defn ^:export init-app [server-state]
  (let [server-state (t/read (t/reader :json) server-state)] ;; read server state
    (js/console.log server-state)
    (swap! state assoc :server-state server-state)
    (citrus/broadcast-sync! r :init) ;; init all controllers
    (doseq [[ctrl init-state] server-state]
      (citrus/dispatch-sync! r ctrl :init init-state)) ;; init controllers with server state
    (r/start! #(citrus/dispatch! r :router :push %) routes) ;; start router
    (render) ;; render the app
    (watch-router!))) ;; watch route changes to load data on change

(defn restore-local-state []
  "For figwheel"
  (js/console.log state)
  (citrus/broadcast-sync! r :init)                          ;; init all controllers
  ;;(citrus/dispatch-sync! r ctrl :init state)                ;; reinit controllers with local state
  (r/start! #(citrus/dispatch! r :router :push %) routes)   ;; start router
  (render)                                                  ;; render the app
  (watch-router!))
