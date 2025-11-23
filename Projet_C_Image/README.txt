Compilation :

Pour compiler le programme, tapez la commande make dans le terminal. Ensuite, exécutez le programme en tapant ./main.


Jeu :

Lors du lancement du programme, des obstacles de tailles et de positions aléatoires sont générés. Un serpent, constitué initialement d'une particule, se déplace automatiquement et évite les obstacles. Un bouton nommé "Déplacement" permet de contrôler le serpent à l'aide de la souris lorsqu'il est activé. Par ailleurs, plusieurs particules se déplacent dans l'espace en rebondissant sur les obstacles. Au début du jeu, une particule est mangée par le serpent, ce qui entraîne une augmentation de sa taille.

Main :

Deux variables importantes peuvent être configurées :

-NB_OBSTACLES : permet de définir le nombre d'obstacles affichés.

-NB_PARTICULES : détermine le nombre de particules qui se déplacent sur l'écran.

Difficultés :

Nous avons rencontré certaines difficultés, notamment :

-Génération des obstacles : Garantir qu'ils ne se chevauchent pas a été complexe, mais nous avons finalement réussi à implémenter une solution fonctionnelle.
-Fonction part_bypass_circle : De nombreux problèmes sont survenus lors de son développement, mais nous avons réussi à la finaliser.
-Fonction hunter_prey : Nous avons tenté de la mettre en place, mais n'avons pas réussi à obtenir un résultat fonctionnel.