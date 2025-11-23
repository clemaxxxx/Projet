#include "options.h"
#include "qtc.h"

/*
 * Module : options
 * -----------------
 * Ce module gère les options de ligne de commande et leur traitement pour configurer
 * le programme. Il inclut les fonctions pour afficher un menu d'aide, initialiser 
 * les options, et gérer les paramètres pour l'encodage et le décodage d'images.
 *
 *
 */





/*
 * Fonction : print_Menu
 * ---------------------
 * Affiche le menu des options disponibles pour le programme.
 *
 * Cette fonction liste les options et paramètres utilisables, comme le mode encodeur 
 * ou décodeur, les fichiers d'entrée et de sortie, ainsi que des options comme 
 * l'activation de la grille ou le paramètre alpha.
 *
 * Retour :
 * - Aucun.
 */

static void print_Menu(){
    printf("Options disponibles :\n");
    printf("-h              : Affiche cette aide\n");
    printf("-c              : Mode encodeur\n");
    printf("-u              : Mode decodeur\n");
    printf("-i <file>       : Fichier d'entree (pgm ou qtc)\n");
    printf("-o <file>       : Rennomer fichier de sortie (pgm ou qtc)\n");
    printf("-g              : Activer grille de segmentation\n");
    printf("-v              : Mode bavard\n");
    printf("-a <float>      : Definir alpha\n");
    exit(0);
}




/*
 * Fonction : print_Verbose
 * -------------------------
 * Affiche les différents paramètres choisis
 *
 * Retour :
 * - Aucun
 */

static void print_Verbose(Options o) {
    printf("Parametres rentres :\n");
    printf("Mode encodeur :  ");
    if (o.modeEncodeur) {
        printf("Codage");
    } else {
        printf("Décodage");
    }
    printf("\nNom du fichier d'entree : %s\n", o.inputFile);
    printf("Nom du fichier de sortie : %s\n", o.outputFile);
    if (o.alpha == 0.0) {
        printf("Aucun filtrage\n");
    } else {
        printf("Filtrage avec alpha = %f\n", o.alpha);
    }
    if (o.grille) {
        printf("La grille de segmentation est activee\n");
    } else {
        printf("La grille de segmentation est désactivee\n");
    }
}






/*
 * Fonction : initOption
 * ---------------------
 * Initialise une structure Options avec des valeurs par défaut.
 *
 * Paramètres :
 * - o : Pointeur vers la structure Options à initialiser.
 * 
 * Retour :
 * - Aucun.
 */

static void initOption(Options *o){
    o->modeEncodeur = 0;
    o->alpha = 0.0;
    o->grille = 0;
    o->verbose = 0;
}



/*
 * Fonction : gestionOptions
 * -------------------------
 * Analyse et gère les options de ligne de commande fournies par l'utilisateur.
 *
 * Paramètres :
 * - argc : Nombre d'arguments en ligne de commande.
 * - argv : Tableau contenant les arguments de ligne de commande.
 * - options : Pointeur vers la structure Options à remplir avec les valeurs.
 *
 * Description :
 * - Parcourt les arguments en ligne de commande pour configurer les options dans la strcuture Options.
 * 
 * Retour :
 * - Aucun.
 */


void gestionOptions(int argc, char** argv, Options* options) {
    initOption(options);
    for (int i = 1; i < argc; i++) {
        if (strcmp(argv[i], "-h") == 0) {
            print_Menu();
        } else if (strcmp(argv[i], "-c") == 0) {
            options->modeEncodeur = 1;
        } else if (strcmp(argv[i], "-u") == 0) {
            options->modeEncodeur = 0;
        } else if (strcmp(argv[i], "-i") == 0) {
            if (i + 1 < argc) {
                strcpy(options->inputFile, argv[i + 1]);
                i++;
            } else {
                printf("Erreur Manque fichier d'entree apres l'option -i\n");
                exit(1);
            }
        } else if (strcmp(argv[i], "-o") == 0) {
            if (i + 1 < argc) {
                strcpy(options->outputFile, argv[i + 1]);
                i++;
            } else {
                printf("Erreur Manque fichier de sortie apres l'option -o\n");
                exit(1);
            }
        } else if (strcmp(argv[i], "-g") == 0) {
            options->grille = 1;
        } else if (strcmp(argv[i], "-v") == 0) {
            options->verbose = 1;
        } else if (strcmp(argv[i], "-a") == 0) {
            if (i + 1 < argc) {
                options->alpha = atof(argv[i + 1]);
                i++;
            }else {
                printf("Erreur Manque la valeur pour alpha après l'option -a\n");
                exit(1);
            }
        } else {
            printf("Option inconnue : %s\n", argv[i]);
            exit(1);
        }
    }
    if (strlen(options->outputFile) == 0) {
        if (options->modeEncodeur) {
            strcpy(options->outputFile, "QTC/out.qtc");
        } else {
            strcpy(options->outputFile, "PGM/out.pgm");
        }
    }
}




/*
 * Fonction : Encodage
 * --------------------
 * Effectue le processus d'encodage d'une image en utilisant un QuadTree.
 *
 * Paramètres :
 * - o : Structure Options contenant les configurations nécessaires, 
 * telles que les noms des fichiers d'entrée/sortie et les paramètres d'encodage.
 *
 * Retour :
 * - Aucun.
 *
 */

void Encodage(Options o){
    if(!o.modeEncodeur){
        fprintf(stderr,"Erreur");
        exit(1);
    }
    if(o.verbose){
        print_Verbose(o);
    }
    const char* nomFichier = o.inputFile;
    const char* nom = o.outputFile;
    int ligne, colonne;
    unsigned char* image = lireImagePGM(nomFichier, &ligne, &colonne);
    if (image == NULL) {
        fprintf(stderr, "Erreur lors de la lecture de l'image.\n");
        exit(1);
    }
    unsigned long taille_origine = (unsigned long)(ligne*colonne*8); 
    QuadTree* tree = initialiseQuadTree(ligne, colonne);
    codage(tree, image, ligne, colonne, 0, 0, 0, 0, ligne);
    if(o.alpha != 0.0){
        filterQuadTree_G(tree,o.alpha,1.0);
    }
    if(o.grille){
        EditionGrille(tree,ligne,colonne,nomFichier);
    }
    printf("%s",nom);
    ecrireArbreQTC(tree, nom,taille_origine);
    libererQuadTree(tree);
    free(image);
}



/*
 * Fonction : Decodage
 * --------------------
 * Effectue le processus de décodage d'un fichier QTC pour générer une image PGM.
 *
 * Paramètres :
 * - o : Structure Options contenant les configurations nécessaires, 
 *   telles que les noms des fichiers d'entrée/sortie et les paramètres de décodage.
 *
 *
 * Retour :
 * - Aucun.
 *
 */


void Decodage(Options o){
    if(o.modeEncodeur){
        fprintf(stderr,"Erreur");
        exit(1);
    }
    if(o.verbose){
        print_Verbose(o);
    }
    const char* nomFichier = o.inputFile;
    QuadTree* t =decodageQTC(nomFichier);
    int ligne = 1 << t->n;   
    int colonne = 1 << t->n; 
    unsigned char* p = (unsigned char*)malloc(ligne * colonne * sizeof(unsigned char));
    if (p == NULL) {
        fprintf(stderr, "Erreur d'allocation de mémoire.\n");
        exit(1);
    }
    if(o.grille){
        EditionGrille(t,ligne,colonne,o.outputFile);
    }
    EcriturePixmap(p, t, 0, 0, 0, ligne, colonne, ligne, 0);
    sauverImagePGM(p,o.outputFile, ligne, colonne);
    libererQuadTree(t);
    free(p);
}


