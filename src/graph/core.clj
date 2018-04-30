(ns graph.core
  (:require [clojure.tools.logging :as log]))

;;
;; Graph utilites.
;;
(defn _add-graph-set
  [graph k new-elem]
  (assoc graph k (-> (get graph k) (conj ,,, new-elem))))

(defn _valid-edge?
  "Indicates whether the given edge is valid (IFF the nodes it references exists
  in the graph's nodes)."
  [graph e]
  (log/debugf "Validating edge %s for graph %s"e graph)
  (every? #(some #{%} (get graph :nodes)) (vals e)))

(defn _add-edge
    "Internal function - add-edge; returns the modified graph when successful;
    when unsuccessful, invokes the given error function."
    [graph new-edge error-fn]
    (if (_valid-edge? graph new-edge)
      (_add-graph-set graph :edges new-edge)
      (error-fn)))

;;
;; Graph API.
;;
(defn create-graph
  "Creates an empty graph."
  []
  {:nodes '() :edges '()})

(defn nodes
  "Gets the nodes associated to the given graph."
  [graph]
  (log/debugf "Retrieving nodes for ")
  (get graph :nodes))

(defn edges
  "Gets the edges associated to the given graph."
  [graph]
  (get graph :edges))

(defn add-node
  "Adds a node to the given graph."
  [graph new-node]
  (if (some #{new-node} (nodes graph))
    (graph)
    (_add-graph-set graph :nodes new-node)))

(defn add-edge
  "Adds the given edge to the graph. (The fact that the edge references existing
  nodes is asserted"
  ([graph new-edge]
    (_add-edge
      graph
      new-edge
      #(throw (Exception. (str "Invalid node" new-edge)))))
  ([graph new-edge error-fn]
   (_add-edge
     graph
     new-edge
     error-fn)))
