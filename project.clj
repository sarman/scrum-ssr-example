(defproject ssr "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.8.0"]
                           [org.clojure/clojurescript "1.9.521"]
                           [org.clojure/core.match "0.3.0-alpha4"]
                           [com.stuartsierra/component "0.3.2"]
                           [ring "1.6.0-RC3"]
                           [aleph "0.4.3"]
                           [cheshire "5.7.1"]
                           [pandect "0.6.1"]
                           [amalloy/ring-gzip-middleware "0.1.3"]
                           [com.cognitect/transit-clj "0.8.300"]
                           [com.cognitect/transit-cljs "0.8.239"]
                           [hiccup "1.0.5"]
                           [rum "0.10.8"]
                           [org.roman01la/citrus "3.0.0"]
                           [bidi "2.0.17"]
                           [kibu/pushy "0.3.7"]
                           [alandipert/storage-atom "2.0.1"]]

            :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]
                      [lein-figwheel  "0.5.13"]
                      [cider/cider-nrepl "0.15.1-SNAPSHOT"]]

            :source-paths ["src" "script"]

            :repl-options {:port 6666}

            :figwheel     {:css-dirs     ["resources/public/css"]
                           :server-port   3449
                           :repl          false
                           :nrepl-port    7777}

            :cljsbuild {:builds
                        [{:id           "dev"
                          ;; put client config options in :figwheel
                          :figwheel     {:on-jsload "client.core/restore-local-state"
                                         ;;:reload-clj-files {:clj false :cljc true}
                                         }
                          :source-paths ["src/client" "src/ui"]
                          :compiler     {:main                 client.core
                                         :preloads             [devtools.preload]
                                         :asset-path           "/js/compiled/out"
                                         :output-to            "resources/public/js/compiled/bundle.js"
                                         :output-dir           "resources/public/js/compiled/out"
                                         :source-map-timestamp true
                                         :verbose              true}
                          }]}

            :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                                            [com.stuartsierra/component.repl "0.2.0"]
                                            [binaryage/devtools "0.9.4"]
                                            [figwheel-sidecar "0.5.12"]
                                            [com.cemerick/piggieback "0.2.2"]
                                            [org.clojure/tools.nrepl "0.2.12"]]
                             :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                             :source-paths ["dev"]}})
