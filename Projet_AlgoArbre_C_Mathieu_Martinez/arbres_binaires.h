#ifndef ARBRES_BINAIRES_H
#define ARBRES_BINAIRES_H
#define TAILLE 250
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

typedef struct _noeud {
    char * val ;
    struct _noeud * fg , * fd ;
} Noeud , * Arbre ;


Noeud * alloue_noeud(char * s);

void liberer(Arbre *A);

Arbre cree_A_1(void);

Arbre cree_A_2(void);

Arbre cree_A_3(void);

int construit_arbre(Arbre *a);


#endif