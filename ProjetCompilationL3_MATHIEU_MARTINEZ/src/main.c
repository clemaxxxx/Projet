#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <string.h>
#include "tree.h"
#include "parser.h"
#include "symbol_table.h"
#include "Traduction.h"
#include "semantique.h"
extern Node *root;

void Help(){
  printf("Usage : ./tpcas [Option] < fichier\n");
  printf("Options : \n");
  printf("-t, Affiche l'arbre abstrait\n");
  printf("-h  Affiche cette aide\n");
  printf("-s Affihce la table des symboles\n");
}


int main(int argc, char *argv[]){
    SymbolTable * t;
    int arbre = 0;
    int help = 0;
    int symbol = 0;
    for(int i=1; i<argc;i++){
        if(strcmp(argv[i], "-t")==0 || strcmp(argv[i], "-tree")==0 ){
            arbre = 1;
        }else if(strcmp(argv[i], "-help")==0 || strcmp(argv[i], "-h")==0){
            help = 1;
        }else if(strcmp(argv[i], "-symtabs")==0 || strcmp(argv[i], "-s")==0){
            symbol = 1;
        }else{
            printf("Option Inconnus");
            Help();
            return 2;
        }
    }
    
    if(help){
        Help();
        return yyparse();
    }
    if(yyparse() == 0) {
        printf("Analyse réussie.\n");
            if(root && arbre) {
                printf("Arbre abstrait généré :\n");
                printTree(root); 
            }
        t = SymbolTableFromTree(root);
        if(symbol){
            PrintTable(t);
        }
        check_error(root,t);
        open_file();
        Translate(root,t);
        close_file();

    }else{
        printf("Erreur pendant l'analyse.\n");
        return 1;
    }

    
    return 0;
}