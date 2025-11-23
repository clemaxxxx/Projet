#include "decodeur.h"

/*
 * Module : decodeur 
 * --------------------------------
 * Ce module implémente les fonctions nécessaires pour décoder un QuadTree à partir d'un fichier QTC 
 * gérer la reconstruction des valeurs de l'arbre et effectuer l'interpolation des valeurs des nœuds . 
 * Les données sont lues à partir d'un buffer (Bitstream) et reconstruites dans la structure QuadTree.
 */




static int puissance(int base,int expo){
    int res = 1;
    for(int i=0;i<expo;i++){
        res*=base;
    }
    return res;
}



/*
 * Fonction : interpolation
 * -------------------------
 * Effectue l'interpolation pour déterminer la valeur d'un nœud basé sur les nœuds voisins.
 *
 *
 * Paramètres :
 * - t : Pointeur vers le QuadTree contenant les données.
 * - index : L'index du nœud pour lequel l'interpolation est effectuée.
 *
 * Retour :
 * - La valeur interpolée du nœud.
 */
static unsigned char interpolation(QuadTree * t, int index){
    unsigned m;
    m = (4*t->tabNoeud[(index-1)/4].m +t->tabNoeud[(index-1)/4].e) - (t->tabNoeud[index-3].m + t->tabNoeud[index-2].m + t->tabNoeud[index-1].m);
    return m;
}




/*
 * Fonction : LireNoeud
 * ---------------------
 * Construit l'arbre a partir des valeurs d'un fichier QTC lit a l'aide du buffer bitstream
 *
 * Paramètres :
 * - bitstream : Pointeur vers le BitStream contenant les données à lire.
 * - tree : Pointeur vers le QuadTree dans lequel les nœuds doivent être décodés.
 * - index : L'index du nœud actuel dans le QuadTree.
 * - niveau : Le niveau actuel du QuadTree.
 *
 * Retour :
 * - Aucun (la fonction modifie directement le QuadTree).
 */
void LireNoeud(BitStream *bitstream, QuadTree *tree, int index,int niveau) {
    if (niveau > tree->n) {
        return; 
    }
    unsigned char m, e,u;
     for(int i =0 ; i<puissance(4,niveau);i++){ 
        bool fils4 = (index % 4 == 0);
        int pere_u = tree->tabNoeud[(index - 1) / 4].u;
        if(pere_u == 1 && niveau !=0){
            tree->tabNoeud[index].m = tree->tabNoeud[(index-1)/4].m;
        }else if (niveau == 0) {
            m = readBits(bitstream, 8);
            tree->tabNoeud[index].m=m;
            e = readBits(bitstream,2);
            tree->tabNoeud[index].e =e;
            if (e == 0) {
                u = readBits(bitstream, 1);
                tree->tabNoeud[index].u=u;
            }else{
                tree->tabNoeud[index].u= 0;
            }
        }else if (niveau == tree->n) { 
            if (fils4==false) { 
                m = readBits(bitstream, 8);
                tree->tabNoeud[index].m=m;
            }else{
                m = interpolation(tree,index);
                tree->tabNoeud[index].m=m;
            }
        }else { 
            if(fils4==false) { 
                m = readBits(bitstream, 8);
                tree->tabNoeud[index].m=m;
                e = readBits(bitstream,2);
                tree->tabNoeud[index].e=e;
                if (e == 0) {
                    u = readBits(bitstream, 1);
                    tree->tabNoeud[index].u=u;
                }else{
                    tree->tabNoeud[index].u= 0;
                }
            }else { 
                m = interpolation(tree,index);
                tree->tabNoeud[index].m=m;
                e = readBits(bitstream,2);
                tree->tabNoeud[index].e=e;
                if(e == 0) {
                    u = readBits(bitstream, 1);
                    tree->tabNoeud[index].u=u;
                }else{
                    tree->tabNoeud[index].u= 0;
                }
            }
        }

    index++;
    }
    LireNoeud(bitstream, tree, index,niveau+1);
}



/*
 * Fonction : decodageQTC
 * ----------------------
 * Décode un fichier QTC et reconstruit le QuadTree en fonction des données
 * lues depuis le fichier QTC. Cette fonction initialise le QuadTree et commence le processus de lecture
 * des nœuds.
 *
 * Paramètres :
 * - nom_fichier : Le nom du fichier à lire pour récupérer le fichier QTC.
 *
 * Retour :
 * - Un pointeur vers le QuadTree construit.
 * - NULL en cas d'erreur (si le fichier n'est pas valide ou ne peut pas être ouvert).
 */
QuadTree* decodageQTC(const char *nom_fichier) {
    FILE *fichier = fopen(nom_fichier, "rb");
    if(!fichier) {
        perror("Erreur d'ouverture du fichier");
        return NULL;
    }
    char buffer[256];
    for(int i = 0; i < 3; i++){
        if(!fgets(buffer, sizeof(buffer), fichier)) {
            fprintf(stderr, "Erreur le fichier est trop court pour avoir 3 lignes\n");
            fclose(fichier);
            return NULL;
        }
    }
    QuadTree * t;
    BitStream stream;
    int niveau;
    initBitStream(&stream, fichier);
    niveau = readBits(&stream, 8);  
    t = initialiseQuadTreeSelonNiveau(niveau);
    LireNoeud(&stream, t, 0, 0);
    fclose(fichier);
    flushBitStream(&stream);
    return t;
}




