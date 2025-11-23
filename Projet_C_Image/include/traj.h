/*!==================================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - LICENCE INFORMATIQUE 3° ANNEE - Option IMAGE -
  =================================================================== */

#ifndef _PATH_H_
  #define _PATH_H_

  #include <g2x.h>

  /******************************************************************
   ** PRIMITIVES GEOMETRIQUES ET FONCTIONS UTILES                  **/
  /******************************************************************
   * G2Xcoordonnees XY : G2Xpoint & G2Xvector
   ******************************************************************/

  typedef struct
  {
    G2Xvector u; /* direction -- toujours de norme 1 */
    double    n; /* norme     -- toujours positive   */
  }
  Traj;

  /** construit un objet <Traj> à partir de 2 coord. (x,y) **/
  Traj   traj_xy(double x, double y);

  /** construit un objet <Traj> à partir de 2 point A et B **/
  Traj   traj_2p(G2Xpoint A, G2Xpoint B);

  /** construit un objet <Traj> à partir d'un vect. u et d'une norme n **/
  Traj   traj_un(G2Xvector u, double n);

  /** construit un objet <Traj> à partir d'un vecteur V **/
  Traj   traj_1v(G2Xvector V);

  /** construit un vecteur à partir d'objet <Traj> V=t.n*t.u **/
  G2Xvector traj_to_vect(Traj V);

  /** construit la trajectoire {(u1+u2),(n1+n2)} **/
  Traj   traj_add(Traj t1, Traj t2);

  /** construit la trajectoire {(u1-u2),(n1-n2)} **/
  Traj   traj_sub(Traj t1, Traj t2);

  /** calcule le produit scalaire t1°t2 **/
  double traj_prodscal(Traj t1, Traj t2);

  /** produit vectoriel (t1^t2).z : en 2D c'est un scalaire !!!! **/
  double traj_prodvect(Traj t1, Traj t2);

  /** mapping **/
  Traj   traj_mapscal(Traj T, double map);
  Traj   traj_mapvect(Traj T, G2Xcoord  map);


#endif
