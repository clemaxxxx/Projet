#ifndef CODEUR_H
    #define CODEUR_H
    #include <stdio.h>
    #include <stdlib.h>
    #include <time.h>
    #include "QuadTree.h"
    #include "Buffer.h"





    void codage(QuadTree * tree, unsigned char* pixel,  int ligne, int colonne, int index, int niveau,int x, int y, int largeur_totale);
    void ecrireArbreQTC(QuadTree *tree, const char * nom_fichier,unsigned long taille_origine);
#endif
