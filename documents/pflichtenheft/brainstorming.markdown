Braunstorming für das Pflichtenheft
Stand: 15.04.2011

# Zielbestimmung (Christian)

## Musskriterien

* 3D-Visualisierung der Achterbahn

* 2D-Visualisierung der Beschleunigungen (oder Kräfte), die auf die Insassen des Achterbahnwaagen einwirken.

* Oberfläche für das Einlesen der Achterbahn-Spezfikationen

* Darstellung eines Bodens (Grundfläche bei z=0)

* mind. zwei Querbalken pro Streckenabschnitt

* Schienen sollen immer parallel laufen

* Echtzeitberechnung 

## Wunschkriterien

* Neben der Achterbahn wird auch die Umgebung mit Gebäuden, Vegetation, Himmel simululiert.

* Realistischer Boden (uneben)

* Tunnel

* Berücksichtigung der Bahnreibung

* Berücksichtigung der Luftreibung

Bei einer Berücksichtigung der Luftreibung reduziert sich die Gesamtenergie der simulierten Achterbahnwaagen mit der Zeit, und zwar quadratisch zur Geschwindigkeit und proportional zum Luftwiderstandsbeiwert des Waagens.

* Zusätzlicher Antrieb oder Bremsen

* Zeitraffer

* Aufnahmefunktion (Screenshots oder Film)

* Stützbalken für die Bahn

* Darstellung des Waagens

* Außenperspektive

* Berücksichtigung der Beschleunigungskräfte durch außeraxiale Massen

## Abgrenzungskriterien

* Die statische Integrität des Achterbahngerüstes wird nicht simuliert.
* Die Einflüsse der Umgebung auf die Achterbahn (z.B. Wetter) bleiben unberücksichtigt.
* Eine Simulation des Regelbetriebs mit Passagierein- oder -ausstieg findet nicht statt.
* Kein Editor

# Produkteinsatz (Daniel)

## Anwendungsbereiche
Die Simulation soll dem technischen Entwickler der Achterbahnen als Visualisierungswerkzeug für die physikalischen Gegebenheiten dienen. Ein Einsatz zu Präsentationszwecken soll möglich sein.

## Zielgruppen
Ingenieure, Publikum (Auftraggeber)

## Betriebsbedingungen

Einzelplatzanwendung
Bürorechner oder Notebook
2 Jahre alte Grafikkarte mit 3D Beschleunigung

Zusammenspiel mit dem existierenden Editor

# Produktübersicht (Matthias)

* Einlesen der Achterbahnspezifikation aus XML-Datei (alte Datei verwerfen)
* Starten/Stoppen/Pausieren der 3D Simulation
* Ein- und Ausblenden der Beschleunigungsdaten
* Ein- und Ausblenden des HUD Displays
* (optional) Wechseln der Kameraperspektive
* (optional) Zeitfaktor anpassen
* (optional) Grafische/physikalische Einstellungen anpassen

# Produktfunktionen (Matthias)

## F100

Geschäftsprozess: Einlesen Achterbahn-Spezifikationsdatei

Ziel: Eine Achterbahnspezifikation wird aus einer Datei geladen in den Arbeitsspeicher geladen.

Vorbedingung: 

Nachbedingung Erfolg:

Nachbedingung Fehlschlag: 

(Ergänzen)

# Produktdaten (Konstantin)

Achterbahndaten (nur lesen)
Auswertungsdaten
Videodaten (AVI)

Alle Daten werden im Dateisystem gehalten.

## Stützstellen (max. 1000)

Der Datensatz für eine Achterbahn besteht aus einer geordneten Liste von Stützpunkten. Für jeden Stützpunkt werden Position und Orientierung in einem dreidimensionalen Bezugssystem gespeichert. Die Orientierung erfolgt durch Angabe des jeweilige Giervektors.

# Produktleistungen (Simon)

Die Simulation der Achterbahn muss in Echtzeit möglich sein.

Die Energieerhaltung für den simulierten Wagen muss sichergestellt sein. Ohne Reibung und Luftwiderstand soll die Achterbahn unbeschränkt weiterfahren können.

# Qualitätsanforderungen (Simon)

(siehe Liste)
Bedienbarkeit: sehr gut
Zeitverhalten: sehr gut

# Benutzeroberfläche (Robin)

(Entwurf Bildschirmaufteilung)
(Entwurf für Datei-Dialogfenster)
(Entwurf für die 2D-Daten)

* Die Benutzerelemente sollen ausblendbar und verschiebbar sein.
* (optional) In der 3D-Anssicht soll über ein heads-up display (HUD) die wesentlichen physikalischen Daten einblendbar sein.

# Nichtfunktionale Anforderungen

# Technische Produktumgebung (Simon)

Einzelplatzanwendung, Linux, 3D (siehe oben)

## Software
## Hardware

PC mit OpenGL-fähige Grafikkarte

# Produktschnittstellen

Die Übergabe der Achterbahn-Daten aus dem bestehenden Editor an den Simulator erfolgt als schematisierte XML-Datei. (Schema => Christian)

(Referenz auf Schema einfügen)

# Glossar

Gierachse: Im dreidimensionalen Raum kann ein lokales Koordinatensystem durch drei Vektoren festgelegt werden. Für die Achterbahn wählt man den Geschwindigkeitsvektor tangential zur Bahn (Rollachse), die Richtung der Querbalken (Nickachse) und deren Kreuzprodukt (Gierachse). Die Gierachse legt fest, welche Richtung von den Passagieren als aufrecht empfunden wird.
