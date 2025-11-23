#include "arbres_binaires.h"
#include "greffe.h"
#include "saage.h"


int main(int argc, char *argv[]){
    if(argc == 3 && strcmp(argv[1], "-E")==0){
        Arbre A;
        construit_arbre(&A);
        if(serialise(argv[2],A)==0){
            liberer(&A);
            return 0;
        }
        liberer(&A);

    }else if(argc == 4 && strcmp(argv[1], "-G")== 0){
        Arbre source;
        Arbre greffon;

        if(deserialise(argv[2],&source)==0){
            return 0;
        }
        if(deserialise(argv[3],&greffon)==0){
            liberer(&source);
            return 0;
        }
        
        expansion(&source,greffon);

        if(serialise("resultat.saage",source)==0){
            liberer(&source);
            liberer(&greffon);
            return 0;
        }

        liberer(&source);
        liberer(&greffon);

    }
    return 1;
}