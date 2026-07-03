(ns kotoba.iso3166
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]
            [kotoba.technology :as technology]))

(def registry-resource "kotoba/iso3166/registry.edn")

(defn registry []
  (edn/read-string (slurp (io/resource registry-resource))))

(defn countries
  ([] (:iso3166 (registry)))
  ([reg] (:iso3166 reg)))

(defn by-code
  ([] (by-code (registry)))
  ([reg] (into {} (map (juxt :code identity) (countries reg)))))

(defn get-country
  ([code] (get-country (registry) code))
  ([reg code] (get (by-code reg) (str/upper-case (str code)))))

(defn required-technologies
  ([code] (required-technologies (registry) code))
  ([reg code] (:required-technologies (get-country reg code))))

(defn optional-technologies
  ([code] (optional-technologies (registry) code))
  ([reg code] (:optional-technologies (get-country reg code))))

(defn technology-stack
  "Resolve the required technology records for a country's market-entry business."
  ([code] (technology-stack (registry) code))
  ([reg code]
   (technology/stack (required-technologies reg code))))

(defn readiness
  "Return an execution-readiness summary for an ISO 3166 alpha-3 code and available technology IDs."
  [code available-tech-ids]
  (let [country (get-country code)
        required (set (:required-technologies country))
        available (set available-tech-ids)
        missing (set/difference required available)]
    {:iso3166 (:code country)
     :business-id (:business-id country)
     :ready? (empty? missing)
     :required required
     :available available
     :missing missing
     :operating-states (:operating-states country)}))

(defn execution-plan
  "Data contract cloud-itonami-iso3166 can expose in business state."
  [code]
  (let [country (get-country code)
        stack (technology-stack code)]
    {:iso3166 (:code country)
     :business-id (:business-id country)
     :country (:name country)
     :maturity (:maturity country)
     :required-technologies (:required-technologies country)
     :optional-technologies (:optional-technologies country)
     :operating-states (:operating-states country)
     :ui-ready? (some :ui? stack)
     :export-ready? (some :export? stack)
     :technology-stack (mapv #(select-keys % [:id :name :layer :capabilities :repos :contracts :ui? :export?])
                             stack)}))

(defn maturity
  "Return the maturity level of an ISO 3166 entry: :spec (registry only),
  :blueprint (blueprint repo published), or :implemented (source actor exists).
  Defaults to :spec when unset."
  [code]
  (let [country (get-country code)]
    (or (:maturity country)
        (cond
          (:implemented? country) :implemented
          (:repo country)         :blueprint
          :else                   :spec))))

(defn maturity-summary
  "Aggregate maturity counts across all ISO 3166 entries."
  []
  (let [cs (countries)]
    {:total       (count cs)
     :spec        (count (filter #(= :spec (maturity (:code %))) cs))
     :blueprint   (count (filter #(= :blueprint (maturity (:code %))) cs))
     :implemented (count (filter #(= :implemented (maturity (:code %))) cs))}))

(defn maturity-roadmap
  "Return the next maturity step for an ISO 3166 entry: :spec->:blueprint->:implemented,
  with the action required to advance and whether a capability lib with UI/export
  already backs it."
  [code]
  (let [country (get-country code)
        level (maturity code)
        stack (technology-stack code)
        ui? (some :ui? stack)
        export? (some :export? stack)
        has-repo (boolean (:repo country))]
    {:iso3166 (:code country)
     :maturity level
     :next-step (condp = level
                  :spec        :blueprint
                  :blueprint   :implemented
                  :implemented nil)
     :next-action (condp = level
                    :spec        "publish a blueprint repo (scaffold + blueprint.edn + docs)"
                    :blueprint   "implement the actor (source + tests)"
                    :implemented "at maturity ceiling")
     :ui-ready? ui?
     :export-ready? export?
     :has-repo has-repo}))
