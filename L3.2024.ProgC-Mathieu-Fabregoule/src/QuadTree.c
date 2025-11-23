#include "QuadTree.h"
/*
 * Module : QuadTree
 * ------------------
 * Ce module gère la création, manipulation et filtrage de structures QuadTree.
 * Le module inclut des fonctions pour initialiser, libérer, calculer des propriétés, 
 * et appliquer des filtres à un QuadTree.
 */


/*
 * Fonction : initialiseQuadTreeSelonNiveau
 * -----------------------------------------
 * Initialise un QuadTree pour un niveau donné.
 *
 * Paramètre :
 * - niveau : Profondeur maximale du QuadTree.
 *
 * Retourne un pointeur vers une structure QuadTree initialisée.
 */
QuadTree* initialiseQuadTreeSelonNiveau(int niveau){
    int nbNoeud = nbNoeuds(niveau);
    QuadTree *quadtree = (QuadTree*)malloc(sizeof(QuadTree));
    if(quadtree == NULL) {
        fprintf(stderr, "Erreur Malloc Quadtree");
    }
    quadtree->tabNoeud = (Noeud*)malloc(nbNoeud * sizeof(Noeud));
    if(quadtree->tabNoeud == NULL) {
        fprintf(stderr, "Erreur d'allocation de mémoire Noeud\n");
        free(quadtree);  
        exit(1); 
    }
    quadtree->n = niveau;
    for(int i = 0; i<nbNoeud; i++){
        quadtree->tabNoeud[i].e = 0;
        quadtree->tabNoeud[i].m = 0;
        quadtree->tabNoeud[i].u = 1;
    }
    quadtree->n = niveau;
    return quadtree;
}


/*
 * Fonction : nbNoeuds
 * --------------------
 * Calcule le nombre total de nœuds dans un QuadTree en fonction de son niveau.
 *
 * Paramètre :
 * - niveau : Profondeur maximale du QuadTree.
 *
 * Retourne le nombre total de nœuds.
 */
int nbNoeuds(int niveau) {
    int NombreFinal = 0;
    int puissance = 1;
    for (int i = 0; i <= niveau; i++) {
        NombreFinal += puissance;
        puissance *= 4;
    }
    return NombreFinal;
}

/*
 * Fonction : calculerNiveauMax
 * ----------------------------
 * Calcule la profondeur maximale du QuadTree en fonction des dimensions d'une image.
 *
 * Paramètres :
 * - ligne : Nombre de lignes dans l'image.
 * - colonne : Nombre de colonnes dans l'image.
 *
 * Retourne la profondeur maximale possible.
 */
int calculerNiveauMax(int ligne, int colonne) {
    int dimensionMax = (ligne > colonne) ? ligne : colonne;
    int niveauMax = 0;
    while ((1 << niveauMax) <= dimensionMax) {
        niveauMax++;
    }
    return niveauMax - 1;
}


/*
 * Fonction : initialiseQuadTree
 * ------------------------------
 * Initialise un QuadTree en fonction des dimensions d'une image.
 *
 * Paramètres :
 * - ligne : Nombre de lignes dans l'image.
 * - colonne : Nombre de colonnes dans l'image.
 *
 * Retourne un pointeur vers une structure QuadTree initialisée.
 */
QuadTree* initialiseQuadTree(int ligne,int colonne){
    int niveau = calculerNiveauMax(ligne,colonne);
    int nbNoeud = nbNoeuds(niveau);
    QuadTree *quadtree = (QuadTree*)malloc(sizeof(QuadTree));
    if(quadtree == NULL) {
        fprintf(stderr, "Malloc rate");
    }
    quadtree->tabNoeud = (Noeud*)malloc(nbNoeud * sizeof(Noeud));
    if(quadtree->tabNoeud == NULL) {
        fprintf(stderr, "Erreur d'allocation de mémoire Noeud\n");
        free(quadtree);  
        exit(1); 
    }
    quadtree->n = niveau;
    for(int i = 0; i<nbNoeud; i++){
        quadtree->tabNoeud[i].e = 0;
        quadtree->tabNoeud[i].m = 0;
        quadtree->tabNoeud[i].u = 1;
    }
    quadtree->n = niveau;
    return quadtree;

}


/*
 * Fonction : libererQuadTree
 * --------------------------
 * Libère la mémoire allouée pour un QuadTree.
 *
 * Paramètre :
 * - quadtree : Pointeur vers le QuadTree à libérer.
 *
 * Retour :
 * - Aucun.
 */
void libererQuadTree(QuadTree* quadtree){
    if (quadtree != NULL) {
        free(quadtree);
    }
}


/*
 * Fonction : calculer_variance
 * ----------------------------
 * Calcule la variance pour un nœud donné du QuadTree.
 *
 * Paramètres :
 * - t : Pointeur vers le QuadTree.
 * - index : Index du nœud dans le tableau de nœuds.
 *
 * Retourne la variance calculée pour le nœud.
 */
static float calculer_variance(QuadTree* t, int index) {
    unsigned char m = t->tabNoeud[index].m;
    float variance = 0.0f;
    float somme_diff_carre = 0.0f;

    for (int i = 0; i < 4; i++) {
        int fils_index = 4 * index + i;
        unsigned char m_fils = t->tabNoeud[fils_index].m;
        float variance_fils = t->tabNoeud[fils_index].variance;
        somme_diff_carre += (m - m_fils) * (m - m_fils);
        variance += variance_fils * variance_fils;
    }
    variance = sqrt((variance + somme_diff_carre) / 4.0f);
    t->tabNoeud[index].variance = variance;
    return variance;
}


/*
 * Fonction : est_feuille
 * ----------------------
 * Vérifie si un nœud donné est une uniforme.
 *
 * Paramètres :
 * - tree : Pointeur vers le QuadTree.
 * - index : Index du nœud.
 *
 * Retourne 1 si le nœud est uniforme, 0 sinon.
 */
static int est_feuille(QuadTree* tree, int index) {
    int max_nodes = nbNoeuds(tree->n);  
    if (index >= max_nodes) {
        printf("Erreur  index %d est hors limites.\n", index);
        return 0;  
    }
    for (int i = 0; i < 4; i++) {
        int childIndex = 4 * index + i;
        if (childIndex < max_nodes && tree->tabNoeud[childIndex].u == 0) {
            return 0;
        }
    }
    return 1;
}


/*
 * Fonction : mettre_a_jour_variances
 * -----------------------------------
 * Met à jour les variances pour tous les nœuds d'un QuadTree.
 *
 * Paramètre :
 * - t : Pointeur vers le QuadTree.
 *
 * Retour :
 * - Aucun.
 */
static void mettre_a_jour_variances(QuadTree *t) {
    for (int i = 0; i < nbNoeuds(t->n); i++) {
        if (!est_feuille(t, i)) {
            calculer_variance(t, i);
        }
    }
}



/*
 * Fonction : getAverageMaxVariance
 * ---------------------------------
 * Calcule la variance maximale et moyenne pour tous les nœuds d'un QuadTree.
 *
 * Paramètres :
 * - qt : Pointeur vers le QuadTree.
 * - max : Pointeur pour stocker la variance maximale.
 * - average : Pointeur pour stocker la variance moyenne.
 *
 * Retour :
 * - Aucun.
 */
static void getAverageMaxVariance(QuadTree *qt, double *max, double *average) {
  size_t numNodes = nbNoeuds(qt->n - 1);
  if (numNodes == 0) return;

  *max = 0.;
  *average = 0.;
  for (size_t i = 0; i < numNodes; i++) {
    Noeud *node = &qt->tabNoeud[i];
    *average += node->variance;
    if (node->variance > *max) {
      *max = node->variance;
    }
  }

  *average /= numNodes;
}


/*
 * Fonction : filterQuadTree
 * --------------------------
 * Filtre les nœuds d'un QuadTree en fonction de critères définis (alpha et beta).
 *
 * Paramètres :
 * - qt : Pointeur vers le QuadTree.
 * - index : Index du nœud à traiter.
 * - sigma, alpha, beta : Paramètres de filtrage.
 *
 * Retourne 1 si le nœud est uniformisé, 0 sinon.
 */
static int filterQuadTree(QuadTree *qt, size_t index, double sigma, double alpha, double beta) {
    if (qt->tabNoeud[index].e == 0 && qt->tabNoeud[index].u == 1) {
        return 1;
    }

    size_t childIndex = 4 * index + 1;
    unsigned char uniformize = 1;
    for (size_t i = 0; i < 4; i++) {
        uniformize &= filterQuadTree(qt, childIndex + i, sigma * alpha, pow(alpha, beta), beta);
    }
    if (!uniformize || qt->tabNoeud[index].variance > sigma) {
        return 0;
    }
    qt->tabNoeud[index].e = 0;
    qt->tabNoeud[index].u = 1;
    return 1;
}




/*
 * Fonction : filterQuadTree_G
 * ----------------------------
 * Applique un filtrage global au QuadTree en fonction des paramètres alpha et beta.
 *
 * Paramètres :
 * - qt : Pointeur vers le QuadTree.
 * - alpha : Facteur d'intensité.
 * - beta : Facteur d'ajustement.
 *
 * Retour :
 * - Aucun.
 */
void filterQuadTree_G(QuadTree *qt, double alpha, double beta) {
  double maxVar, medVar;
  mettre_a_jour_variances(qt);
  getAverageMaxVariance(qt, &maxVar, &medVar);
  filterQuadTree(qt, 0, medVar / maxVar, alpha, beta);
}