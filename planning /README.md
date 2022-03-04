# TDT4100-project plan

## Regningssystem 

Planen er å lage et regningssystem hvor bruker har mulighet til å lage regninger fra et firma. Bruker kan legge inn Kunde/selger, alle produkter kunden har kjøpt med pris (med mulighet for å legge inn produkter så man slipper å legge inn samme flere ganger.), programmet regner ut totalpris, mva og har mulighet til å legge til eventuelle rabatter. Alle regninger som tidligere er laget og sendt er mulig å hente opp igjen fra tekstfil, men ikke mulighet til å endre etter sending. Man kan se totalt utestående, totalt fakturert for året og total margin for året på hovedsiden. Man kan lagre en regning under redigering før man sender den, og så hente denne opp igjen senere. 


1. Hoveklassen vil være en dataorientert-klasse ansvarlig for å lagre alle verdier som bruker legger inn. Dette vil lagres til en txt fil med en struktur som gir mening for denne. Det vil i utgangspunktet være 2 forskjellige tekstfiler som er ansvarlig for å lagre data fra objektene. En som lagrer ferdig sendte fakturaer og en som lagrer uferdige fakturaer. 
2. En annen klasse vil håndtere kalkulasjonene som er nødvendig for å regne ut mva, total og evt rabatt. Denne vil også ta seg av å regne ut totalt utestående på fakturaer samt total for året.
3. En tredje "hjelpeklasse" vil være ta seg av å eksportere en faktura fra programmet til PDF slik at denne kan sendes til en kunde.  

## Testing

For å teste appen vil det være relevant å legge inn ulike data som kanksje ikke gir ønsket tilstand. Prøve å så eksportere denne dataen og så hva som fungerer / ikke fungerer. Det kan også være relevant å prøve å legge inn store mengder data for å se om det ødelegger database / eksporterings-funksjoner. 

## Andre teknologier

Bruker maven for å enklet kunne bruke Apache PDF box for å eksportere fakturaer som pdf samt for å forenkle oppsett av prosjektet.




## Lover og regler som følges 

https://www.sendregning.no/faktura/fakturanummer-krav/

En nummerserie for en bedrift. Etter loven skal en nummersere velges når første faktura opprettes og man kan ikke gå vekk fra denne, da samtidig ikke slette fakturaer når en er sendt. 

altinn.no/starte-og-drive/regnskap-og-revisjon/regnskap/faktura-salgsdokumentasjon/

Følge retningslinjer for hva en faktura skal inneholde, slik alltinn har beskrevet det. 


Orgnummer 
https://no.wikipedia.org/wiki/MOD11
