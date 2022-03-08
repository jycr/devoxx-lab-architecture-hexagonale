#language: fr
#noinspection NonAsciiCharacters

Fonctionnalité: Validation des données

	@E8
	Scénario: Achat d'un code action trop court
		Soit l'achat
			| action | nombre |
			| C      | 4      |
		Alors l'achat est invalide avec l'erreur
			| propriété | message                                      |
			| action    | doit être composé de 2 à 5 caractères latins |

	@E8
	Scénario: Achat d'un code action trop long
		Soit l'achat
			| action | nombre |
			| CAWXYZ | 4      |
		Alors l'achat est invalide avec l'erreur
			| propriété | message                                      |
			| action    | doit être composé de 2 à 5 caractères latins |

	@E8
	Scénario: Achat d'un code action avec caractère erronné
		Soit l'achat
			| action | nombre |
			| Cβ     | 4      |
		Alors l'achat est invalide avec l'erreur
			| propriété | message                                      |
			| action    | doit être composé de 2 à 5 caractères latins |

	@E8
	Scénario: Achat d'un nombre d'action égal à zéro
		Soit l'achat
			| action | nombre |
			| CA     | 0      |
		Alors l'achat est invalide avec l'erreur
			| propriété | message                         |
			| nombre    | doit être supérieur ou égal à 1 |

	@E8
	Scénario: Achat d'un nombre d'action négatif
		Soit l'achat
			| action | nombre |
			| CA     | -1     |
		Alors l'achat est invalide avec l'erreur
			| propriété | message                         |
			| nombre    | doit être supérieur ou égal à 1 |
