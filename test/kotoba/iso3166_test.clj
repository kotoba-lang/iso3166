(ns kotoba.iso3166-test
  (:require [clojure.test :refer [deftest is testing]]
            [kotoba.iso3166 :as iso3166]))

(deftest registry-loads
  (let [reg (iso3166/registry)]
    (is (= :kotoba/iso3166 (:kotoba.registry/id reg)))
    (is (= 193 (count (iso3166/countries reg))))))

(deftest curated-countries-resolve
  (doseq [code ["JPN" "USA" "DEU" "KEN" "IND"]]
    (is (:business-id (iso3166/get-country code)))
    (is (seq (iso3166/required-technologies code)))
    (is (seq (:technology-stack (iso3166/execution-plan code))))))

(deftest get-country-is-case-insensitive
  (is (= (iso3166/get-country "jpn") (iso3166/get-country "JPN"))))

(deftest readiness-reports-missing-tech
  (let [r (iso3166/readiness "JPN" #{:forms :dmn})]
    (is (false? (:ready? r)))
    (is (contains? (:missing r) :audit-ledger)))
  (is (:ready? (iso3166/readiness "USA" #{:identity :forms :dmn :bpmn :audit-ledger}))))

(deftest maturity-tier
  (testing "published blueprint repos are :blueprint"
    (is (= :blueprint (iso3166/maturity "JPN")))
    (is (= :blueprint (iso3166/maturity "USA")))
    (is (= :blueprint (iso3166/maturity "DEU")))
    (is (= :blueprint (iso3166/maturity "KEN")))
    (is (= :blueprint (iso3166/maturity "IND"))))
  (testing "a registry-only country entry is :spec"
    (is (= :spec (iso3166/maturity "AFG")))
    (is (= :spec (iso3166/maturity "BRA"))))
  (testing "maturity-summary counts tiers"
    (let [m (iso3166/maturity-summary)]
      (is (= (:total m) (+ (:spec m) (:blueprint m) (:implemented m))))
      (is (= 193 (:total m)))
      (is (= 5 (:blueprint m)))
      (is (= 0 (:implemented m))))))

(deftest maturity-roadmap-next-step
  (is (= :implemented (:next-step (iso3166/maturity-roadmap "JPN"))))
  (is (= :blueprint (:next-step (iso3166/maturity-roadmap "AFG")))))

(deftest all-countries-have-a-code-and-name
  (doseq [{:keys [code name]} (iso3166/countries)]
    (is (re-matches #"[A-Z]{3}" code))
    (is (some? name))))

(deftest codes-are-unique
  (let [codes (map :code (iso3166/countries))]
    (is (= (count codes) (count (set codes))))))
