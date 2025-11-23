#include "arbres_binaires.h"

Noeud* alloue_noeud(char* s) {
    Noeud* nouveau = (Noeud*)malloc(sizeof(Noeud));
    if (nouveau == NULL) {
        return NULL;
    }
    nouveau->val = strdup(s);
    if(nouveau->val == NULL){
        free(nouveau);
        return NULL;
    }
    nouveau->fg = NULL;
    nouveau->fd = NULL;
    return nouveau;
}

void liberer(Arbre *A) {
    if (*A) {
        liberer(&((*A)->fg));
        liberer(&((*A)->fd));
        free((*A)->val);
        free(*A);
        *A = NULL;
    }
}

Arbre cree_A_1(void) {
    Arbre A1 = alloue_noeud("arbre");
    A1->fg = alloue_noeud("binaire");
    A1->fd = alloue_noeud("ternaire");
    return A1;
}

Arbre cree_A_2(void) {
    Arbre A2 = alloue_noeud("Anémone");
    A2->fg = alloue_noeud("Camomille");
    A2->fd = alloue_noeud("Camomille");
    A2->fd->fg = alloue_noeud("Dahlia");
    A2->fd->fg->fd = alloue_noeud("Camomille");
    A2->fd->fg->fd->fd = alloue_noeud("Jasmin");
    A2->fd->fg->fd->fg = alloue_noeud("Iris");
    return A2;
}


Arbre cree_A_3(void) {
    Arbre A3 = alloue_noeud("Intel Core i9");
    A3->fd = alloue_noeud("Intel Core i9");
    A3->fd->fg = alloue_noeud("Intel Core i9");
    A3->fg = alloue_noeud("Apple M3 Max");
    A3->fg->fd = alloue_noeud("AMD Ryzen 9");
    A3->fg->fd->fg = alloue_noeud("Intel Core i9");
    return A3;
}



int construit_arbre(Arbre *a) {
    char saisie[100];
    fgets(saisie, 100, stdin);
    if (saisie[0] == '0') {
        *a = NULL;
        return 0;
    } else {
        int i = 2;
        while (saisie[i] != '"') {
            i++;
        }
        char *etiquette = strtok(saisie + 2, "\" ");
        *a = alloue_noeud(etiquette);

        if (saisie[i + 3] == '0') {
            construit_arbre(&((*a)->fg));
            construit_arbre(&((*a)->fd));
            return 1;
        } else {
            if (!construit_arbre(&((*a)->fg)) && !construit_arbre(&((*a)->fd))) {
                return 0;
            }
            return 1;
        }
    }
}
