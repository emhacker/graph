(ns graph.core-test
  (:require [clojure.test :refer :all]
            [graph.core :refer :all]))

(deftest a-test
  (testing "Graph creation"
    (is (not (nil? create-graph))))
  (testing "Node addition"
    (is (= [:n1] (-> (create-graph) (add-node ,,, :n1) (nodes ,,,)))))
  (testing "Edge addition"
    (let [e1 {:src :n1 :dst :n2}]
      (is (= [e1]
            (-> (create-graph)
                (add-node ,,, :n1)
                (add-node ,,, :n2)
                (add-edge ,,, e1)
                (edges ,,,)))))))
