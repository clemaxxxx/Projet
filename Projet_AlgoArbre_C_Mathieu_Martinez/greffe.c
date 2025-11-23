#include "greffe.h"

int copie(Arbre * dest, Arbre source) {
    if (source == NULL) {
        *dest = NULL;
        return 1;
    }
    *dest = alloue_noeud(source->val);
    if (*dest == NULL) {
        return 0;
    }
    int copie_gauche = copie(&((*dest)->fg), source->fg);
    int copie_droite = copie(&((*dest)->fd), source->fd);
    if (!copie_gauche || !copie_droite) {
        liberer(dest);
         return 0;
    }
    return 1;
}

static int greffe(Arbre *G, Arbre B, Arbre C) {
    if ((*G) == NULL) {
        return 1;
    }
    if ((*G)->fg == NULL && (*G)->fd == NULL) {
        copie(&((*G)->fg), B);
        copie(&((*G)->fd), C);
        return 1;
    } 
    else if ((*G)->fg == NULL) {
        copie(&((*G)->fg), B);
        return greffe(&((*G)->fd), B, C);
    }
    else if ((*G)->fd == NULL) {
        copie(&((*G)->fd), C);
        return greffe(&((*G)->fg), B, C);
    }
    greffe(&((*G)->fg), B, C);
    greffe(&((*G)->fd), B, C);
    return 1;
}

int expansion(Arbre *A, Arbre B) {
    if (*A == NULL) {
        return 1;
    }
    expansion(&(*A)->fg, B);
    expansion(&(*A)->fd, B);
    if (strcmp((*A)->val, B->val) == 0) {
        Arbre copie_B;
        if (!copie(&copie_B, B))
            return 0;
        greffe(&copie_B, (*A)->fg, (*A)->fd);
        liberer(A);
        (*A) = copie_B;
        return 1;
    }
    return 1;
}