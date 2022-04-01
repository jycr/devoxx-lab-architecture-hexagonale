# Lab - Architecture Hexagonale

---

## Préparation

**☛ <u>à faire impérativement AVANT le Lab !!!</u> ☚**

Pour améliorer au maximum votre confort dans le déroulement de ce lab :<br/>
effectuer les actions suivantes **avant** le début du lab<br/>
... de préférence chez vous<br/>
(avec une bonne connexion internet)

```bash
git clone https://github.com/javathought/lab-archi-hexa
cd devoxx-lab-architecture-hexagonale
git switch solution && ./mvnw dependency:go-offline
git switch -
```

----

## Prérequis pour ce lab

* Connaitre le langage Java
* Avoir des bases dans l'usage de Git, Maven, Cucumber
* Avoir un JDK (Java >= 11)
* Avoir un IDE

----

## Envie de se familiariser avec les concepts avant ?

- [Pérennisez votre métier avec l’architecture hexagonale <small>(Publicis Sapiens - ex: Xebia)</small>](https://blog.xebia.fr/2016/03/16/perennisez-votre-metier-avec-larchitecture-hexagonale/)
- [Architecture Hexagonale : trois principes et un exemple d’implémentation <small>(Octo)</small>](https://blog.octo.com/architecture-hexagonale-trois-principes-et-un-exemple-dimplementation/)
- [Architecture hexagonale pour les nuls <small>(Y. Chéné - Devoxx FR 2018)</small>](https://www.youtube.com/watch?v=Hi5aDfRe-aE)
- [Architecture hexagonale <small>(Eleven Labs)</small>](https://blog.eleven-labs.com/fr/architecture-hexagonale/)

Note: **Autres ressources**

- [DDD, Hexagonal, Onion, Clean, CQRS, … How I put it all together](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/)
- [WikiBooks - Java Persistence/ElementCollection - Example of a ElementCollection relationship to a basic value XML](https://en.wikibooks.org/wiki/Java_Persistence/ElementCollection#Example_of_a_ElementCollection_relationship_to_a_basic_value_XML)

---

## Vos "accompagnateurs" pour ce lab ...

<table>
<tr>
	<th scope="col" style="text-align:center">Pascal</th>
	<th scope="col" style="text-align:center">JY</th>
</tr>
<tr>
	<td><img src="https://s3.amazonaws.com/garmin-connect-prod/profile_images/1b691e8a-88f0-4237-92d9-5def9ae7e8e4-1363476.png" alt="" width="300"/></td>
	<td><img src="https://lh3.googleusercontent.com/a-/AOh14GhY9dkmjmpi-s_E41iik_7C2_gCQPnm_nSzop9KSg=s576-p-rw-no" alt="" width="300"/></td>
</tr>
</table>

---

## Objectifs

Développer un petit système de courtage boursier

→ en s'appuyant sur une<br/>
**architecture applicative hexagonale**

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

```plantuml
@startuml
allowmixing
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members

namespace "Classique : en couche" as classique {
	namespace API {
		class CourtageResource
	}
	namespace Domaine {
		class Courtage
	}
	namespace Persistance {
		class PortefeuilleRepositorySpringDataImpl
	}
	classique.Domaine.Courtage ..>  classique.Persistance.PortefeuilleRepositorySpringDataImpl : utilise
	classique.API.CourtageResource ..> classique.Domaine.Courtage : utilise
}
package "Centrée sur le métier" as hex {
	package API {
		class CourtageResource
	}
	package Domaine <<Hexagon>> {
		class Courtage
		interface PortefeuilleRepository
	}
	package Persistance {
		class PortefeuilleRepositorySpringDataImpl
	}
	CourtageResource ..> Courtage : utilise
	Courtage ..>  PortefeuilleRepository : utilise
	PortefeuilleRepository <|.. PortefeuilleRepositorySpringDataImpl
}
@enduml
```

Note: Les principes de "Clean Architecture" → Le domaine métier : au centre

---

## Organisation du lab

... en plusieurs étapes jusqu'à la solution finale :

```plantuml
@startuml
!include <logos/spring>
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

package API {
	annotation "<$spring,scale=0.4> RestController" as RestController
	class CourtageResource
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	annotation DomainService
	class Courtage
	class Portefeuille {
		String nom
	}
	class Achat {
		String action
		int nombre
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
			void ajouteAction(String nomPortefeuille, Achat achat)
			BigDecimal calculerValeurEnsemblePortefeuilles()
		}
	}
	note bottom of ServiceCourtage
		limite explicitement
		ce qui est disponible
	end note
	package "Port secondaire" {
		interface PortefeuilleRepository
    	interface ServiceBourse
    }
}
package Adapters {
	package Persistance {
		class PortefeuilleRepositorySpringDataImpl
		interface PortefeuilleSpringDataCrudRepository
		interface "<$spring,scale=.4> CrudRepository" as CrudRepository<Portefeuille, String>
	}
	package Api {
		class ServiceBourseHttpAdapter {
			<$spring,scale=.4> RestTemplate restTemplate
		}
	}
}

RestController <.l. CourtageResource : est annotée
CourtageResource ..> ServiceCourtage : utilise
ServiceCourtage <|.. Courtage
DomainService <.l. Courtage : est annotée
Courtage ..>  PortefeuilleRepository : utilise
Courtage ..>  ServiceBourse : utilise
ServiceBourse <|.. ServiceBourseHttpAdapter
PortefeuilleRepository <|.. PortefeuilleRepositorySpringDataImpl
PortefeuilleRepositorySpringDataImpl -l-* PortefeuilleSpringDataCrudRepository
PortefeuilleSpringDataCrudRepository -l-|> CrudRepository
@enduml
```

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

## Étape 1

Mise en place du _métier de l'application_<br/>
**sans intégrer de framework technique**

> *Un service de courtage gére une liste de portefeuille*

* Code à créer dans le module :<br/>
  `courtage-domain`

----

```plantuml
@startuml
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nDomain Step\nDefinitions" as stepDefs #fab1a0 {
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class Courtage
	class Portefeuille {
		String nom
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
		}
	}
}

ServiceCourtage <|.. Courtage
stepDefs ..> ServiceCourtage : utilise
@enduml
```

----

Point de départ :

```bash
git stash && git switch -c dev-etape-1 etape-1
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -pl courtage-domain -am -Dcucumber.filter.tags="@E1"
```

---

## Étape 2

* Mise en oeuvre du *PortefeuilleRepository*
* Création d'un bouchon<br/>
  <small>(une implémentation basée sur `HashMap`, dans les packages de test, suffira)</small>

----

```plantuml
@startuml
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nDomain Step\nDefinitions" as stepDefs #fab1a0 {
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	class Courtage
	class Portefeuille {
		String nom
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
		}
	}
	package "Port secondaire" {
		interface PortefeuilleRepository
    }
}
class PortefeuilleRepositoryMock #fab1a0

ServiceCourtage <|.. Courtage
Courtage ..>  PortefeuilleRepository : utilise
PortefeuilleRepository <|.. PortefeuilleRepositoryMock
stepDefs ..> ServiceCourtage : utilise
@enduml
```

----

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-2 etape-2
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -pl courtage-domain -am -Dcucumber.filter.tags="@E2"
```

---

## Étape 3

* Ajout de la gestion des actions au domaine métier
* Mise en place d'un bouchon pour la bourse

----

```plantuml
@startuml
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nDomain Step\nDefinitions" as stepDefs #fab1a0 {
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	class Courtage
	class Portefeuille {
		String nom
	}
	class Achat {
		String action
		int nombre
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
			void ajouteAction(String nomPortefeuille, Achat achat)
		}
	}
	package "Port secondaire" {
		interface PortefeuilleRepository
    	interface ServiceBourse
    }
}
class PortefeuilleRepositoryMock #fab1a0
class ServiceBourseMock #fab1a0

ServiceCourtage <|.. Courtage
Courtage ..>  PortefeuilleRepository : utilise
Courtage ..>  ServiceBourse : utilise
PortefeuilleRepository <|.. PortefeuilleRepositoryMock
ServiceBourse <|.. ServiceBourseMock
stepDefs ..> ServiceCourtage : utilise
@enduml
```

----

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-3 etape-3
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -pl courtage-domain -am -Dcucumber.filter.tags="@E3"
```

---

## Étape 4

* enrichissement du domaine métier
* enrichissement du bouchon Bourse

----

```plantuml
@startuml
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nDomain Step\nDefinitions" as stepDefs #fab1a0 {
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	class Courtage
	class Portefeuille {
		String nom
	}
	class Achat {
		String action
		int nombre
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
			void ajouteAction(String nomPortefeuille, Achat achat)
			BigDecimal calculerValeurEnsemblePortefeuilles()
		}
	}
	package "Port secondaire" {
		interface PortefeuilleRepository
    	interface ServiceBourse
    }
}
class PortefeuilleRepositoryMock #fab1a0
class ServiceBourseMock #fab1a0

ServiceCourtage <|.. Courtage
Courtage ..>  PortefeuilleRepository : utilise
Courtage ..>  ServiceBourse : utilise
PortefeuilleRepository <|.. PortefeuilleRepositoryMock
ServiceBourse <|.. ServiceBourseMock
stepDefs ..> ServiceCourtage : utilise
@enduml
```

----

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-4 etape-4
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -pl courtage-domain -am -Dcucumber.filter.tags="@E4"
```

---

## Pause

---

## Étape 5

services métier via API REST<br/>
**... sans modifier le code métier**

* module `courtage-application-springboot`
	* avec un 1er service REST :<br/>
	  `/courtage/version`
	* UI pour tester les APIs REST :<br/>
	  <smal><a href="http://localhost:8081/swagger-ui.html">http://localhost:8081/swagger-ui.html</a></small>

----

### À developer :

* Création d'un portefeuille :<br/>
  `POST /courtage/portefeuilles/{nom}`
* Vérification de l'existance d'un portefeuille :<br/>
  `GET /courtage/portefeuilles/{nom}`

----

```plantuml
@startuml
!include <logos/spring>
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nApplication Step\nDefinitions" as stepDefs #fab1a0 {
}
package API {
	annotation "<$spring,scale=0.4> RestController" as RestController
	class CourtageResource
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	annotation DomainService
	class Courtage
	class Portefeuille {
		String nom
	}
	class Achat {
		String action
		int nombre
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
			void ajouteAction(String nomPortefeuille, Achat achat)
			BigDecimal calculerValeurEnsemblePortefeuilles()
		}
	}
	package "Port secondaire" {
		interface PortefeuilleRepository
    	interface ServiceBourse
    }
}
package Adapters {
	package Persistance {
		class PortefeuilleRepositoryMock
	}
	package Api {
		class ServiceBourseMock
	}
}

RestController <.l. CourtageResource : est annotée
CourtageResource ..> ServiceCourtage : utilise
ServiceCourtage <|.. Courtage
DomainService <.l. Courtage : est annotée
Courtage ..>  PortefeuilleRepository : utilise
Courtage ..>  ServiceBourse : utilise
PortefeuilleRepository <|.. PortefeuilleRepositoryMock
ServiceBourse <|.. ServiceBourseMock
CourtageResource <-[bold]l- stepDefs : via RestAssured
@enduml
```

----

### Injection de dépendances

Pour caractériser les services du domaine métier, création d'une annotation :

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

Point de départ :

```bash
git stash && git switch -c dev-etape-5 etape-5
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -Dcucumber.filter.tags="@E5"
```

---

## Étape 6

* Persistence des données en base<br/>
  ➥ gérée dans une base H2
* Calcul de la valorisation d'un portefeuille :<br/>
  `GET /courtage/portefeuilles/{nom}/valorisation`
* Achat d'actions dans un portefeuille :<br/>
  `POST /courtage/portefeuilles/{nom}/actions`

----

```plantuml
@startuml
!include <logos/spring>
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nApplication Step\nDefinitions" as stepDefs #fab1a0 {
}
package API {
	annotation "<$spring,scale=0.4> RestController" as RestController
	class CourtageResource
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	annotation DomainService
	class Courtage
	class Portefeuille {
		String nom
	}
	class Achat {
		String action
		int nombre
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
			void ajouteAction(String nomPortefeuille, Achat achat)
			BigDecimal calculerValeurEnsemblePortefeuilles()
		}
	}
	package "Port secondaire" {
		interface PortefeuilleRepository
    	interface ServiceBourse
    }
}
package Adapters {
	package Persistance {
		class PortefeuilleRepositorySpringDataImpl
		interface PortefeuilleSpringDataCrudRepository
		interface "<$spring,scale=.4> CrudRepository" as CrudRepository<Portefeuille, String>
	}
	package Api {
		class ServiceBourseMock
	}
}

RestController <.l. CourtageResource : est annotée
CourtageResource ..> ServiceCourtage : utilise
ServiceCourtage <|.. Courtage
DomainService <.l. Courtage : est annotée
Courtage ..>  PortefeuilleRepository : utilise
Courtage ..>  ServiceBourse : utilise
ServiceBourse <|.. ServiceBourseMock
PortefeuilleRepository <|.. PortefeuilleRepositorySpringDataImpl
PortefeuilleRepositorySpringDataImpl -l-* PortefeuilleSpringDataCrudRepository
PortefeuilleSpringDataCrudRepository -l-|> CrudRepository
CourtageResource <-[bold]l- stepDefs : via RestAssured
@enduml
```

----

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-6 etape-6
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -Dcucumber.filter.tags="@E6"
```

---

## Étape 7

* Appel HTTP pour la bourse
* Enrichissement des API REST :
	* Restitution des positions d'un portefeuille :<br/>
	  `GET /courtage/portefeuilles/{nom}/positions`
	* Calcul de valorisation des portefeuilles gérés :<br/>
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

```plantuml
@startuml
!include <logos/spring>
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nApplication Step\nDefinitions" as stepDefs #fab1a0 {
}
package API {
	annotation "<$spring,scale=0.4> RestController" as RestController
	class CourtageResource
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	annotation DomainService
	class Courtage
	class Portefeuille {
		String nom
	}
	class Achat {
		String action
		int nombre
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
			void ajouteAction(String nomPortefeuille, Achat achat)
			BigDecimal calculerValeurEnsemblePortefeuilles()
		}
	}
	package "Port secondaire" {
		interface PortefeuilleRepository
    	interface ServiceBourse
    }
}
package Adapters {
	package Persistance {
		class PortefeuilleRepositorySpringDataImpl
		interface PortefeuilleSpringDataCrudRepository
		interface "<$spring,scale=.4> CrudRepository" as CrudRepository<Portefeuille, String>
	}
	package Api {
		class ServiceBourseHttpAdapter {
			<$spring,scale=.4> RestTemplate restTemplate
		}
	}
}
rectangle BourseMock #fab1a0 {
}
note left of BourseMock
  Mise en oeuvre
  via Wiremock
end note

RestController <.l. CourtageResource : est annotée
CourtageResource ..> ServiceCourtage : utilise
ServiceCourtage <|.. Courtage
DomainService <.l. Courtage : est annotée
Courtage ..>  PortefeuilleRepository : utilise
Courtage ..>  ServiceBourse : utilise
ServiceBourse <|.. ServiceBourseHttpAdapter
PortefeuilleRepository <|.. PortefeuilleRepositorySpringDataImpl
PortefeuilleRepositorySpringDataImpl -l-* PortefeuilleSpringDataCrudRepository
PortefeuilleSpringDataCrudRepository -l-|> CrudRepository
CourtageResource <-[bold]l- stepDefs : via RestAssured
BourseMock <-[bold]d- ServiceBourseHttpAdapter
@enduml
```

----

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-7 etape-7
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -Dcucumber.filter.tags="@E7"
```

---

## Étape 8

* Validation de la cohérence fonctionnelle des objets du domaine

----

```plantuml
@startuml
!include <logos/spring>
skinparam packageStyle rectangle
skinparam package.backgroundColor white
hide empty members
left to right direction

rectangle "Cucumber\nApplication Step\nDefinitions" as stepDefs #fab1a0 {
}
package API {
	annotation "<$spring,scale=0.4> RestController" as RestController
	class CourtageResource
}
package Domaine <<Hexagon>> {
	class PortefeuilleDejaExistantException
	class PortefeuilleNonGereException
	annotation DomainService
	class Courtage
	class Portefeuille {
		String nom
	}
	class Achat {
		String action
		int nombre
	}
	package "Port primaire" {
		interface ServiceCourtage {
			Portefeuille creerPortefeuille(String nomPortefeuille)
			boolean gere(String nomPortefeuille)
			void ajouteAction(String nomPortefeuille, Achat achat)
			BigDecimal calculerValeurEnsemblePortefeuilles()
		}
	}
	package "Port secondaire" {
		interface PortefeuilleRepository
    	interface ServiceBourse
    }
}
package Adapters {
	package Persistance {
		class PortefeuilleRepositorySpringDataImpl
		interface PortefeuilleSpringDataCrudRepository
		interface "<$spring,scale=.4> CrudRepository" as CrudRepository<Portefeuille, String>
	}
	package Api {
		class ServiceBourseHttpAdapter {
			<$spring,scale=.4> RestTemplate restTemplate
		}
	}
}
rectangle BourseMock #fab1a0 {
}
note left of BourseMock
  Mise en oeuvre
  via Wiremock
end note

RestController <.l. CourtageResource : est annotée
CourtageResource ..> ServiceCourtage : utilise
ServiceCourtage <|.. Courtage
DomainService <.l. Courtage : est annotée
Courtage ..>  PortefeuilleRepository : utilise
Courtage ..>  ServiceBourse : utilise
ServiceBourse <|.. ServiceBourseHttpAdapter
PortefeuilleRepository <|.. PortefeuilleRepositorySpringDataImpl
PortefeuilleRepositorySpringDataImpl -l-* PortefeuilleSpringDataCrudRepository
PortefeuilleSpringDataCrudRepository -l-|> CrudRepository
CourtageResource <-[bold]l- stepDefs : via RestAssured
BourseMock <-[bold]d- ServiceBourseHttpAdapter
@enduml
```

----

Point de départ (facultatif) :

```bash
git stash && git switch -c dev-etape-8 etape-8
```

Point d'arrivée :

```bash
# La commande suivante doit terminer en 'BUILD SUCCESS'
./mvnw clean verify -Dcucumber.filter.tags="@E8"
```

---

## Conclusion

Nous avons vu :

* mettre en œuvre une "clean architecture" (suivant les principes de l'architecture hexagonale)
* la gestion de la persistance avec Spring
* la validation de la cohérence fonctionnelle des objets du domaine
