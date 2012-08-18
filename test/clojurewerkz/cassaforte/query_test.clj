(ns clojurewerkz.cassaforte.cql-test
  (:use clojure.test
        clojurewerkz.cassaforte.query
        clojurewerkz.cassaforte.test.helper))

(deftest t-prepare-create-column-family-query
  (is (= "CREATE TABLE libraries (name varchar, language varchar, rating double, PRIMARY KEY (name));"
         (prepare-create-column-family-query "libraries"
                                             {:name :varchar :language :varchar :rating :double }
                                             :primary-key :name)))
  (is (= "CREATE TABLE libraries (name varchar, language varchar, rating double);"
         (prepare-create-column-family-query "libraries"
                                             {:name :varchar :language :varchar :rating :double }))))

(deftest t-prepare-drop-column-family-query
  (is (= "DROP TABLE libraries;"
         (prepare-drop-column-family-query "libraries"))))

(deftest t-prepare-insert-query
  (is (= "INSERT INTO libraries (name, language, rating) VALUES ('name', 'language', 1.0) USING CONSISTENCY ONE AND TTL 100;"
         (prepare-insert-query "libraries" {:name "name" :language "language" :rating 1.0}
                               :consistency "ONE"
                               :ttl 100))))


(deftest t-prepare-create-index-query
  (is (= "CREATE INDEX ON column_family_name (column_name)"
         (prepare-create-index-query "column_family_name" "column_name")))
  (is (= "CREATE INDEX index_name ON column_family_name (column_name)"
         (prepare-create-index-query "column_family_name" "index_name" "column_name"))))


;;
;; Conversion to CQL values, escaping
;;

(deftest ^{:cql true} test-conversion-to-cql-values
  (are [val cql] (is (= cql (to-cql-value val)))
    nil "null"
    10  "10"
    10N "10"
    :age "age"))
