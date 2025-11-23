#include "LirePgm.h"

/*
 * Module : LirePgm.c
 * ------------------
 * Ce module contient les fonctions nécessaires pour lire et écrire des images au format PGM.
 * Les fonctionnalités incluent la lecture d'un fichier PGM en mémoire sous forme de tableau de pixels,
 * ainsi que l'écriture d'une image PGM à partir d'un tableau de pixels.
 * 
 * 
 */






/*
 * Fonction : lireImagePGM
 * -------------------------
 * Cette fonction permet de lire une image au format PGM et de la charger en mémoire sous forme de tableau de pixels.
 * 
 * Paramètres :
 * - f : Le nom du fichier PGM à lire.
 * - ligne : Pointeur vers un entier où sera stocké le nombre de lignes de l'image.
 * - colonne : Pointeur vers un entier où sera stocké le nombre de colonnes de l'image.
 * 
 * Retour :
 * - Un tableau d'unsigned char représentant les pixels de l'image.
 * - Si une erreur survient lors de la lecture, la fonction retourne NULL.
 */
unsigned char* lireImagePGM(const char* f, int* ligne, int* colonne) {
    FILE* fichier = fopen(f, "rb");
    if(fichier == NULL) {
        fprintf(stderr, "Erreur Impossible d'ouvrir le fichier %s\n", f);
        return NULL;
    }
    char header[3];
    if(fgets(header,sizeof(header),fichier)==NULL || header[0]!='P' || header[1]!='5') {
        fprintf(stderr, "Erreur Format incorrect\n");
        fclose(fichier);
        return NULL;
    }
    char buffer[256];
    int max_val = 0;
    while(fgets(buffer,sizeof(buffer),fichier)) {
        if(buffer[0]=='#') {
            continue;
        }
        if(sscanf(buffer, "%d %d %d", colonne, ligne, &max_val) == 3) {
            break;
        }
        if(sscanf(buffer, "%d %d", colonne, ligne) == 2) {
            while(fgets(buffer, sizeof(buffer), fichier)) {
                if(buffer[0] == '#') {
                    continue; 
                }
                if(sscanf(buffer, "%d", &max_val) == 1) {
                    break;
                }
            }
            break;
        }
    }

    if(*colonne<=0 || *ligne<=0 || max_val<=0 || max_val>255) {
        fprintf(stderr, "Erreur Dimensions ou valeur maximale incorrecte\n");
        fclose(fichier);
        return NULL;
    }
    unsigned char* pixels = (unsigned char*)malloc((*ligne) * (*colonne) * sizeof(unsigned char));
    if(pixels == NULL) {
        fprintf(stderr, "Erreur Memoire insuffisante\n");
        fclose(fichier);
        return NULL;
    }
    size_t totalPixels = (*ligne)*(*colonne);
    if(fread(pixels, sizeof(unsigned char), totalPixels, fichier) != totalPixels) {
        fprintf(stderr, "Erreur lecture des pixels \n");
        free(pixels);
        fclose(fichier);
        return NULL;
    }
    fclose(fichier);
    return pixels;
}


/*
 * Fonction : EcriturePixmap
 * ---------------------------
 * Remplie un tableau de pixels à partir d'un QuadTree en traversant
 * récursivement l'arbre et en attribuant des valeurs aux pixels.
 *
 * Paramètres :
 * - pixel : Tableau d'unsigned char représentant l'image.
 * - tree : Pointeur vers le QuadTree contenant les données des pixels.
 * - niveau : Niveau actuel de l'arbre.
 * - x, y : Coordonnées du pixel à remplir.
 * - ligne, `colonne` : Dimensions de l'image.
 * - largeur_totale : Largeur totale de l'image pour indexer le tableau `pixel`.
 * - index : Index du nœud actuel dans `tree->tabNoeud`.
 *
 * La fonction divise récursivement l'image en quatre sous-images pour remplir
 * le tableau de pixels avec les valeurs issues des nœuds de l'arbre.
 */

 void EcriturePixmap(unsigned char * pixel,QuadTree * tree,int niveau,int x, int y,int ligne,int colonne , int largeur_totale, int index){
    if(niveau == tree->n){
        int p = y*largeur_totale+x;
        pixel[p] = tree->tabNoeud[index].m;
        return;
    }
    int demiligne = ligne/2;
    int demicolonne = colonne/2;
    EcriturePixmap(pixel,tree,niveau+1,x,y,demiligne,demicolonne,largeur_totale,4*index+1);
    EcriturePixmap(pixel,tree,niveau+1,x+demicolonne,y,demiligne,demicolonne,largeur_totale,4*index+2);
    EcriturePixmap(pixel,tree,niveau+1,x+demicolonne,y+demiligne,demiligne,demicolonne,largeur_totale,4*index+3);
    EcriturePixmap(pixel,tree,niveau+1,x,y+demiligne,demiligne,demicolonne,largeur_totale,4*index+4);
}




/*
 * Fonction : sauverImagePGM
 * ----------------------------
 * Sauvegarde un tableau de pixels dans un fichier au format PGM.
 * 
 * Paramètres :
 * - pixels : Tableau contenant les valeurs des pixels à sauvegarder.
 * - nom_fichier : Le nom du fichier où l'image sera sauvegardée.
 * - largeur : Largeur de l'image.
 * - hauteur : Hauteur de l'image.
 *
 * La fonction crée un fichier PGM avec les informations d'image et écrit les pixels dans le fichier.
 */
void sauverImagePGM(unsigned char *pixels, const char *nom_fichier, int largeur, int hauteur) {
    FILE *fichier = fopen(nom_fichier, "wb");
    if (!fichier) {
        perror("Erreur d'ouverture du fichier");
        return;
    }
    fprintf(fichier, "P5\n");            
    fprintf(fichier, "%d %d\n", largeur, hauteur); 
    fprintf(fichier, "255\n");               
    size_t totalPixels = largeur * hauteur;
    fwrite(pixels, sizeof(unsigned char), totalPixels, fichier);
    fclose(fichier);
    printf("Image sauvegardee dans %s\n", nom_fichier);
}



void EcritureGrilleSegmentation(unsigned char *pixel, QuadTree *tree, int niveau, int x, int y, int ligne, int colonne, int largeur_totale, int index) {
    if(niveau == tree->n) {
        int p = y * largeur_totale + x;
        pixel[p] = 200;  
        return;
    }
    if(tree->tabNoeud[index].u == 1) {
        for (int i = 0; i < ligne; i++) {
            for (int j = 0; j < colonne; j++) {
                int p = (y + i)*largeur_totale+(x+j);
                if (i == 0 || j == 0 || i == ligne - 1 || j == colonne - 1) {
                    pixel[p] = 200;  
                } else {
                    pixel[p] = 255;  
                }
            }
        }
    } else {
        int demiligne = ligne / 2;
        int demicolonne = colonne / 2;
        EcritureGrilleSegmentation(pixel, tree, niveau + 1, x, y, demiligne, demicolonne, largeur_totale, 4 * index + 1);
        EcritureGrilleSegmentation(pixel, tree, niveau + 1, x + demicolonne, y, demiligne, demicolonne, largeur_totale, 4 * index + 2);
        EcritureGrilleSegmentation(pixel, tree, niveau + 1, x + demicolonne, y + demiligne, demiligne, demicolonne, largeur_totale, 4 * index + 3);
        EcritureGrilleSegmentation(pixel, tree, niveau + 1, x, y + demiligne, demiligne, demicolonne, largeur_totale, 4 * index + 4);
    }
}


static char * NomGrilleS(const char *nom_fichier) {
    char *grille_nom = strdup(nom_fichier);
    if (!grille_nom) {
        perror("Erreur d'allocation mémoire");
        exit(EXIT_FAILURE);
    }
    char *extension = strrchr(grille_nom, '.');
    strcpy(extension, "_g.pgm");
    return grille_nom;
}

void EditionGrille(QuadTree *t, int ligne, int colonne, const char * nom){
     unsigned char* pixmapGrille = (unsigned char*)malloc(ligne * colonne * sizeof(unsigned char));
     if (pixmapGrille == NULL) {
            fprintf(stderr, "Erreur d'allocation de mémoire.\n");
            exit(1);
        }
        EcritureGrilleSegmentation(pixmapGrille, t, 0, 0, 0, ligne, colonne, ligne, 0);
        sauverImagePGM(pixmapGrille,NomGrilleS(nom), ligne, colonne);
        free(pixmapGrille);
}