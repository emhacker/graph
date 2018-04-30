(ns graph.core-test
  (:require [clojure.test :refer :all]
            [graph.core :refer :all]
            [graph.parser :refer :all]
            [clojure.tools.logging :as log]))

(deftest a-test
  (testing "Graph creation"
    (is (not (nil? new-graph))))
  (testing "Node addition"
    (is (= [:n1] (-> (new-graph) (add-node ,,, :n1) (nodes ,,,)))))
  (testing "Edge addition"
    (let [e1 {:src :n1 :dst :n2}]
      (is (= [e1]
            (-> (new-graph)
                (add-node ,,, :n1)
                (add-node ,,, :n2)
                (add-edge ,,, e1)
                (edges ,,,))))))
  (testing "Graph parser"
    (let [res (parse-graph "digraph mygraph {" #(printf "Error: %s" %1))]
      (log/infof "graph parse result is : %s" res)
      (is (get res :graph-name) "mygraph")
      (is (get res :graph-ty) "digraph"))))
