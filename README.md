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

At the COUNTRY level, a country is not itself a business-function taxonomy,
so entries are FLAT -- every one of the 193 current UN-member states is
independently blueprint-eligible. ADR-2607040100 adds a SECOND, AGENCY level
for Japan only (`:level :ministry` / `:agency` / `:independent-commission`,
`:parent "JPN"`), mirroring COFOG's division/group coordinator+leaf pattern
(ADR-2606301900): `cloud-itonami-iso3166-jpn` is the country-level
coordinator, and per-agency blueprints (`cloud-itonami-iso3166-jpn-meti`,
etc.) are leaves under it. Use `(iso3166/children "JPN")` to list them. Other
countries stay flat until this pattern is validated.

## Current ISO 3166 Blueprints (country level)

| Code | Country | Blueprint |
|---|---|---|
| JPN | Japan | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Japan |
| USA | United States of America | Independent Public-Sector Market-Entry & Procurement Compliance Service -- United States |
| DEU | Federal Republic of Germany | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Germany |
| KEN | Kenya | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Kenya |
| IND | Republic of India | Independent Public-Sector Market-Entry & Procurement Compliance Service -- India |
| BRA | Federative Republic of Brazil | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Brazil |
| GBR | United Kingdom | Independent Public-Sector Market-Entry & Procurement Compliance Service -- United Kingdom |
| SGP | Singapore | Independent Public-Sector Market-Entry & Procurement Compliance Service -- Singapore |

8 countries spanning East Asia / North America / Western Europe /
Sub-Saharan Africa / South Asia / South America / common-law Europe /
Southeast Asia are `:maturity :blueprint`. The remaining 185 entries (full
193/193 current-UN-member coverage) are registered at `:maturity :spec`
(registry-only stub) for future promotion, following the same `:spec` ->
`:blueprint` -> `:implemented` path `kotoba-industry` / `kotoba-occupation`
/ `kotoba-cofog` use.

Alpha-3 code + English/local name are reused verbatim from
`com-etzhayyim-ooyake`'s Wikidata-verified `gov-units.*.edn` registries (a
one-time maintainer pull of CURRENT UN members, 2026-06-03), not re-derived
-- same reuse discipline `kotoba-cofog` applied to `matsurigoto`'s COFOG
backbone.

## Current ISO 3166 Blueprints (Japan agency level)

| Code | Agency | Ooyake ID | Blueprint |
|---|---|---|---|
| JPN-METI | Ministry of Economy, Trade and Industry (経済産業省) | `gov.jpn.meti` | Independent METI-Regulated Trade & Industrial-Policy Compliance Service -- Japan |
| JPN-MOF | Ministry of Finance (財務省) | `gov.jpn.mof` | Independent MOF-Regulated Customs & Tax Compliance Service -- Japan |
| JPN-DIGITAL | Digital Agency (デジタル庁) | `gov.jpn.digital` | Independent Digital-Agency-Regulated GovTech Procurement Compliance Service -- Japan |
| JPN-JFTC | Fair Trade Commission / 公正取引委員会 | `gov.jpn.competition` | Independent JFTC Antitrust & Bid-Rigging Compliance Service -- Japan |
| JPN-PPC | Personal Information Protection Commission / 個人情報保護委員会 | `gov.jpn.dataprotection` | Independent PPC Personal-Data Compliance Service -- Japan |
| JPN-MOJ | Ministry of Justice (法務省) | `gov.jpn.moj` | Independent MOJ-Regulated Corporate Registry & Status-of-Residence Compliance Service -- Japan |
| JPN-MHLW | Ministry of Health, Labour and Welfare (厚生労働省) | `gov.jpn.mhlw` | Independent MHLW-Regulated Labor-Standards Compliance Service -- Japan |
| JPN-FSA | Financial Services Agency (金融庁) | `gov.jpn.finreg` | Independent FSA Payment-Services & Financial-Regulatory Compliance Service -- Japan |
| JPN-MAFF | Ministry of Agriculture, Forestry and Fisheries (農林水産省) | `gov.jpn.maff` | Independent MAFF Food-Safety & JAS-Certification Compliance Service -- Japan |
| JPN-MLIT | Ministry of Land, Infrastructure, Transport and Tourism (国土交通省) | `gov.jpn.mlit` | Independent MLIT Construction-Business Licensing Compliance Service -- Japan |
| JPN-MOE | Ministry of the Environment (環境省) | `gov.jpn.moe` | Independent MOE Environmental-Assessment & Waste-Permit Compliance Service -- Japan |
| JPN-MOFA | Ministry of Foreign Affairs (外務省) | `gov.jpn.mofa` | Independent MOFA ODA/JICA Tender-Eligibility Compliance Service -- Japan |
| JPN-MOD | Ministry of Defense (防衛省) | `gov.jpn.mod` | Independent MOD Defense-Equipment Transfer & Security-Clearance Compliance Service -- Japan |
| JPN-AUDIT | Board of Audit (会計検査院) | `gov.jpn.audit` | Independent Board-of-Audit Readiness Compliance Service -- Japan |
| JPN-CAO | Cabinet Office (内閣府) | `gov.jpn.cao` | Independent Cabinet-Office Cross-Ministerial Program Compliance Service -- Japan |
| JPN-MIC | Ministry of Internal Affairs and Communications (総務省) | `gov.jpn.mic` | Independent MIC Telecom & Broadcasting Licensing Compliance Service -- Japan |
| JPN-MEXT | Ministry of Education, Culture, Sports, Science and Technology (文部科学省) | `gov.jpn.mext` | Independent MEXT Research-Grant & School-Accreditation Compliance Service -- Japan |
| JPN-RECONSTRUCTION | Reconstruction Agency (復興庁) | `gov.jpn.reconstruction` | Independent Reconstruction-Agency Special-Zone Procurement Compliance Service -- Japan |
| JPN-STATISTICS | Statistics Japan (総務省統計局) | `gov.jpn.statistics` | Independent Statistics-Japan Reporting-Obligation Compliance Service -- Japan |

**19/19 Japan agencies are now `:maturity :blueprint` — full coverage
complete** (ADR-2607040100 + ADR-2607040200 + ADR-2607040300 +
ADR-2607040400 + ADR-2607040500), spanning all three agency types
(ministry / agency / independent commission). Every agency entry's
`:ooyake-id` cross-references the source record in `com-etzhayyim-ooyake`'s
`gov-units.jp-central.seed.edn` / `gov-units.seed.edn` /
`gov-units.oversight-*.edn` (IDs, names, and official URLs reused verbatim,
not re-derived).

## Test

```bash
clojure -M:test
```
