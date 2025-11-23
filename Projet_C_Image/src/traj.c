/*!==================================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - LICENCE INFORMATIQUE 3° ANNEE - Option IMAGE -
  =================================================================== */

#include <traj.h>

  /** construit un objet <Traj> à partir de 2 coord. (x,y) **/
extern Traj traj_xy(double x, double y)
{
  Traj t;
  t.u = (G2Xvector){x,y};
  t.n = g2x_Normalize(&(t.u));
  return t;
}

  /** construit un objet <Traj> à partir de 2 point A et B **/
extern Traj traj_2p(G2Xpoint A, G2Xpoint B)
{
  Traj t;
  t.u = (G2Xvector){(B.x-A.x),(B.y-A.y)};
  t.n = g2x_Normalize(&(t.u));
  return t;
}

  /** construit un objet <Traj> à partir d'un vect. u et d'une norme n **/
extern Traj traj_un(G2Xvector u, double n)
{
  Traj t;
  t.u = u;
  g2x_Normalize(&(t.u));
  t.n = n;
  return t;
}
  /** construit un objet <Traj> à partir d'un vecteur V (normé ou pas) **/
extern Traj traj_1v(G2Xvector V)
{
  Traj t;
  t.u = V;
  t.n = g2x_Normalize(&(t.u));
  return t;
}

  /** construit un vecteur à partir d'objet <Traj> V=t.n*t.u **/
extern G2Xvector traj_to_vect(Traj t)
{
  return (G2Xvector){(t.n*t.u.x),(t.n*t.u.y)};
}

  /** construit la trajectoire {(u1+u2),(n1+n2)} **/
extern Traj traj_add(Traj t1, Traj t2)
{
  Traj t3;
  t3.u=g2x_AddVect(t1.u,t2.u);
  t3.n=g2x_Normalize(&(t3.u))*(t1.n+t2.n);
  return t3;
}

  /** construit la trajectoire {(u1-u2),(n1-n2)} **/
extern Traj traj_sub(Traj t1, Traj t2)
{
  Traj t3;
  t3.u=g2x_SubVect(t1.u,t2.u);
  t3.n=g2x_Normalize(&(t3.u))*(t1.n-t2.n);
  return t3;
}

  /** calcule le produit scalaire U°V et AB°AC**/
extern double traj_prodscal(Traj t1, Traj t2)
{
  return t1.n*t2.n*g2x_ProdScal(t1.u,t2.u);
}

  /** produit G2Xvectoriel (U^V).z et (AB^AC).z  : en 2D c'est un scalaire !!!! **/
extern double traj_prodvect(Traj t1, Traj t2)
{
  return (t1.n*t2.n)*g2x_ProdVect(t1.u,t2.u);
}

  /** mapping G2Xpoint/Vecteur **/
extern Traj traj_mapscal(Traj t, double map)
{
  return (Traj){t.u,map*t.n};
}

extern Traj traj_mapvect(Traj t, G2Xcoord  map)
{
  t.u = g2x_mapvect2(t.u,map);
  t.n*= g2x_Normalize(&(t.u));
  return t;
}
