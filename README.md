# TDT4100-project plan

## Inventory management system 

Planen er å lage en form for lagerbeholdningssystem for en nettbutikk hvor en bruker kan legge inn nye produkter med bilde, pris, spesifikasjoner etc. 
Appliksjonen vil ha 3 hovedfunksjoner: 

1. Oversikt over alle varer som er i systemet, med muligheten til å filtrere på kategori samt søke etter varenummer. Her kan man også fjerne/slette produkter fra databasen. (Klasse 1)
2. Legge inn nye varer i databasen i et eget panel, hvor man kan legge inn bilde, pris, kost, mva prosent (her blir total mva på produkt regnet ut), spesifikasjoner (forenklet: Kun mulighet for lengde, bredde, høyde, vekt og produsent) og lagerbeholdning m.m. Denne dataen vil så bli skrevet til databasen og et varenummer blir generert automatisk, med tomme verider for data som ikke er lagt inn. (Klasse 2)
3. Eksportere all data som er i databasen til excel-fil og regne ut total lagerbeholdning, total salgskost, se avanse på produkter og regne ut total mva regning for alle varer. (Klasse 3)

## Testing

For å teste appen vil det være relevant å legge inn ulike data som kanksje ikke gir ønsket tilstand. Prøve å så eksportere denne dataen og så hva som fungerer / ikke fungerer. Det kan også være relevant å prøve å legge inn store mengder data for å se om det ødelegger database / eksporterings-funksjoner. 

## Andre teknologier

Som database velger jeg å bruke SQLite (https://www.sqlite.org/index.html), jeg bruker java sitt innebygde object java.sql for å interagere med databasen. Databasen og alle constrainst/datatyper samt tables/rows for denne lager jeg på forhand og klassen kan kun skrive til og fjerne elementer fra databasen. For å eksportere til excel-fil bruker jeg Apache POI biblioteket (https://poi.apache.org) eller easyXLS for å konvertere data som er i databasen til excel fil. Brukergrensesnittet vil lages i JavaFX. Bruker maven for filstruktur samt for å bygge prosjektet. 