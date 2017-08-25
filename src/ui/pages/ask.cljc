(ns ui.pages.ask
  (:require [rum.core :as rum]
            [citrus.core :as citrus]
            [ui.base :as base]))

(rum/defc Layout < rum/reactive [r]
  (let [{:keys [from to total items]}
        (rum/react (citrus/subscription r [:ask-posts]))
        current (when (pos? total) (/ to (- to from)))
        total (when (pos? total) (->> (- to from) (/ total) Math/ceil int))]
    [:main.layout
     (base/Pagination
       {:slug "ask"
        :current current
        :total total})
     [:div.page-content
      (map #(rum/with-key (base/PostItem %) (:id %)) items)]]))
