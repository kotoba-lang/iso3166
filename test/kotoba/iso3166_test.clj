(ns kotoba.iso3166-test
  (:require [clojure.test :refer [deftest is testing]]
            [kotoba.iso3166 :as iso3166]))

(deftest registry-loads
  (let [reg (iso3166/registry)]
    (is (= :kotoba/iso3166 (:kotoba.registry/id reg)))
    (is (= 212 (count (iso3166/countries reg))))))

(deftest curated-countries-resolve
  (doseq [code ["JPN" "USA" "DEU" "KEN" "IND" "BRA" "GBR" "SGP"]]
    (is (:business-id (iso3166/get-country code)))
    (is (seq (iso3166/required-technologies code)))
    (is (seq (:technology-stack (iso3166/execution-plan code))))))

(def all-19-jpn-agency-codes
  ["JPN-CAO" "JPN-MIC" "JPN-MOJ" "JPN-MOFA" "JPN-MOF" "JPN-MEXT" "JPN-MHLW"
   "JPN-MAFF" "JPN-METI" "JPN-MLIT" "JPN-MOE" "JPN-MOD" "JPN-RECONSTRUCTION"
   "JPN-DIGITAL" "JPN-JFTC" "JPN-PPC" "JPN-FSA" "JPN-AUDIT" "JPN-STATISTICS"])

(deftest curated-jpn-agencies-resolve
  (doseq [code all-19-jpn-agency-codes]
    (is (:business-id (iso3166/get-country code)))
    (is (seq (iso3166/required-technologies code)))
    (is (seq (:technology-stack (iso3166/execution-plan code))))
    (is (= "JPN" (:parent (iso3166/get-country code))))))

(deftest all-19-jpn-agencies-are-blueprint
  (testing "full Japan agency-level coverage complete (ADR-2607040100 through ADR-2607040500)"
    (doseq [code all-19-jpn-agency-codes]
      (is (= :blueprint (iso3166/maturity code)) (str code " should be :blueprint"))))
  (is (= 19 (count all-19-jpn-agency-codes)))
  (is (= (set all-19-jpn-agency-codes) (set (map :code (iso3166/children "JPN"))))))

(deftest jpn-children-resolve-to-19-agencies
  (let [kids (iso3166/children "JPN")]
    (is (= 19 (count kids)))
    (is (every? #(= "JPN" (:parent %)) kids))
    (is (= #{:ministry :agency :independent-commission} (set (map :level kids))))))

(deftest non-jpn-countries-have-no-children-yet
  (doseq [code ["USA" "DEU" "KEN" "IND" "AFG"]]
    (is (empty? (iso3166/children code)))))

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
    (is (= :blueprint (iso3166/maturity "IND")))
    (is (= :blueprint (iso3166/maturity "JPN-METI")))
    (is (= :blueprint (iso3166/maturity "JPN-MOF")))
    (is (= :blueprint (iso3166/maturity "JPN-DIGITAL")))
    (is (= :blueprint (iso3166/maturity "JPN-JFTC")))
    (is (= :blueprint (iso3166/maturity "JPN-PPC")))
    (is (= :blueprint (iso3166/maturity "JPN-MOJ")))
    (is (= :blueprint (iso3166/maturity "JPN-MHLW")))
    (is (= :blueprint (iso3166/maturity "JPN-FSA")))
    (is (= :blueprint (iso3166/maturity "JPN-MAFF")))
    (is (= :blueprint (iso3166/maturity "JPN-MLIT")))
    (is (= :blueprint (iso3166/maturity "JPN-MOE")))
    (is (= :blueprint (iso3166/maturity "JPN-MOFA")))
    (is (= :blueprint (iso3166/maturity "JPN-MOD")))
    (is (= :blueprint (iso3166/maturity "JPN-AUDIT")))
    (is (= :blueprint (iso3166/maturity "JPN-CAO")))
    (is (= :blueprint (iso3166/maturity "JPN-MIC")))
    (is (= :blueprint (iso3166/maturity "JPN-MEXT")))
    (is (= :blueprint (iso3166/maturity "JPN-RECONSTRUCTION")))
    (is (= :blueprint (iso3166/maturity "JPN-STATISTICS")))
    (is (= :blueprint (iso3166/maturity "BRA")))
    (is (= :blueprint (iso3166/maturity "GBR")))
    (is (= :blueprint (iso3166/maturity "SGP"))))
  (testing "a registry-only country entry is :spec"
    (is (= :spec (iso3166/maturity "AFG")))
    (is (= :spec (iso3166/maturity "FRA"))))
  (testing "maturity-summary counts tiers"
    (let [m (iso3166/maturity-summary)]
      (is (= (:total m) (+ (:spec m) (:blueprint m) (:implemented m))))
      (is (= 212 (:total m)))
      (is (= 27 (:blueprint m)))
      (is (= 0 (:implemented m))))))

(deftest maturity-roadmap-next-step
  (is (= :implemented (:next-step (iso3166/maturity-roadmap "JPN"))))
  (is (= :blueprint (:next-step (iso3166/maturity-roadmap "AFG")))))

(deftest all-countries-have-a-code-and-name
  (doseq [{:keys [code name level]} (iso3166/countries)]
    (if level
      (is (re-matches #"[A-Z]{3}-[A-Z]+" code))
      (is (re-matches #"[A-Z]{3}" code)))
    (is (some? name))))

(deftest all-agency-entries-have-a-resolvable-parent
  (doseq [{:keys [level parent]} (iso3166/countries)]
    (when level
      (is (some? parent))
      (is (some? (iso3166/get-country parent)) (str parent " must resolve to a country entry")))))

(deftest codes-are-unique
  (let [codes (map :code (iso3166/countries))]
    (is (= (count codes) (count (set codes))))))
