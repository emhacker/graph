(ns graph.core-test
  (:require [clojure.test :refer :all]
            [graph.core :refer :all]))

(deftest a-test
  (testing "Graph creation"
    (is (not (nil? create-graph)))))
