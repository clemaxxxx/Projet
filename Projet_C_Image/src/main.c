#include <g2x.h>
#include <particule.h>
#include <snake.h>
#include <Obstacle.h>
#include <Chain.h>

/* dimension fenêtre (pixels) */
static int WWIDTH=512, WHEIGHT=512;
/* zone de travail réelle */
static double wxmin=-10.,wymin=-10.,wxmax=+10.,wymax=+10.;
#define VREFH 0.1
bool FLAG;
#define NB_OBSTACLES 10
#define NB_PARTICULES 10

G2Xpoint *mouse = NULL;

double alpha = 0.30;
double omega = 30;
double beta = 0.25;
Particule * A;
Particule *test;

Snake S;

Obstacle obs[NB_OBSTACLES];

Chain hunter;
Chain Preys;


/* fonction d'initialisation */
static void init(void){
  G2Xvector va = g2x_RandVector(VREFH);
  hunter.head = part_create(g2x_RandPoint(1),traj_un(va,VREFH),G2Xb,5);
  init_chain(&S,hunter.head,G2Xb,5,alpha);

  init_prey(&Preys,NB_PARTICULES);

  for(int i=0; i<NB_OBSTACLES; i++){
    obs_generate(&obs[i],obs,1,i);
  }
  test = Preys.head;




 }

/* fonction de contrôle */
static void ctrl(void){
  g2x_CreateSwitch("Deplacement",&FLAG, "");
  g2x_CreateScrollv_d("alpha", &alpha, 0.00, 0.45, "poursuite");
  mouse = NULL;
  if(g2x_MouseInWindow(.1)){
    mouse = g2x_GetMousePosition();
  }

}

/* fonction de contrôle */
static void evts(void) {

  }


/* fonction de dessin   */
static void draw(void){
  snake_draw(&S);
  draw_Chain(&Preys);
  for(int i =0; i<NB_OBSTACLES;i++){
    obs_draw(&obs[i]);
  }
}

/* fonction d'animation */
static void anim(void){
  if(mouse != NULL && FLAG){
    snake_track(&S,mouse,alpha);
  }
  for(int i=0; i<NB_OBSTACLES; i++){
    part_bypass_circle(S.head,&obs[i],omega);
    Chain_bypass_circle(&Preys,&obs[i],omega);
  }
  eat(&hunter,&Preys,test);

  Chain_bounce(&Preys);
  snake_bounce(&S);

  Chain_move(&Preys);
  snake_move(&S);

}

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
  g2x_SetExitFunction(NULL); /* fonction de sortie        */

  /* boucle principale */
  return g2x_MainStart();
}