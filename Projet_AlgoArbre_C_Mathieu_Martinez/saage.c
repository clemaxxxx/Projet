#include "saage.h"

static int serialise_aux(FILE *f, Noeud *noeud,int indentation) {
    if (noeud == NULL) {
        fprintf(f, "NULL\n");
        return 1;
    }
    if(indentation == 0){
        fprintf(f,"%*sValeur : %s\n",indentation*4, "", noeud->val);
    }else{
        fprintf(f,"\n%*sValeur : %s\n",indentation*4, "", noeud->val);
    }

    fprintf(f,"%*sGauche : ", indentation*4, "");
    if (!serialise_aux(f, noeud->fg, indentation+1))
        return 0;

    fprintf(f, "%*sDroite : ", indentation*4, "");
    if (!serialise_aux(f, noeud->fd, indentation+1))
        return 0;
    return 1;
}

int serialise(char *nom_de_fichier, Arbre A) {
    FILE *f = fopen(nom_de_fichier, "w");
    if (f == NULL) {
        return 0; 
    }
    int s = serialise_aux(f, A, 0);
    fclose(f);
    return s;
}


static Arbre deserialise_aux(FILE *f) {
    char ligne[TAILLE]; 
    char * etiquette; 
    Arbre noeud; 
    if (fgets(ligne, TAILLE, f) == NULL) {
        return NULL; 
    }
    etiquette = strchr(ligne, ':') + 2; 
    etiquette[strlen(etiquette) - 1] = '\0'; 
    noeud = alloue_noeud(etiquette);

    fgets(ligne, TAILLE, f);
    if (strstr(ligne, "NULL") == NULL) { 
        noeud->fg = deserialise_aux(f); 
    } else {
        noeud->fg = NULL; 
    }

    fgets(ligne, TAILLE, f);
    if (strstr(ligne, "NULL") == NULL) { 
        noeud->fd = deserialise_aux(f); 
    } else {
        noeud->fd = NULL; 
    }
    return noeud; 
}

int deserialise(char *nom_de_fichier, Arbre * A) {
    FILE *f = fopen(nom_de_fichier, "r");
    if (f == NULL) {
        return 0; 
    }
    *A = deserialise_aux(f); 
    fclose(f); 
    if(*A==NULL){
        return 0;
    }
    return 1; 
}

