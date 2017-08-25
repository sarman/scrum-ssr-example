(require
 '[figwheel-sidecar.repl-api :as ra]
 '[com.stuartsierra.component :as component]
 '[ring.component.jetty :refer [jetty-server]])

(def figwheel-config
   {:figwheel-options {} ;; <-- figwheel server config goes here 
    :build-ids ["dev"]   ;; <-- a vector of build ids to start autobuilding
    :all-builds          ;; <-- supply your build configs here
    [{:id "dev"
      :figwheel true
      :source-paths ["src/main"]
      :compiler {:main "example.core"
                 :asset-path "/out"
                 :output-to "resources/public/main.js"
                 :output-dir "resources/public/out"
                 :verbose true}}]})

(defrecord Figwheel []
  component/Lifecycle
  (start [config]
    (ra/start-figwheel! config)
    config)
  (stop [config]
    ;; you may want to restart other components but not Figwheel
    ;; consider commenting out this next line if that is the case
    (ra/stop-figwheel!)
    config))

(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/plain"}
   :body    "Hello World"})

(def system
  (atom
   (component/system-map
    :app-server (jetty-server {:app {:handler handler}, :port 3000})
    :figwheel   (map->Figwheel figwheel-config))))

(defn start []
  (swap! system component/start))

(defn stop []
  (swap! system component/stop))

(defn reload []
  (stop)
  (start))

(defn repl []
  (ra/cljs-repl))