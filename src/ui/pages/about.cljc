(ns ui.pages.about
  (:require [rum.core :as rum]
            [citrus.core :as citrus]))

(rum/defc Layout [r]
  [:main.layout.about
   [:div.page-content
    [:article.article-entry
     [:h1 "Simple Hacker News Clone"]
     [:h2 "test1"]
     [:p "This is an example of a web app built using ClojureScript and Clojure backend."]]]])
