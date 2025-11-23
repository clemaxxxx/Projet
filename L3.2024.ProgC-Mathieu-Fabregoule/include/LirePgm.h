#ifndef LIREPGM_H
    #define LIREPGM_H
    #include <stdio.h>
    #include <stdlib.h>
    #include "QuadTree.h"
    #include <string.h>

   

    unsigned char* lireImagePGM(const char* f, int *ligne, int *colonne);
    void EcriturePixmap(unsigned char * pixel,QuadTree * tree,int niveau,int x, int y,int ligne,int colonne , int largeur_totale, int index);
    void sauverImagePGM(unsigned char *pixels, const char *nom_fichier, int largeur, int hauteur);
    void EcritureGrilleSegmentation(unsigned char *pixel, QuadTree *tree, int niveau, int x, int y, int ligne, int colonne, int largeur_totale, int index);
    void EditionGrille(QuadTree *t, int ligne, int colonne, const char * nom);



#endif
