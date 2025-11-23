#ifndef SAAGE_H
#define SAAGE_H
#include "arbres_binaires.h"


int serialise(char *nom_de_fichier, Arbre A);

int deserialise(char *nom_de_fichier, Arbre * A);


#endif