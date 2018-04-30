(ns graph.parser
  (:require [clojure.tools.logging :as log])
  (:require [clojure.string :as str])
  (:require [graph.core :as core]))

(defn _new-ctx [] {:phase :phase-top-level :graph-ty :unkown})

;;
;; Parsing functions.
;;
(defn _parse-graph-rule 
  [ctx tokens]
  (log/infof "_parse-graph-rule called with ctx %s, tokens %s" ctx tokens)
  (loop [l-ctx ctx l-tokens tokens state :grule-ty]
    (and (not (seq l-tokens)) l-ctx)
    (let [ftoken (first l-tokens)]
      (case state
        :grule-ty
        (if (= ftoken "digraph")
          (recur (assoc l-ctx :graph-ty "digraph") (rest l-tokens) :grule-name)
          (assoc
            ctx
            :parse-error
            (format "Expected keyword 'digraph', found %s" ftoken)))
        :grule-name
        (if
          (= (re-find #"[a-zA-Z0-9_-]+" ftoken) ftoken)
          (recur (assoc l-ctx :graph-name ftoken) (rest l-tokens) :grule-cbracket)
          (assoc
            ctx
            :parse-error
            (format "%s is not a valid graph name" ftoken)))
        :grule-cbracket
        (if (= ftoken "{")
          l-ctx
          (assoc
            ctx
            :parse-error
            (format "Expected '{' sign, found %s" ftoken)))))))

;;
;; Parser API.
;;
(defn parse-graph
  "Parses a graph from the given string."
  [s-spec error-fn]
  (loop [ctx (_new-ctx) tokens (str/split s-spec #"\s+")]
    (case (get ctx :phase)
      :phase-top-level
        (_parse-graph-rule ctx tokens)
      :phase-error
      (-> (get ctx :parse-error) (error-fn ,,,))
      ;; Default
      (assoc
        ctx
        :parse-error
        (format "Internal error: unknown parser phase %s" (get ctx :phase))))))
