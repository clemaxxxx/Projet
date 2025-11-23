/*!=================================================================!*/
/*!= E.Incerti - eric.incerti@univ-eiffel.fr                       =!*/
/*!= Université Gustave Eiffel                                     =!*/
/*!= Code exemple pour prototypage avec libg2x.6e (2023)           =!*/
/*   => Base de départ pour tout programme utilisant <libg2x>        */
/*!=================================================================!*/

#include <g2x.h>

/* dimension fenêtre (pixels) */
static int WWIDTH=512, WHEIGHT=512;
/* zone de travail réelle */
static double wxmin=-10.,wymin=-10.,wxmax=+10.,wymax=+10.;

/* fonction d'initialisation */
static void init(void) { }

/* fonction de contrôle */
static void ctrl(void) { }

/* fonction de contrôle */
static void evts(void) { }

/* fonction de dessin   */
static void draw(void) { }

/* fonction d'animation */
static void anim(void) { }

/* fonction de sortie   */
static void quit(void) { }

/************************/
/* fonction principale  */
/************************/
int main(int argc, char **argv)
{
  g2x_InitWindow(*argv,WWIDTH,WHEIGHT);
  g2x_SetWindowCoord(wxmin,wymin,wxmax,wymax);

  /*  fonctions -> handlers  */
  g2x_SetInitFunction(init); /* fonction d'initialisation */
  g2x_SetCtrlFunction(ctrl); /* fonction de contrôle      */
  g2x_SetEvtsFunction(evts); /* fonction d'événements     */
  g2x_SetDrawFunction(draw); /* fonction de dessin        */
  g2x_SetAnimFunction(anim); /* fonction d'animation      */
  g2x_SetExitFunction(quit); /* fonction de sortie        */

  /* boucle principale */
  return g2x_MainStart();
}
