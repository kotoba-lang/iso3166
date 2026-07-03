# kotoba-iso3166

ISO 3166-1 alpha-3 country registry for kotoba-lang and itonami open
businesses -- the jurisdiction/market-entry counterpart to `kotoba-industry`
(ISIC-coded businesses), `kotoba-occupation` (ISCO-08-coded occupations),
and `kotoba-cofog` (COFOG-coded government functions).

This repository maps each country to the technology capabilities needed to
run an independent public-sector market-entry & procurement-compliance
navigator business for that jurisdiction.

## Contract

```clojure
(require '[kotoba.iso3166 :as iso3166])

(iso3166/get-country "JPN")
(iso3166/required-technologies "DEU")
(iso3166/readiness "USA" #{:identity :forms :dmn :bpmn :audit-ledger})
```

## Layers

- business: customer-facing open ISO 3166 market-entry blueprint
- iso3166: country-coded operating domain (which jurisdiction's rules apply)
- technology: reusable kotoba-lang capability stack
- implementation: concrete repos, services and operators

ISIC classifies what a *business* produces; ISCO classifies what a *worker*
does; COFOG classifies what *government function* a service serves; ISO 3166
classifies **which jurisdiction's market-entry and public-procurement rules
apply**. `cloud-itonami-iso3166-{code}` publishes forkable OSS operator
businesses that help an ALREADY-INCORPORATED operator win and service a
public-sector contract in that specific country -- distinct from
`cloud-itonami-M6910` (company *incorporation* itself) and from
`cloud-itonami-cofog-{code}` (a jurisdiction-agnostic operator template for
ONE public function). See `docs/cloud-itonami.md` for the full boundary with
adjacent etzhayyim/cloud-itonami actors.

Unlike COFOG (division/group hierarchy), a country is not itself a
business-function taxonomy, so entries here are FLAT -- every one of the
193 current UN-member states is independently blueprint-eligible.

## Current ISO 3166 Blueprints

| Code | Country | Blueprint |
|---|---|---|
| JPN | Japan | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Japan |
| USA | United States of America | Independent Public-Sector Market-Entry & Procurement Compliance Service -- United States |
| DEU | Federal Republic of Germany | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Germany |
| KEN | Kenya | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Kenya |
| IND | Republic of India | Independent Public-Sector Market-Entry & Procurement Compliance Service -- India |

5 countries spanning East Asia / North America / Western Europe /
Sub-Saharan Africa / South Asia are `:maturity :blueprint`. The remaining
188 entries (full 193/193 current-UN-member coverage) are registered at
`:maturity :spec` (registry-only stub) for future promotion, following the
same `:spec` -> `:blueprint` -> `:implemented` path `kotoba-industry` /
`kotoba-occupation` / `kotoba-cofog` use.

Alpha-3 code + English/local name are reused verbatim from
`com-etzhayyim-ooyake`'s Wikidata-verified `gov-units.*.edn` registries (a
one-time maintainer pull of CURRENT UN members, 2026-06-03), not re-derived
-- same reuse discipline `kotoba-cofog` applied to `matsurigoto`'s COFOG
backbone.

## Test

```bash
clojure -M:test
```
