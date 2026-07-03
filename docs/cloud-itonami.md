# cloud-itonami-iso3166 Integration

`cloud-itonami-iso3166-*` repositories publish independent-operator
public-sector market-entry & procurement-compliance blueprints keyed by
jurisdiction. `kotoba-iso3166` declares what technology capabilities are
required to run them.

Runtime flow:

```text
cloud-itonami-iso3166-{code}
        |
        v
kotoba.iso3166/execution-plan
        |
        v
kotoba.technology/stack
        |
        v
concrete repos and operator services
```

The ISO 3166 blueprint should not import a capability implementation
directly. It should request the capability contract from `kotoba-technology`.

## Maturity & readiness

`kotoba.iso3166/maturity-summary` and `execution-plan` expose per-country
maturity and UI/export readiness so an operator console can show them.

| Maturity tier | Meaning |
|---|---|
| `:implemented` | source actor exists (reference implementation) |
| `:blueprint` | blueprint repo published (`:repo` set) |
| `:spec` | registry entry only (blueprint repo pending) |

Current state (full 193/193 current-UN-member coverage):

- Total entries: 193
- `:implemented` 0 · `:blueprint` 5 · `:spec` 188

No `:robotics` requirement on any entry: this is a data/compliance service,
not a physical-domain business (same digital-service exemption class as
`cloud-itonami-6310` and `cloud-itonami-gtin-*`, ADR-2607011000 /
ADR-2607031800).

## Boundary with adjacent etzhayyim / cloud-itonami actors

This registry sits alongside FIVE other things in this workspace that also
touch "country" or "government" and must not be conflated:

- **`com-etzhayyim-ooyake`** (etzhayyim/root) -- a read-only, non-commercial
  civic-wayfinding MIRROR of government structure. Already carries one
  `:gov.unit` record per country (this registry's alpha-3/name source), but
  is constitutionally barred from claiming to BE the government or act as
  an official channel (G3). `kotoba-iso3166` / `cloud-itonami-iso3166-*` is
  commercial and never touches `ooyake`'s civic-map data model.
- **`matsurigoto`** (etzhayyim/root) -- sovereign e-government statecraft,
  either etzhayyim's own covenant self-governance or an ADOPTING
  NATION-STATE's own execution. Two principals, both "the government
  itself," constitutionally gated (no operator master-key, spec-derived
  only, authority-bearing). `cloud-itonami-iso3166-*` is a THIRD, separate
  principal: an independent operator the government CONTRACTS or that
  bids into that government's public procurement -- never the government.
- **`com-etzhayyim-toritsugi`** (etzhayyim/root) -- citizen-facing, member-
  consent-only, non-profit government-procedure concierge for an
  INDIVIDUAL's own benefit/procedure. `cloud-itonami-iso3166-*` never
  touches individual citizen procedures; its client is a business operator,
  not a citizen.
- **`legal-entity.etzhayyim.com`** -- read-only aggregated company-registry
  data across 190+ countries, no execution. `cloud-itonami-iso3166-*` is
  commercial and executes (gated) filings.
- **`cloud-itonami-M6910`** (cloud-itonami org) -- helps a client BECOME a
  legal entity in a jurisdiction (company formation/incorporation, ISIC
  6910); jurisdiction is internal per-country DATA
  (`formation.facts/catalog`), not a blueprint-per-country split.
  `cloud-itonami-iso3166-{code}` is the NEXT phase: an ALREADY-incorporated
  operator (e.g. a `cloud-itonami-cofog-04.5` fire-inspection business)
  navigating THAT country's public-procurement portal, local-content/
  licensing rules, and public-sector invoicing/tax mapping to actually win
  and service a government contract. Different regulatory domain (company
  law vs. public-procurement law), different client lifecycle stage.
- **`cloud-itonami-cofog-{code}`** (cloud-itonami org) -- a
  jurisdiction-AGNOSTIC operator template for ONE public function (fire
  inspection, waste collection, ...). `cloud-itonami-iso3166-{code}` is the
  orthogonal axis: jurisdiction-SPECIFIC market-entry mechanics that apply
  regardless of which COFOG function the operator performs. The two
  compose: an operator forks a COFOG-function blueprint AND an ISO3166
  jurisdiction blueprint to actually operate a licensed civic-tech business
  in a given country.
