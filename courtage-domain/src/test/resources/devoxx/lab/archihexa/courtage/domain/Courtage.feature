#language: fr
#noinspection NonAsciiCharacters

Fonctionnalité: Gestion du portefeuille

	@E1 @E2 @E3 @E4 @E5 @E6 @E7 @E8
	Scénario: Récupération de l'identifiant d'un portefeuille
		Quand on demande au service de courtage la création du portefeuille "FOO-1"
		Alors l'id du portefeuille créé doit être "FOO-1"

	@E1 @E2 @E3 @E4 @E5 @E6 @E7 @E8
	Scénario: Récupération de l'identifiant d'un autre portefeuille
		Quand on demande au service de courtage la création du portefeuille "BAR-2"
		Alors l'id du portefeuille créé doit être "BAR-2"

	@E1 @E2 @E3 @E4 @E5 @E6 @E7 @E8
	Scénario: Ajout d'un portefeuille et vérification de la présence du portefeuille
		Quand on demande au service de courtage la création du portefeuille "FOO-3"
		Alors le portefeuille "FOO-3" est géré par le service de courtage

	@E1 @E2 @E3 @E4 @E5 @E6 @E7 @E8
	Scénario: Création de 2 portefeuilles et vérification de la présence de ces 2 portefeuilles
		Quand on demande au service de courtage la création du portefeuille "FOO-4"
		Et qu'on demande au service de courtage la création du portefeuille "BAR-4"
		Alors le portefeuille "FOO-4" est géré par le service de courtage
		Et le portefeuille "BAR-4" est géré par le service de courtage

	@E2 @E3 @E4 @E5 @E6 @E7 @E8
	Scénario: Ajout d'un portefeuille et vérification de la non présence du portefeuille
		Quand on demande au service de courtage la création du portefeuille "FOO-6"
		Alors le portefeuille "INCONNU" n'est pas géré par le service de courtage

	@E2 @E3 @E4 @E5 @E6 @E7 @E8
	Scénario: Un portefeuille doit être unique pour un service de courtage
		Quand on demande au service de courtage la création du portefeuille "FOO-7"
		Et qu'on demande au service de courtage la création du portefeuille "FOO-7"
		Alors une exception est levée : Portefeuille déjà géré

	@E2 @E3 @E4 @E6 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille vide
		Quand on demande au service de courtage la création du portefeuille "FOO-8"
		Et qu'on demande le calcul de la valeur du portefeuille "FOO-8"
		Alors la valeur du portefeuille est 0,0

	@E2 @E3 @E4 @E6 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille non géré
		Quand on demande au service de courtage la création du portefeuille "FOO-9"
		Et qu'on demande le calcul de la valeur du portefeuille "INCONNU"
		Alors une exception est levée : Portefeuille non géré

	@E3 @E4 @E7 @E8
	Scénario: Récupération par le service de bourse de la valeur d'une action
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,1    |
		Quand on demande au service de bourse la valeur de l'action "TEST"
		Alors la valeur récupérée pour l'action est 0,1

	@E3 @E4 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille avec une action
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,1    |
		Quand on demande au service de courtage la création du portefeuille "FOO-10"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| FOO-10       | TEST   | 1      |
		Et qu'on demande le calcul de la valeur du portefeuille "FOO-10"
		Alors la valeur du portefeuille est 0,1

	@E3 @E4 @E7 @E8
	Scénario: Ajout d'une action à un portefeuille inexistant
		Quand on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| INCONNU      | TEST   | 1      |
		Alors une exception est levée : Portefeuille non géré

	@E3 @E4 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille avec deux action
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,1    |
		Quand on demande au service de courtage la création du portefeuille "FOO-11"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| FOO-11       | TEST   | 2      |
		Et qu'on demande le calcul de la valeur du portefeuille "FOO-11"
		Alors la valeur du portefeuille est 0,2

	@E3 @E4 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille avec deux actions au cours de 0,2
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,2    |
		Quand on demande au service de courtage la création du portefeuille "FOO-12"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| FOO-12       | TEST   | 2      |
		Et qu'on demande le calcul de la valeur du portefeuille "FOO-12"
		Alors la valeur du portefeuille est 0,4

	@E3 @E4 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille avec deux actions différentes
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| CA     | 0,2    |
			| SG     | 0,7    |
		Quand on demande au service de courtage la création du portefeuille "FOO-13"
		Et qu'on demande au service de courtage d'ajouter les actions suivantes :
			| Portefeuille | Action | Nombre |
			| FOO-13       | CA     | 2      |
			| FOO-13       | SG     | 4      |
		Et qu'on demande le calcul de la valeur du portefeuille "FOO-13"
		Alors la valeur du portefeuille est 3,2

	@E3 @E4 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille en ajoutant 2 actions au même titre
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| CA     | 0,2    |
		Quand on demande au service de courtage la création du portefeuille "FOO-14"
		Et qu'on demande au service de courtage d'ajouter les actions suivantes :
			| Portefeuille | Action | Nombre |
			| FOO-14       | CA     | 2      |
			| FOO-14       | CA     | 4      |
		Et qu'on demande le calcul de la valeur du portefeuille "FOO-14"
		Alors la valeur du portefeuille est 1,2

	@E4 @E7 @E8
	Scénario: Calcul de la valeur de plusieurs portefeuilles non vides
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| CA     | 0,2    |
			| SG     | 0,5    |
		Quand on demande au service de courtage la création du portefeuille "myCard"
		Et qu'on demande au service de courtage la création du portefeuille "eWallet"
		Et qu'on demande au service de courtage d'ajouter les actions suivantes :
			| Portefeuille | Action | Nombre |
			| myCard       | CA     | 2      |
			| myCard       | SG     | 4      |
			| eWallet      | CA     | 6      |
			| eWallet      | SG     | 5      |
		Et qu'on demande au service de courtage le calcul de la valeur de tous les portefeuilles
		Alors la valeur pour l'ensemble des portefeuilles est 10,7

	@E4 @E7 @E8
	Scénario: Calcul de la valeur des actions sans portefeuille
		Quand on demande au service de courtage le calcul de la valeur de tous les portefeuilles
		Alors la valeur pour l'ensemble des portefeuilles est 10,7

	@E4 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille en ajoutant 2 actions au même titre
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| CA     | 0,2    |
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| TOTO         | CA     | 2      |
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 0,4
		Quand les cours de bourse sont :
			| Action | Valeur |
			| CA     | 0,4    |
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 0,8

	@E4 @E7 @E8
	Scénario: Calcul de la valeur d'un portefeuille inconnu
		Quand on demande le calcul de la valeur du portefeuille "INCONNU"
		Alors une exception est levée : Portefeuille non géré

	@E4 @E7 @E8
	Scénario: La valeur totale du portefeuille doit être correctement recalculée si le cours des actions changent
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| CA     | 0,2    |
			| SG     | 0,5    |
		Quand on demande au service de courtage la création du portefeuille "myCard"
		Et qu'on demande au service de courtage la création du portefeuille "eWallet"
		Et qu'on demande au service de courtage d'ajouter les actions suivantes :
			| Portefeuille | Action | Nombre |
			| myCard       | CA     | 2      |
			| myCard       | SG     | 4      |
			| eWallet      | CA     | 6      |
			| eWallet      | SG     | 5      |
		Et qu'on demande au service de courtage le calcul de la valeur de tous les portefeuilles
		Alors la valeur pour l'ensemble des portefeuilles est 17,2
		Mais si les cours de bourse deviennent :
			| Action | Valeur |
			| CA     | 1,1    |
			| SG     | 0,7    |
		Et qu'on demande au service de courtage le calcul de la valeur de tous les portefeuilles
		Alors la valeur pour l'ensemble des portefeuilles est 45,0
