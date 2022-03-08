#language: fr

Fonctionnalité: Gestion du portefeuille

	@E1 @E2 @E3 @E4 @E5 @E6 @E7
	Scénario: Récupération de l'identifiant d'un portefeuille
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Alors l'id du portefeuille créé doit être "TOTO"

	@E1 @E2 @E3 @E4 @E5 @E6 @E7
	Scénario: Récupération de l'identifiant d'un autre portefeuille
		Quand on demande au service de courtage la création du portefeuille "TUTU"
		Alors l'id du portefeuille créé doit être "TUTU"

	@E1 @E2 @E3 @E4 @E5 @E6 @E7
	Scénario: Ajout d'un portefeuille et vérification de la présence du portefeuille
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Alors le portefeuille "TOTO" est géré par le service de courtage

	@E1 @E2 @E3 @E4 @E5 @E6 @E7
	Scénario: Création de 2 portefeuilles et vérification de la présence de ces 2 portefeuilles
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage la création du portefeuille "TITI"
		Alors le portefeuille "TOTO" est géré par le service de courtage
		Et le portefeuille "TITI" est géré par le service de courtage

	@E2 @E3 @E4 @E5 @E6 @E7
	Scénario: Ajout d'un portefeuille et vérification de la non présence du portefeuille
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Alors le portefeuille "TITI" n'est pas géré par le service de courtage

	@E2 @E3 @E4 @E5 @E6 @E7
	Scénario: Un portefeuille doit être unique pour un service de courtage
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage la création du portefeuille "TOTO"
		Alors une exception est levée : Portefeuille déjà géré

	@E2 @E3 @E4 @E6 @E7
	Scénario: Calcul de la valeur d'un portefeuille vide
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 0,0

	@E2 @E3 @E4 @E6 @E7
	Scénario: Calcul de la valeur d'un portefeuille non géré
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande le calcul de la valeur du portefeuille "TITI"
		Alors une exception est levée : Portefeuille non géré

	@E3 @E4 @E7
	Scénario: Récupération par le service de bourse de la valeur d'une action
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,1    |
		Quand on demande au service de bourse la valeur de l'action "TEST"
		Alors la valeur récupérée pour l'action est 0,1

	@E3 @E4 @E7
	Scénario: Calcul de la valeur d'un portefeuille avec une action
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,1    |
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| TOTO         | TEST   | 1      |
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 0,1

	@E3 @E4 @E7
	Scénario: Ajout d'une action à un portefeuille inexistant
		Quand on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| INCONNU      | TEST   | 1      |
		Alors une exception est levée : Portefeuille non géré

	@E3 @E4 @E7
	Scénario: Calcul de la valeur d'un portefeuille avec deux action
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,1    |
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| TOTO         | TEST   | 2      |
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 0,2

	@E3 @E4 @E7
	Scénario: Calcul de la valeur d'un portefeuille avec deux actions au cours de 0,2
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| TEST   | 0,2    |
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| TOTO         | TEST   | 2      |
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 0,4

	@E3 @E4 @E7
	Scénario: Calcul de la valeur d'un portefeuille avec deux actions différentes
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| CA     | 0,2    |
			| SG     | 0,7    |
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage d'ajouter les actions suivantes :
			| Portefeuille | Action | Nombre |
			| TOTO         | CA     | 2      |
			| TOTO         | SG     | 4      |
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 3,2

	@E3 @E4 @E7
	Scénario: Calcul de la valeur d'un portefeuille en ajoutant 2 actions au même titre
		Soit les cours de bourse suivants :
			| Action | Valeur |
			| CA     | 0,2    |
		Quand on demande au service de courtage la création du portefeuille "TOTO"
		Et qu'on demande au service de courtage d'ajouter les actions suivantes :
			| Portefeuille | Action | Nombre |
			| TOTO         | CA     | 2      |
			| TOTO         | CA     | 4      |
		Et qu'on demande le calcul de la valeur du portefeuille "TOTO"
		Alors la valeur du portefeuille est 1,2

	@E4 @E7
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
		Alors la valeur pour l'ensemble des portefeuilles est 6,1

	@E4 @E7
	Scénario: Calcul de la valeur des actions sans portefeuille
		Quand on demande au service de courtage le calcul de la valeur de tous les portefeuilles
		Alors la valeur pour l'ensemble des portefeuilles est 0,0

	@E4 @E7
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

	@E4 @E7
	Scénario: Calcul de la valeur d'un portefeuille inconnu
		Quand on demande le calcul de la valeur du portefeuille "INCONNU"
		Alors une exception est levée : Portefeuille non géré
