# Lab - Architecture Hexagonale

---

## Préparation

**☛ <u>à faire impérativement AVANT le Lab !!!</u> ☚**

Pour améliorer au maximum votre confort dans le déroulement de ce lab :<br/>
effectuer les actions suivantes **avant** le début du lab<br/>
... de préférence chez vous<br/>
(avec une bonne connexion internet)

```bash
git clone https://github.com/jycr/devoxx-lab-architecture-hexagonale
cd devoxx-lab-architecture-hexagonale
git switch solution && ./mvnw dependency:go-offline
git switch -
```

----

## Prérequis pour ce lab

* Connaitre le langage Java
* Avoir des bases dans l'usage de Git, Maven
* Avoir un JDK (Java >= 17)

---

## Objectifs

Développer un petit système de courtage boursier

→ en s'appuyant sur une<br/>
**architecture applicative hexagonale**

----

### Nous verrons, dans ce contexte

* la gestion de la persistance
* la validation de la cohérence fonctionnelle des objets du domaine
* la gestion des transactions
* l'approche [CQRS](https://fr.wikipedia.org/wiki/S%C3%A9paration_commande-requ%C3%AAte)

---

## Architecture hexagonale

Qu'est-ce que c'est ?

*Commençons par un peu de théorie ...*

----

## Quels sont les enjeux

* S'adapter à l'obsolescence technologique
* Avoir un feedback ultra rapide

Note:

* Obsolescence technologique :
	* Choisir les frameworks le plus tard possible
	* Changer de frameworks sans toucher au métier
* Feedback de dev :
	* Pas besoin de démarrer toute la stack pour tester le métier

----

## Différentes orientations

<table>
	<thead>
		<tr>
			<th style="text-align:center">Database centric</th>
			<th style="text-align:center">Domain centric</th>
		</tr>
	</thead>
	<tbody style="background:#FFF">
		<tr>
			<td>
				<svg xmlns="http://www.w3.org/2000/svg" style="font: bold 12px sans-serif;text-anchor:middle" viewBox="0 0 200 200">
					<circle cx="100" cy="100" r="100" fill="#9b59b6" /><text x="100" y="15" fill="#FFF">UI</text>
					<circle cx="100" cy="100" r="80" fill="#2ecc71" /><text x="100" y="45">Business Logic</text>
					<circle cx="100" cy="100" r="50" fill="#e67e22" /><text x="100" y="80">Data Access</text>
					<path style="transform:translate(100px,100px);fill:#FFF;stroke-width:4px;stroke:#000" d="m-10.5-7.5c0 6 21 6 21 0v7.5c0 6-21 6-21 0zm0 0c0-6 21-6 21 0v15c0 6-21 6-21 0z"/>
				</svg>
			</td>
			<td>
				<svg xmlns="http://www.w3.org/2000/svg" style="font:bold 12px sans-serif;text-anchor:middle" viewBox="0 0 200 200">
					<defs>
						<clipPath id="clipPersistance"><rect y="100" width="200" height="100"/></clipPath>
						<clipPath id="clipUI"><rect width="200" height="100"/></clipPath>
					</defs>
					<circle cx="100" cy="100" r="100" clip-path="url(#clipUI)" fill="#9b59b6"/><text x="100" y="15" fill="#FFF">UI</text>
					<circle cx="100" cy="100" r="100" clip-path="url(#clipPersistance)" fill="#e67e22"/><text x="100" y="190">Persistence</text>
					<path style="transform:translate(185px,185px);fill:#FFF;stroke-width:4px;stroke:#000" d="m-10.5-7.5c0 6 21 6 21 0v7.5c0 6-21 6-21 0zm0 0c0-6 21-6 21 0v15c0 6-21 6-21 0z"/>
					<circle cx="100" cy="100" r="80" fill="#3498db"/><text x="100" y="45">Application</text>
					<circle cx="100" cy="100" r="50" fill="#2ecc71"/><text x="100" y="100">Domaine</text>
				</svg>
			</td>
		</tr>
	</tbody>
</table>

Note: Les principes de "Clean Architecture" → Le domaine métier : au centre

----

## L'architecture hexagonale

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<style>
		path.i{marker-end:url(#i);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px}
		path.p{marker-end:url(#p);fill:none;stroke:#000}
		path.a{marker-mid:url(#a);fill:none;stroke:#000}
	</style>
	<defs>
		<marker id="a" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<path fill="#2ecc71" stroke="#3498db" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z"/>
	<g transform="translate(66,82) rotate(-56.5)">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF" font-size="5">port primaire</text>
	</g>
	<text x="15" y="50" font-size="30">👤︎</text>
	<g transform="translate(52,61)">
		<rect fill="#9b59b6" x="-15" y="-14" width="30" height="18"/>
		<text fill="#FFF" font-size="14">UI</text>
		<path class="p" d="M10 20L1 13"/>
		<path class="a" d="M-2 4V9l.9 .2"/>
	</g>
	<g transform="translate(64,119) rotate(56.5)" style="font-size:5px">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">port primaire</text>
	</g>
	<g transform="translate(55,150)">
		<rect fill="#9b59b6" x="-15" y="-14" width="30" height="18"/>
		<text fill="#FFF" font-size="14">API</text>
		<path class="p" d="m7 -31l-8 6"/>
		<path class="a" d="m-5 -14v-8l.9 .2"/>
	</g>
	<g transform="translate(134,82) rotate(56.5)" style="font-size:5px">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">port secondaire</text>
	</g>
	<g transform="translate(144,50)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
			<tspan x="0">HTTP</tspan>
			<tspan x="0" dy="10">adapter</tspan>
		</text>
		<path class="p" d="M-7 30L1 24"/>
		<path class="a" d="m5 12.4v8.4l-.9 -.2"/>
		<path fill="none" stroke="#000" d="m17 3h17v-35"/>
	</g>
	<g transform="translate(177,25)">
		<path fill="#fff" stroke="#000" stroke-width="2" d="M-11 5c-6 0-11-4-11-10 0-5 5-9 10-9 2-4 7-7 12-7 7 0 12 4 14 10 4 0 8 4 8 8 0 5-4 8-9 8h-24"/>
		<text font-size="12" style="text-align:center;text-anchor:middle">{API}</text>
	</g>
	<g transform="translate(134.4,121) rotate(-56.5)" style="font-size:5px">
		<rect width="43" height="6" x="-21" y="-4" />
		<text fill="#FFF">port secondaire</text>
	</g>
	<g transform="translate(138,150)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
			<tspan x="0">ORM</tspan>
			<tspan x="0" dy="10">adapter</tspan>
		</text>
		<path class="p" d="M-3 -27L7-18"/>
		<path class="a" d="m10.1 -6.5v-7.5l-.9 -.2"/>
		<path fill="none" stroke="#000" d="m0 12.5v14h35"/>
	</g>
	<path transform="translate(185,180)" style="fill:#FFF;stroke-width:3px;stroke:#000" d="m-10.5-7.5c0 6 21 6 21 0v7.5c0 6-21 6-21 0zm0 0c0-6 21-6 21 0v15c0 6-21 6-21 0z"/>
	<text x="100" y="38">Application</text>
	<text transform="translate(102,100)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
</svg>

---

## Organisation du lab

Ce lab est composé de plusieurs étapes qui vous guiderons jusqu'à la solution finale.

----

### Pour chaque étape ...

Vous aurez :

* de nom de tag Git<br/>
  <small>partir de laquelle vous pourrez créer une branche pour vos développements</small>
* les exigences à couvrir<br/>
  <small>sous forme de scénarios écrit en langage [Gherkin](https://cucumber.io/docs/gherkin/)</small>

----

### La structure de base du projet ...

comporte :

* une chaine de build Maven
* une mise en place de Cucumber<br/>
  <small>avec un embryon de "glue" pour faciliter l'exécution des exigences</small>

---

## Etape 1

Mise en place du _métier de l'application_<br/>
*sans intégrer de framework technique* dans le code

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<defs>
		<marker id="a1" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p1" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i1" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<style>
		path.i1{marker-end:url(#i1);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px}
		path.p1{marker-end:url(#p1);fill:none;stroke:#000}
		path.a1{marker-mid:url(#a1);fill:none;stroke:#000}
	</style>
	<path fill="#2ecc71" stroke="#FFF" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z" />
	<g transform="translate(64,119) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4" />
		<text fill="#FFF">ServiceCourtage</text>
	</g>
	<g transform="translate(55,150)">
		<path class="p3" d="m7 -31l-8 6"/>
	</g>
	<text transform="translate(99,90)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
	<g transform="translate(100,120)" style="font:normal 5px monospace">
		<rect x="-20" y="-6" height="8" width="40" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Portefeuille</text>
	</g>
	<path class="i1" d="M100 134H81"/>
	<g transform="translate(103,135)" style="font:normal 5px monospace">
		<rect x="-15" y="-6" height="8" width="30" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Courtage</text>
	</g>
</svg>

----

Durée : XX min
<hr/>

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-1 etape-1
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw verify -Dcucumber.filter.tags="@E1"
```

----

### Domaine métier

> *Un service de courtage gére une liste de portefeuille*

* Code à gérer dans le module :<br/>
  `courtage-domain`
* Cours de bourse des actions :<br/>
  fournis par un service externe<br/>
  <small>_(cf. contrat d'interface : `ServiceBourse.java`)_</small>

Note: **Scénarios**

- L'identifiant d'un portefeuille est une chaîne de caractères.
- le service de courtage gère un ensemble de portefeuilles.
- le service de courtage calcule la valeur d'un portefeuille.
- La valeur d'un portefeuille vide est zéro

---

## Etape 2

* Mise en place du bouchon pour le *PortefeuilleRepository*

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<style>
		path.i2{marker-end:url(#i2);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px}
		path.p2{marker-end:url(#p2);fill:none;stroke:#000}
		path.a2{marker-mid:url(#a2);fill:none;stroke:#000}
	</style>
	<defs>
		<marker id="a2" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p2" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i2" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<path fill="#2ecc71" stroke="#FFF" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z" />
	<g transform="translate(64,119) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4" />
		<text fill="#FFF">ServiceCourtage</text>
	</g>
	<g transform="translate(55,150)">
		<path class="p3" d="m7 -31l-8 6"/>
	</g>
	<g transform="translate(134.4,121) rotate(-56.5)" style="font:bold 3px monospace">
		<rect width="43" height="6" x="-21" y="-4" />
		<text fill="#FFF">PortefeuilleRepository</text>
	</g>
	<g transform="translate(138,150)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19" />
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">DB</tspan>
		</text>
		<path class="p2" d="M-3 -27L7-18"/>
		<path class="a2" d="m10.1 -6.5v-7.5l-.9 -.2"/>
	</g>
	<text transform="translate(99,90)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
	<g transform="translate(100,120)" style="font:normal 5px monospace">
		<rect x="-20" y="-6" height="8" width="40" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Portefeuille</text>
	</g>
	<path class="i2" d="M100 134H81"/>
	<g transform="translate(103,135)" style="font:normal 5px monospace">
		<rect x="-15" y="-6" height="8" width="30" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Courtage</text>
	</g>
</svg>

----

Durée : XX min
<hr/>

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-2 etape-2
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw verify -Dcucumber.filter.tags="@E2"
```

---

## Etape 3

* Ajout de la gestion des actions au domaine métier
* Mise en place d'un bouchon pour la bourse

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<style>
		path.i3{marker-end:url(#i3);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px}
		path.p3{marker-end:url(#p3);fill:none;stroke:#000}
		path.a3{marker-mid:url(#a3);fill:none;stroke:#000}
	</style>
	<defs>
		<marker id="a3" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p3" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i3" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<path fill="#2ecc71" stroke="#FFF" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z" />
	<g transform="translate(64,119) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4" />
		<text fill="#FFF">ServiceCourtage</text>
	</g>
	<g transform="translate(55,150)">
		<path class="p3" d="m7 -31l-8 6"/>
	</g>
	<g transform="translate(134,82) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4" />
		<text fill="#FFF">ServiceBourse</text>
	</g>
	<g transform="translate(144,50)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19" />
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">Bourse</tspan>
		</text>
		<path class="p3" d="M-7 30L1 24"/>
		<path class="a3" d="m5 12.4v8.4l-.9 -.2"/>
	</g>
	<g transform="translate(134.4,121) rotate(-56.5)" style="font:bold 3px monospace">
		<rect width="43" height="6" x="-21" y="-4" />
		<text fill="#FFF">PortefeuilleRepository</text>
	</g>
	<g transform="translate(138,150)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19" />
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">DB</tspan>
		</text>
		<path class="p3" d="M-3 -27L7-18"/>
		<path class="a3" d="m10.1 -6.5v-7.5l-.9 -.2"/>
	</g>
	<text transform="translate(99,90)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
	<g transform="translate(100,120)" style="font:normal 5px monospace">
		<rect x="-20" y="-6" height="8" width="40" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Portefeuille</text>
	</g>
	<path class="i3" d="M100 134H81"/>
	<g transform="translate(103,135)" style="font:normal 5px monospace">
		<rect x="-15" y="-6" height="8" width="30" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Courtage</text>
	</g>
</svg>

----

Durée : XX min
<hr/>

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-3 etape-3
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw verify -Dcucumber.filter.tags="@E3"
```

Note: **Scénarios**

- le service permet d'ajouter une action à un portefeuille
- la valeur d'un portefeuille avec une action est la valeur du cours de l'action
- le service permet d'ajouter au portefeuille une action avec une quantité associée
- la valeur d'un portefeuille avec une quantité `n` d'une action est `n×` le cours de l'action
- la valeur d'un portefeuille avec plusieurs actions différentes est la somme des valeurs des actions `/` par les
  quantités
- Le service permet d'ajouter une quantité supplémentaire à une action déjà présente dans un portefeuille

---

## Etape 4

* enrichissement du domaine métier
* enrichissement du bouchon Bourse

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<style>
		path.i4{marker-end:url(#i4);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px}
		path.p4{marker-end:url(#p4);fill:none;stroke:#000}
		path.a4{marker-mid:url(#a4);fill:none;stroke:#000}
	</style>
	<defs>
		<marker id="a4" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p4" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i4" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<path fill="#2ecc71" stroke="#FFF" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z" />
	<g transform="translate(64,119) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4" />
		<text fill="#FFF">ServiceCourtage</text>
	</g>
	<g transform="translate(55,150)">
		<path class="p4" d="m7 -31l-8 6"/>
	</g>
	<g transform="translate(134,82) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4" />
		<text fill="#FFF">ServiceBourse</text>
	</g>
	<g transform="translate(144,50)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19" />
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">Bourse</tspan>
		</text>
		<path class="p4" d="M-7 30L1 24"/>
		<path class="a4" d="m5 12.4v8.4l-.9 -.2"/>
	</g>
	<g transform="translate(134.4,121) rotate(-56.5)" style="font:bold 3px monospace">
		<rect width="43" height="6" x="-21" y="-4" />
		<text fill="#FFF">PortefeuilleRepository</text>
	</g>
	<g transform="translate(138,150)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19" />
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">DB</tspan>
		</text>
		<path class="p4" d="M-3 -27L7-18"/>
		<path class="a4" d="m10.1 -6.5v-7.5l-.9 -.2"/>
	</g>
	<text transform="translate(99,90)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
	<g transform="translate(100,120)" style="font:normal 5px monospace">
		<rect x="-20" y="-6" height="8" width="40" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Portefeuille</text>
	</g>
	<path class="i4" d="M100 134H81"/>
	<g transform="translate(103,135)" style="font:normal 5px monospace">
		<rect x="-15" y="-6" height="8" width="30" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Courtage</text>
	</g>
</svg>

----

Durée : XX min
<hr/>

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-4 etape-4
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw verify -Dcucumber.filter.tags="@E4"
```

Note: **Scénarios**

- La valeur des avoirs gérés par le service est la somme des valeurs des portefeuilles gérés
- Si le cours d'une action change, alors la valeur des portefeuilles ayant cette action dans leur avoir est modifiée en
  conséquence

---

## Etape 5

* Mise à disposition des services métier :<br/>
  au travers d'API REST *... sans modifier le code métier !*

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<style>
		path.i5{marker-end:url(#i5);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px}
		path.p5{marker-end:url(#p5);fill:none;stroke:#000}
		path.a5{marker-mid:url(#a5);fill:none;stroke:#000}
	</style>
	<defs>
		<marker id="a5" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p5" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i5" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<path fill="#2ecc71" stroke="#3498db" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z"/>
	<g transform="translate(64,119) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">ServiceCourtage</text>
	</g>
	<g transform="translate(55,150)">
		<rect fill="#9b59b6" x="-15" y="-14" width="30" height="18"/>
		<text fill="#FFF" font-size="14">API</text>
		<path class="p5" d="m7 -31l-8 6"/>
		<path class="a5" d="m-5 -14v-8l.9 .2"/>
	</g>
	<g transform="translate(134,82) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">ServiceBourse</text>
	</g>
	<g transform="translate(144,50)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">Bourse</tspan>
		</text>
		<path class="p5" d="M-7 30L1 24"/>
		<path class="a5" d="m5 12.4v8.4l-.9 -.2"/>
	</g>
	<g transform="translate(134.4,121) rotate(-56.5)" style="font:bold 3px monospace">
		<rect width="43" height="6" x="-21" y="-4" />
		<text fill="#FFF">PortefeuilleRepository</text>
	</g>
	<g transform="translate(138,150)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">DB</tspan>
		</text>
		<path class="p5" d="M-3 -27L7-18"/>
		<path class="a5" d="m10.1 -6.5v-7.5l-.9 -.2"/>
	</g>
	<text x="100" y="38">Application</text>
	<text transform="translate(99,90)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
	<g transform="translate(100,120)" style="font:normal 5px monospace">
		<rect x="-20" y="-6" height="8" width="40" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Portefeuille</text>
	</g>
	<path class="i5" d="M100 134H81"/>
	<g transform="translate(103,135)" style="font:normal 5px monospace">
		<rect x="-15" y="-6" height="8" width="30" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Courtage</text>
	</g>
</svg>

----

Durée : XX min
<hr/>

Point de départ :

```bash
git stash && git switch -c dev-etape-5 etape-5
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw verify -Dcucumber.filter.tags="@E5"
```

Note: **Scénarios**

- implémenter ce service dans le module `courtage-application-springboot` avec Spring framework

----

### Est fourni :

* module `courtage-application-springboot`
	* avec un 1er service REST : `/courtage/version`
	* UI pour tester les APIs REST :<br/>
	  <smal><a href="http://localhost:8081/swagger-ui.html">http://localhost:8081/swagger-ui.html</a></small>
* module `bourse-mock`
	* → à embarquer dans l'application

----

### Injection de dépendances

Pour caractériser les services dans le domaine métier, création d'une annotation :

```java

@Target({ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DomainService {
}
```

Pour indiquer à Spring de les prendre en compte :

```java
@ComponentScan(
	basePackageClasses = {
		CourtageSpringbootApplication.class,
		DomainService.class
	},
	includeFilters = {@ComponentScan.Filter(
		value = {DomainService.class},
		type = FilterType.ANNOTATION
	)}
)
```

----

### À developer :

* Création d'un portefeuille :<br/>
  `POST /courtage/portefeuilles/{nom}`
* Vérification de l'existance d'un portefeuille :<br/>
  `GET /courtage/portefeuilles/{nom}`

---

## Etape 6

Persistence des données en base

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<style>
		path.i6{marker-end:url(#i6);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px}
		path.p6{marker-end:url(#p6);fill:none;stroke:#000}
		path.a6{marker-mid:url(#a6);fill:none;stroke:#000}
	</style>
	<defs>
		<marker id="a6" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p6" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i6" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<path fill="#2ecc71" stroke="#3498db" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z"/>
	<g transform="translate(64,119) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">ServiceCourtage</text>
	</g>
	<g transform="translate(55,150)">
		<rect fill="#9b59b6" x="-15" y="-14" width="30" height="18"/>
		<text fill="#FFF" font-size="14">API</text>
		<path class="p6" d="m7 -31l-8 6"/>
		<path class="a6" d="m-5 -14v-8l.9 .2"/>
	</g>
	<g transform="translate(134,82) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">ServiceBourse</text>
	</g>
	<g transform="translate(144,50)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
			<tspan x="0">mock</tspan>
			<tspan x="0" dy="10">Bourse</tspan>
		</text>
		<path class="p6" d="M-7 30L1 24"/>
		<path class="a6" d="m5 12.4v8.4l-.9 -.2"/>
	</g>
	<g transform="translate(134.4,121) rotate(-56.5)" style="font:bold 3px monospace">
		<rect width="43" height="6" x="-21" y="-4" />
		<text fill="#FFF">PortefeuilleRepository</text>
	</g>
	<g transform="translate(138,150)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
			<tspan x="0">ORM</tspan>
			<tspan x="0" dy="10">adapter</tspan>
		</text>
		<path class="p6" d="M-3 -27L7-18"/>
		<path class="a6" d="m10.1 -6.5v-7.5l-.9 -.2"/>
		<path fill="none" stroke="#000" d="m0 12.5v14h35"/>
	</g>
	<path transform="translate(185,180)" style="fill:#FFF;stroke-width:3px;stroke:#000" d="m-10.5-7.5c0 6 21 6 21 0v7.5c0 6-21 6-21 0zm0 0c0-6 21-6 21 0v15c0 6-21 6-21 0z"/>
	<text x="100" y="38">Application</text>
	<text transform="translate(99,90)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
	<g transform="translate(100,120)" style="font:normal 5px monospace">
		<rect x="-20" y="-6" height="8" width="40" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Portefeuille</text>
	</g>
	<path class="i6" d="M100 134H81"/>
	<g transform="translate(103,135)" style="font:normal 5px monospace">
		<rect x="-15" y="-6" height="8" width="30" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Courtage</text>
	</g>
</svg>

----

➥ la persistence sera gérée dans une base H2

<hr/>

On en profite pour enrichir les API REST :

* Calcul de la valorisation d'un portefeuille :<br/>
  `GET /courtage/portefeuilles/{nom}/valorisation`
* Achat d'actions dans un portefeuille :<br/>
  `POST /courtage/portefeuilles/{nom}/actions`

----

Durée : XX min
<hr/>

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-6 etape-6
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw verify -Dcucumber.filter.tags="@E6"
```

---

## Etape 7

* Appel HTTP pour la bourse
* Enrichissement des API REST

<svg viewBox="0 0 200 200" width="500" height="500" style="font:bold 16px sans-serif;text-align:center;text-anchor:middle" xmlns="http://www.w3.org/2000/svg" >
	<style>
		path.i7{marker-end:url(#i7);stroke-dasharray:2px 1px;stroke:#000;fill:none;stroke-width:.5px} path.p7{marker-end:url(
		#p7);fill:none;stroke:#000} path.a7{marker-mid:url(#a7);fill:none;stroke:#000}
	</style>
	<defs>
		<marker id="a7" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-linecap:round;stroke-width:2px" d="M8,3.8C8,4.4 6,5 5,5c-2.8,0-5,-2.2-5,-5 0,-2.8 2.2,-5 5,-5 1.4,0 2.6,.6 3.5,1.5"/></marker>
		<marker id="p7" style="overflow:visible" orient="auto"><circle r="3"/></marker>
		<marker id="i7" style="overflow:visible" orient="auto"><path style="fill:none;stroke:#000;stroke-width:1px;stroke-linejoin:round" d="M0 3 5 0 0-3z"/></marker>
	</defs>
	<path fill="#2ecc71" stroke="#3498db" stroke-width="40" d="m 25,100 40,-60h70l40,60-40,60h-70z"/>
	<g transform="translate(64,119) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">ServiceCourtage</text>
	</g>
	<g transform="translate(55,150)">
		<rect fill="#9b59b6" x="-15" y="-14" width="30" height="18"/>
		<text fill="#FFF" font-size="14">API</text>
		<path class="p7" d="m7 -31l-8 6"/>
		<path class="a7" d="m-5 -14v-8l.9 .2"/>
	</g>
	<g transform="translate(134,82) rotate(56.5)" style="font:bold 4px monospace">
		<rect width="42" height="6" x="-21" y="-4"/>
		<text fill="#FFF">ServiceBourse</text>
	</g>
	<g transform="translate(144,50)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
		<tspan x="0">HTTP</tspan>
		<tspan x="0" dy="10">adapter</tspan>
		</text>
		<path class="p7" d="M-7 30L1 24"/>
		<path class="a7" d="m5 12.4v8.4l-.9 -.2"/>
		<path fill="none" stroke="#000" d="m17 3h17v-35"/>
	</g>
	<g transform="translate(177,25)">
		<path fill="#fff" stroke="#000" stroke-width="2" d="M-11 5c-6 0-11-4-11-10 0-5 5-9 10-9 2-4 7-7 12-7 7 0 12 4 14 10 4 0 8 4 8 8 0 5-4 8-9 8h-24"/>
		<text font-size="9" style="text-align:center;text-anchor:middle" y="-17">
			<tspan x="0" dy="8">API</tspan>
			<tspan x="0" dy="8">{bourse}</tspan>
		</text>
	</g>
	<g transform="translate(134.4,121) rotate(-56.5)" style="font:bold 3px monospace">
		<rect width="43" height="6" x="-21" y="-4" />
		<text fill="#FFF">PortefeuilleRepository</text>
	</g>
	<g transform="translate(138,150)">
		<rect fill="#e67e22" x="-17" y="-6.5" width="34" height="19"/>
		<text font-size="8">
			<tspan x="0">ORM</tspan>
			<tspan x="0" dy="10">adapter</tspan>
		</text>
		<path class="p7" d="M-3 -27L7-18"/>
		<path class="a7" d="m10.1 -6.5v-7.5l-.9 -.2"/>
		<path fill="none" stroke="#000" d="m0 12.5v14h35"/>
	</g>
	<path transform="translate(185,180)" style="fill:#FFF;stroke-width:3px;stroke:#000" d="m-10.5-7.5c0 6 21 6 21 0v7.5c0 6-21 6-21 0zm0 0c0-6 21-6 21 0v15c0 6-21 6-21 0z"/>
	<text x="100" y="38">Application</text>
	<text transform="translate(99,90)">
		<tspan x="0">Domaine</tspan>
		<tspan x="0" dy="16">Métier</tspan>
	</text>
	<g transform="translate(100,120)" style="font:normal 5px monospace">
		<rect x="-20" y="-6" height="8" width="40" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Portefeuille</text>
	</g>
	<path class="i7" d="M100 134H81"/>
	<g transform="translate(103,135)" style="font:normal 5px monospace">
		<rect x="-15" y="-6" height="8" width="30" fill="#AFA" stroke="#000" stroke-width="0.3" />
		<text>Courtage</text>
	</g>
</svg>

----

* Restitution des positions d'un portefeuille :<br/>
  `GET /courtage/portefeuilles/{nom}/positions`
* Calcul de la valorisation de l'ensemble des portefeuilles gérés :<br/>
  `GET /courtage/avoirs`

----

#### Service externe : cours de bourse

* Requête :<br/>
  <code>GET https:// … /finance/quote/{action}</code>

* Exemple de réponse pour<br/>
  `https:// … /finance/quote/CA` :

```json
{
	"symbol": "CA",
	"regularMarketPrice": 1.795
}
```

➥ Service simulé avec Wiremock

----

Durée : XX min
<hr/>

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-7 etape-7
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw verify -Dcucumber.filter.tags="@E7"
```

---

## Etape 8

* Validation de la cohérence fonctionnelle des objets du domaine
* Mise en place des transactions

---

## Etape 9

* Gestion des droits
* CQRS

---

## Ressources complémentaires

- [Pérennisez votre métier avec l’architecture hexagonale <small>(Publicis Sapiens - ex: Xebia)</small>](https://blog.xebia.fr/2016/03/16/perennisez-votre-metier-avec-larchitecture-hexagonale/)
- [Architecture Hexagonale : trois principes et un exemple d’implémentation <small>(Octo)</small>](https://blog.octo.com/architecture-hexagonale-trois-principes-et-un-exemple-dimplementation/)
- [Architecture hexagonale pour les nuls <small>(Y. Chéné - Devoxx FR 2018)</small>](https://www.youtube.com/watch?v=Hi5aDfRe-aE)
- [Architecture hexagonale <small>(Eleven Labs)</small>](https://blog.eleven-labs.com/fr/architecture-hexagonale/)

Note: **Autres ressources**

- [DDD, Hexagonal, Onion, Clean, CQRS, … How I put it all together](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/)
- [WikiBooks - Java Persistence/ElementCollection - Example of a ElementCollection relationship to a basic value XML](https://en.wikibooks.org/wiki/Java_Persistence/ElementCollection#Example_of_a_ElementCollection_relationship_to_a_basic_value_XML)

