(ns kotoba.iso3166-test
  (:require [clojure.test :refer [deftest is testing]]
            [kotoba.iso3166 :as iso3166]))

(deftest registry-loads
  (let [reg (iso3166/registry)]
    (is (= :kotoba/iso3166 (:kotoba.registry/id reg)))
    (is (= 227 (count (iso3166/countries reg))))))

(deftest curated-countries-resolve
  (doseq [code ["JPN" "USA" "DEU" "KEN" "IND" "BRA" "GBR" "SGP" "ARE" "AUS" "KOR"
                "POL" "MEX" "SAU" "CAN" "NZL" "ZAF" "CHL" "NGA" "IDN" "IRL" "NLD" "VNM"
                "THA" "COL" "ESP" "PHL" "PER" "ITA" "BGD" "ARG" "GHA" "FRA" "EGY" "PAK"
                "TUR" "MAR" "ETH" "SWE" "KAZ" "QAT" "CHN" "CRI" "CZE" "UKR" "ISR" "URY"
                "EST" "RWA" "PAN" "GEO" "JOR" "SEN" "NPL" "FIN" "TUN" "NOR" "LKA" "BWA"
                "DNK" "LVA" "ECU" "ISL" "LTU" "ZMB" "HUN" "HRV" "NAM" "SVK" "BOL" "KHM"]]
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

(deftest non-jpn-usa-countries-have-no-children-yet
  (doseq [code ["DEU" "KEN" "IND" "AFG" "CHN"]]
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
    (is (= :implemented (iso3166/maturity "JPN")))
    (is (= :implemented (iso3166/maturity "USA")))
    (is (= :implemented (iso3166/maturity "CHN")))
    (is (= :blueprint (iso3166/maturity "USA-GSA")))
    (is (= :blueprint (iso3166/maturity "USA-SBA")))
    (is (= :blueprint (iso3166/maturity "USA-DOD")))
    (is (= :implemented (iso3166/maturity "USA")))
    (is (= :implemented (iso3166/maturity "DEU")))
    (is (= :blueprint (iso3166/maturity "KEN")))
    (is (= :implemented (iso3166/maturity "IND")))
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
    (is (= :implemented (iso3166/maturity "BRA")))
    (is (= :implemented (iso3166/maturity "GBR")))
    (is (= :implemented (iso3166/maturity "SGP")))
    (is (= :blueprint (iso3166/maturity "ARE")))
    (is (= :implemented (iso3166/maturity "AUS")))
    (is (= :implemented (iso3166/maturity "KOR")))
    (is (= :implemented (iso3166/maturity "POL")))
    (is (= :implemented (iso3166/maturity "MEX")))
    (is (= :blueprint (iso3166/maturity "SAU")))
    (is (= :implemented (iso3166/maturity "CAN")))
    (is (= :blueprint (iso3166/maturity "NZL")))
    (is (= :implemented (iso3166/maturity "ZAF")))
    (is (= :blueprint (iso3166/maturity "CHL")))
    (is (= :blueprint (iso3166/maturity "NGA")))
    (is (= :implemented (iso3166/maturity "IDN")))
    (is (= :blueprint (iso3166/maturity "IRL")))
    (is (= :implemented (iso3166/maturity "NLD")))
    (is (= :blueprint (iso3166/maturity "VNM")))
    (is (= :blueprint (iso3166/maturity "THA")))
    (is (= :blueprint (iso3166/maturity "COL")))
    (is (= :implemented (iso3166/maturity "ESP")))
    (is (= :blueprint (iso3166/maturity "PHL")))
    (is (= :blueprint (iso3166/maturity "PER")))
    (is (= :implemented (iso3166/maturity "ITA")))
    (is (= :blueprint (iso3166/maturity "BGD")))
    (is (= :blueprint (iso3166/maturity "ARG")))
    (is (= :blueprint (iso3166/maturity "GHA")))
    (is (= :implemented (iso3166/maturity "FRA")))
    (is (= :blueprint (iso3166/maturity "EGY")))
    (is (= :blueprint (iso3166/maturity "PAK")))
    (is (= :implemented (iso3166/maturity "TUR")))
    (is (= :blueprint (iso3166/maturity "MAR")))
    (is (= :blueprint (iso3166/maturity "ETH")))
    (is (= :blueprint (iso3166/maturity "SWE")))
    (is (= :blueprint (iso3166/maturity "KAZ")))
    (is (= :blueprint (iso3166/maturity "QAT")))
    (is (= :implemented (iso3166/maturity "CHN")))
    (is (= :blueprint (iso3166/maturity "CRI")))
    (is (= :blueprint (iso3166/maturity "CZE")))
    (is (= :blueprint (iso3166/maturity "UKR")))
    (is (= :blueprint (iso3166/maturity "ISR")))
    (is (= :blueprint (iso3166/maturity "URY")))
    (is (= :blueprint (iso3166/maturity "EST")))
    (is (= :blueprint (iso3166/maturity "RWA")))
    (is (= :blueprint (iso3166/maturity "PAN")))
    (is (= :blueprint (iso3166/maturity "GEO")))
    (is (= :blueprint (iso3166/maturity "JOR")))
    (is (= :blueprint (iso3166/maturity "SEN")))
    (is (= :blueprint (iso3166/maturity "NPL")))
    (is (= :blueprint (iso3166/maturity "FIN")))
    (is (= :blueprint (iso3166/maturity "TUN")))
    (is (= :blueprint (iso3166/maturity "NOR")))
    (is (= :blueprint (iso3166/maturity "LKA")))
    (is (= :blueprint (iso3166/maturity "BWA")))
    (is (= :blueprint (iso3166/maturity "DNK")))
    (is (= :blueprint (iso3166/maturity "LVA")))
    (is (= :blueprint (iso3166/maturity "ECU")))
    (is (= :blueprint (iso3166/maturity "ISL")))
    (is (= :blueprint (iso3166/maturity "LTU")))
    (is (= :blueprint (iso3166/maturity "ZMB")))
    (is (= :blueprint (iso3166/maturity "HUN")))
    (is (= :blueprint (iso3166/maturity "HRV")))
    (is (= :blueprint (iso3166/maturity "NAM")))
    (is (= :blueprint (iso3166/maturity "SVK")))
    (is (= :blueprint (iso3166/maturity "BOL")))
    (is (= :blueprint (iso3166/maturity "KHM")))
    (is (= :implemented (iso3166/maturity "RUS")))
    (is (= :implemented (iso3166/maturity "BEL")))
    (is (= :implemented (iso3166/maturity "AUT")))
    (is (= :implemented (iso3166/maturity "CHE")))
    (is (= :blueprint (iso3166/maturity "ARM")))
    (is (= :blueprint (iso3166/maturity "BHR")))
    (is (= :blueprint (iso3166/maturity "PRT")))
    (is (= :blueprint (iso3166/maturity "SRB")))
    (is (= :blueprint (iso3166/maturity "PRY")))
    (is (= :blueprint (iso3166/maturity "BGR")))
    (is (= :blueprint (iso3166/maturity "CYP")))
    (is (= :blueprint (iso3166/maturity "SVN")))
    (is (= :blueprint (iso3166/maturity "LUX")))
    (is (= :blueprint (iso3166/maturity "MLT")))
    (is (= :blueprint (iso3166/maturity "ROU")))
    (is (= :blueprint (iso3166/maturity "GRC")))
    (is (= :blueprint (iso3166/maturity "ALB")))
    (is (= :blueprint (iso3166/maturity "BIH")))
    (is (= :blueprint (iso3166/maturity "AZE")))
    (is (= :blueprint (iso3166/maturity "CUB")))
    (is (= :blueprint (iso3166/maturity "DOM")))
    (is (= :blueprint (iso3166/maturity "GTM")))
    (is (= :blueprint (iso3166/maturity "HND")))
    (is (= :blueprint (iso3166/maturity "JAM")))
    (is (= :blueprint (iso3166/maturity "FJI")))
    (is (= :blueprint (iso3166/maturity "BRN")))
    (is (= :blueprint (iso3166/maturity "BLR")))
    (is (= :blueprint (iso3166/maturity "AGO")))
    (is (= :blueprint (iso3166/maturity "CIV")))
    (is (= :blueprint (iso3166/maturity "CMR")))
    (is (= :blueprint (iso3166/maturity "DZA")))
    (is (= :blueprint (iso3166/maturity "GAB")))
    (is (= :blueprint (iso3166/maturity "BTN")))
    (is (= :blueprint (iso3166/maturity "BEN")))
    (is (= :blueprint (iso3166/maturity "BFA")))
    (is (= :blueprint (iso3166/maturity "MLI")))
    (is (= :blueprint (iso3166/maturity "TGO")))
    (is (= :blueprint (iso3166/maturity "NER")))
    (is (= :blueprint (iso3166/maturity "MDG")))
    (is (= :blueprint (iso3166/maturity "BDI")))
    (is (= :blueprint (iso3166/maturity "MOZ")))
    (is (= :blueprint (iso3166/maturity "MWI")))
    (is (= :blueprint (iso3166/maturity "TZA")))
    (is (= :blueprint (iso3166/maturity "UGA")))
    (is (= :blueprint (iso3166/maturity "ZWE")))
    (is (= :blueprint (iso3166/maturity "CAF")))
    (is (= :blueprint (iso3166/maturity "COD")))
    (is (= :blueprint (iso3166/maturity "COG")))
    (is (= :blueprint (iso3166/maturity "LBR")))
    (is (= :blueprint (iso3166/maturity "LSO")))
    (is (= :blueprint (iso3166/maturity "SLE"))))
  (testing "a registry-only country entry is :spec"
    (is (= :spec (iso3166/maturity "AFG")))
    (is (= :spec (iso3166/maturity "ERI"))))
  (testing "maturity-summary counts tiers"
    (let [m (iso3166/maturity-summary)]
      (is (= (:total m) (+ (:spec m) (:blueprint m) (:implemented m))))
      (is (= 227 (:total m)))
      (is (= 132 (:blueprint m)))
      (is (= 24 (:implemented m)))
      (is (= 71 (:spec m))))))

(deftest maturity-roadmap-next-step
  (is (nil? (:next-step (iso3166/maturity-roadmap "JPN"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "USA"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "CHN"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "DEU"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "GBR"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "FRA"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "CAN"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "AUS"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "KOR"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "IND"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "BRA"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "SGP"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "NLD"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "ESP"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "ITA"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "MEX"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "IDN"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "AUT"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "BEL"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "CHE"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "TUR"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "ZAF"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "POL"))))
  (is (= :blueprint (:next-step (iso3166/maturity-roadmap "AFG"))))
  (is (nil? (:next-step (iso3166/maturity-roadmap "RUS")))))

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

(deftest usa-agency-children
  (let [cs (iso3166/children "USA")]
    (is (= 15 (count cs)))
    (is (every? #(= "USA" (:parent %)) cs))
    (is (some #(= "USA-GSA" (:code %)) cs))))

(deftest contacts-load
  (let [c (iso3166/contacts)]
    (is (>= (count c) 150))
    (let [meti (iso3166/get-contact "JPN-METI")]
      (is (some? meti))
      (is (= "経済産業大臣" (:head-role meti)))
      (is (string? (get-in meti [:hq :line-local])))
      (is (string? (get-in meti [:hq :phone]))))
    (let [gsa (iso3166/get-contact "USA-GSA")]
      (is (some? gsa))
      (is (string? (:head-role gsa)))
      (is (string? (get-in gsa [:hq :line-en]))))
    (is (nil? (iso3166/get-contact "ZZZ-NOPE")))))

(deftest agency-registry-carries-hq-fields
  (let [meti (iso3166/get-country "JPN-METI")]
    (is (string? (:hq-phone meti)))
    (is (string? (:hq-line-local meti)))
    (is (string? (:head-role meti)))
    (is (string? (:official-url meti))))
  (let [gsa (iso3166/get-country "USA-GSA")]
    (is (string? (:hq-line-en gsa)))
    (is (string? (:head-role gsa)))))
