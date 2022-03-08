#language: fr
#noinspection NonAsciiCharacters

Fonctionnalité: Validation des données

	Scénario: code action trop long
		Quand on demande au service de courtage la création du portefeuille "FOO-10"
		Et qu'on demande au service de courtage d'ajouter l'action suivante :
			| Portefeuille | Action | Nombre |
			| FOO-10       | TESTYZ | 1      |
		Alors une exception est levée : Donnée erronée avec le message 'action doit être composé de 2 à 5 caractères latins'

