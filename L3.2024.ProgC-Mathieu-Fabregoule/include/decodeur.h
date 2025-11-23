#ifndef DECODEUR_H
    #define DECODEUR_H
    #include <stdio.h>
    #include <stdlib.h>
    #include "QuadTree.h"
    #include "Buffer.h"



    void LireNoeud(BitStream *bitstream, QuadTree *tree, int index,int niveau);
    QuadTree * decodageQTC(const char *nom_fichier);
#endif
