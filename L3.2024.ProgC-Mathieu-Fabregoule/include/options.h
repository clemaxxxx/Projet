#ifndef OPTIONS_H
    #define OPTIONS_H
    #include <stdio.h>
    #include <stdlib.h>
    #include <string.h>
    #include "codeur.h"
    #include "decodeur.h"
    #include "LirePgm.h"

    typedef struct {
        int modeEncodeur;    //1 pour le mode codeur et 0 pour le mode decodeur
        char inputFile[256];   //Fichier d'entree
        char outputFile[256];  //Fichier de sortie
        int grille;   // Indique si la grille de segmentation doit etre faite 1 pour avtiver cette option sinon 0
        int verbose;  //1 pour activer le mode verbose sinon 0           
        double alpha; //Taux de perte pour l'encodage         
    } Options;

    void gestionOptions(int argc, char** argv, Options* options);
    void Encodage(Options o);
    void Decodage(Options o);
#endif
