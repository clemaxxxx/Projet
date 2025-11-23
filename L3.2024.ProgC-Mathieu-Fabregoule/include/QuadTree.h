#ifndef QUADTREE_H
    #define QUADTREE_H
    #include <stdio.h>
    #include <stdlib.h>
    #include <math.h>
    #include <stdbool.h>

    typedef struct {
        unsigned char m; //moyenne d'un noeud
        unsigned char e; //Valeur d'erreur d'un noeud
        unsigned char u; //bit d’uniformité d'un noeud
        float variance; //variance d'un noeud
    } Noeud;

    typedef struct {
        Noeud * tabNoeud; // Tableau de noeuds qui correspond a l'arbre
        int n; //Le nombre de niveau
    } QuadTree;

    QuadTree* initialiseQuadTree(int ligne,int colonne);
    QuadTree* initialiseQuadTreeSelonNiveau(int niveau);
    void libererQuadTree(QuadTree* quadtree);
    int nbNoeuds(int niveau);
    int calculerNiveauMax(int ligne, int colonne);
    void filterQuadTree_G(QuadTree *qt, double alpha, double beta);

    




#endif