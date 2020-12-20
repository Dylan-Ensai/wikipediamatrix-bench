# Rapport

## Statistiques

L'extracteur a été testé sur 336 URLs. Il y a 31 pages qui sont introuvables (erreur 404). 

Au sein des 305 pages valides, 1010 ont été extraits. En moyenne un tableau a 8 colonnes, 23 lignes et 184 cellules.
Les tableaux ont entre 2 et 37 colonnes, 2 et 464 lignes et 4 et 5359 cellules. 

Un tableau, généré automatiquement, est disponible à la racine du projet au format CSV ou vous trouverez plus de détail. Dans ce tableau on retrouve les min, max, moyennes et écarts-types ainsi que les différents totaux. 


Concernant les noms des colonnes, les trois noms les plus courants sont : "License"", "" (pas de nom) et "Name"; avec des occurrences respectives de 149, 146 et 129.

La chaine vide en deuxième position peut paraître étrange de première vu.  Cependant, il y a souvent aucun nom pour la première colonne des tableaux, tous les tableaux à doubles entrées par exemple.

Encore une fois un tableau est généré automatiquement avec les occurrences des différents noms de colonnes présents au sein de tous les tableaux extraits.

## Qualité est faiblesse de votre extracteur 

Je pense que mon extracteur a de bonne performance dans le sens qu'il extrait les tableaux en restant fidèle aux tableaux originaux dans la grande majorité des cas. Je n'ai pas parcouru toutes les sorties mais ceux que j'ai ouvertes étaient toutes conformes. 
Pour faire cela, j'ai dû être stricte sur les tableaux qui étaient extraits. J'ai choisi de ne pas extraire les tableaux contenant des "rowspan" ou "colspan" ainsi que les tableaux "imbriqués" c'est-à-dire des tableaux dont les cellules sont elles aussi des tableaux. 

J'ai fais ces choix, tout d'abord car, sans astuce, il n'est pas possible de faire ce genre de tableaux en CSV. De plus, traiter ce genre de tableaux nécessite des algorithmes d'extraction élaborés et complexes que je n'étais pas en mesure de produire. Des astuces simples auraient pu être envisagées, recopier le texte dans toutes les cellules fusionnées par exemple, mais cela génère par suite des soucis avec des tableaux mal extraits. 

Enfin, mon code est très peu commenté, avec un peu plus de temps j'aurais pu le commenter ou utiliser une Library de documentation. Toutefois, il est je pense, facilement réutilisable et évoluable aisément.

## Synthèse générale: est-ce que les tableaux de Wikipedia que vous avez extrait sont exploitables par des outils statistiques ? 


Les tableaux extrait peuvent tous être exploitables par des outils statistiques. 
Certains tableaux pourront nécessiter un traitement humain préalable (j'espère très peu voir aucun). 


