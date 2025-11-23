#include "codeur.h"


/*
 * Module : codeur
 * -------------------------
 * Ce module implémente les opérations de codage, compression et écriture d'un QuadTree.
 * Il inclut des fonctions pour la realisation du QuadTree a aprtir d'une image, le calcul des bits nécessaires 
 * pour la compression, et l'écriture du QuadTree dans un fichier QTC.
 */



/*
 * Fonction : puissance
 * ---------------------
 * Calcule la puissance d'un entier.
 *
 * Paramètres :
 * - base : La base.
 * - expo : L'exposant.
 *
 * Retourne le résultat de la base élevé à l'exposant.
 */
static int puissance(int base,int expo){
    int res = 1;
    for(int i=0;i<expo;i++){
        res*=base;
    }
    return res;
}



/*
 * Fonction : codage
 * -----------------
 * Construit le QuadTree à partir d'un tableau de pixels d'une image.
 * La fonction effectue des appels récursifs de manière descendante jusqu'aux feuilles,
 * puis remonte de manière ascendante pour créer les nœuds, en calculant la moyenne, l'erreur 
 * et en vérifiant l'uniformité pour chaque nœud du QuadTree.
 *
 * Paramètres :
 * - tree : Pointeur vers le QuadTree à remplir.
 * - pixel : Tableau contenant les données des pixels de l'image.
 * - ligne : Nombre de lignes de l'image.
 * - colonne : Nombre de colonnes de l'image.
 * - index : Index du nœud actuel dans le QuadTree.
 * - niveau : Niveau actuel du QuadTree.
 * - x, y : Coordonnées du coin supérieur gauche du quadrant actuel.
 * - largeur_totale : Largeur totale de l'image.
 *
 * Retour :
 * - Aucun (la fonction modifie directement le QuadTree).
 */
void codage(QuadTree * tree, unsigned char* pixel,  int ligne, int colonne, int index, int niveau,int x, int y,int largeur_totale){
    if(niveau==tree->n){
        int p = y*largeur_totale+x;
        tree->tabNoeud[index].m = pixel[p];
        tree->tabNoeud[index].e = 0;
        tree->tabNoeud[index].u = 1;
        return ;
    }
    int demiligne = ligne/2;
    int demicolonne = colonne/2;
    codage(tree,pixel,demiligne,demicolonne,4*index+1,niveau+1,x,y,largeur_totale);
    codage(tree,pixel,demiligne,demicolonne,4*index+2,niveau+1,x+demicolonne,y,largeur_totale);
    codage(tree,pixel,demiligne,demicolonne,4*index+3,niveau+1,x+demicolonne,y+demiligne,largeur_totale);
    codage(tree,pixel,demiligne,demicolonne,4*index+4,niveau+1,x,y+demiligne,largeur_totale);
    int fils1= 4*index+1;
    int fils2= 4*index+2;
    int fils3= 4*index+3;
    int fils4= 4*index+4;
    unsigned char m1 = tree->tabNoeud[fils1].m;
    unsigned char m2 = tree->tabNoeud[fils2].m;
    unsigned char m3 = tree->tabNoeud[fils3].m;
    unsigned char m4 = tree->tabNoeud[fils4].m;
    int s = m1+m2+m3+m4;
    tree->tabNoeud[index].m = s/4;
    if(tree->tabNoeud[fils1].u && tree->tabNoeud[fils2].u && tree->tabNoeud[fils3].u && tree->tabNoeud[fils4].u && m1==m2 && m2==m3 && m3==m4){
        tree->tabNoeud[index].u = 1;
        tree->tabNoeud[index].e = 0;
    }else{
        tree->tabNoeud[index].u = 0;
        tree->tabNoeud[index].e = s%4;
    }
}



/*
 * Fonction : ecrireNoeud
 * -----------------------
 * Écrit un nœud du QuadTree dans un buffer (BitStream) selon les règles de compression.
 * Cette fonction fait des appels récursive pour chaque nœud du QuadTree à différents niveaux.
 *
 * Paramètres :
 * - bitstream : Pointeur vers le BitStream où les données seront écrites.
 * - tree : Pointeur vers le QuadTree.
 * - index : Index du nœud actuel dans le QuadTree.
 * - niveau : Niveau actuel du QuadTree.
 *
 * Retour :
 * - Aucun.
 */
static void ecrireNoeud(BitStream *bitstream, QuadTree *tree, int index,int niveau) {
    if (niveau >tree->n) {
        return; 
    }
    int p = puissance(4,niveau);
    for(int i =0 ; i<p;i++){ 
        unsigned char m = tree->tabNoeud[index].m;
        unsigned char e = tree->tabNoeud[index].e;
        unsigned char u = tree->tabNoeud[index].u;
        bool fils4 = (index % 4 == 0);
        int pere_u = tree->tabNoeud[(index-1)/4].u;
        if(pere_u && niveau !=0){

        }else if (niveau == 0) {
            writeBits(bitstream, m, 8);
            writeBits(bitstream, e, 2);
            if (e == 0) {
                writeBits(bitstream, u, 1);
            }
        } 
        else if (niveau == tree->n) { 
            if (fils4==false) { 
                writeBits(bitstream, m, 8);
            }
        }
        else { 
            if (fils4==false) { 
                writeBits(bitstream, m, 8);
                writeBits(bitstream, e, 2);
                if (e == 0) {
                    writeBits(bitstream, u, 1);
                }
            } else { 
                writeBits(bitstream, e, 2);
                if (e == 0) {
                    writeBits(bitstream, u, 1);
                }
            }
        }
    index++;
    }
    ecrireNoeud(bitstream, tree, index,niveau+1);
}





/*
 * Fonction : CompterBits
 * ----------------------
 * Compte le nombre total de bits nécessaires pour encoder un QuadTree avec sa structure actuelle.
 * Cette fonction est utilisée pour calculer le taux de compression.
 *
 * Paramètres :
 * - tree : Pointeur vers le QuadTree.
 * - index : Index du nœud actuel dans le QuadTree.
 * - niveau : Niveau actuel du QuadTree.
 * - cpt : Compteur de bits.
 *
 * Retour :
 * - Aucun.
 */
static void CompterBits( QuadTree *tree, int index,int niveau,int *cpt) {
    if (niveau >tree->n) {
        return; 
    }
    int p = puissance(4,niveau);
    for(int i =0 ; i<p;i++){ 
        unsigned char e = tree->tabNoeud[index].e;
        bool fils4 = (index % 4 == 0);
        int pere_u = tree->tabNoeud[(index-1)/4].u;
        if(pere_u && niveau !=0){

        }else if (niveau == 0) {
            *cpt +=8;
            *cpt+=2;
            if (e == 0) {
                *cpt+=1;
            }
        } 
        else if (niveau == tree->n) { 
            if (fils4==false) { 
                *cpt+=8;
            }
        }
        else { 
            if (fils4==false) { 
                *cpt+=8;
                *cpt+=2;
                if (e == 0) {
                    *cpt+=1;
                }
            } else { 
                *cpt+=2;
                if (e == 0) {
                    *cpt+=1;
                }
            }
        }
    index++;
    }
    CompterBits(tree, index,niveau+1,cpt);
}




/*
 * Fonction : ecrireArbreQTC
 * -------------------------
 * Écrit le QuadTree dans un fichier au format QTC.
 * Elle inclut le calcul du taux de compression et ajoute des informations sur la Date et type du fichier.
 *
 * Paramètres :
 * - tree : Pointeur vers le QuadTree à écrire.
 * - nom_fichier : Nom du fichier de sortie.
 * - taille_origine : Taille d'origine des données du QuadTree.
 *
 * Retour :
 * - Aucun.
 */
void ecrireArbreQTC(QuadTree *tree, const char *nom_fichier, unsigned long taille_origine) {
    FILE *fichier = fopen(nom_fichier, "wb");
    if (!fichier) {
        perror("Erreur d'ouverture du fichier");
        return;
    }
    int bitCount =0;
    CompterBits(tree,0,0,&bitCount);
    double r = (double)bitCount / (double)taille_origine * 100.0;
    fprintf(fichier, "Q1\n");  
    fprintf(fichier, "# %s\n", __DATE__);  
    fprintf(fichier, "# compression rate: %.2f%%\n",r); 
    fputc(tree->n, fichier);   
    BitStream bitstream;
    initBitStream(&bitstream, fichier);
    ecrireNoeud(&bitstream, tree, 0, 0);
    flushBitStream(&bitstream);
    fclose(fichier);
    printf("Fichier QTC ecrit dans %s\n",nom_fichier);
}

