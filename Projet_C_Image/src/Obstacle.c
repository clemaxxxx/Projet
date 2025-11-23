#include <Obstacle.h>



void obs_create(Obstacle * obs, G2Xpoint pos, G2Xcolor col, double r){
  obs->centre = pos;
  obs->col = col;
  obs->r = r;
}

void obs_draw(Obstacle *obs){
  g2x_DrawFillCircle(obs->centre, obs->r, obs->col);
}

bool chevauchement(Obstacle * obs,Obstacle obstacles[], int indice){
  for(int i=0; i<indice; i++){
    double distance = g2x_Dist(obs->centre,obstacles[i].centre);
    if(distance< (obs->r + obstacles[i].r)){
      return true;
    }
  }
  return false;
}

static double RandDouble(double min, double max){
  return(double)rand()/(RAND_MAX)*(max-min)+min;
}

void obs_generate(Obstacle*obs,Obstacle obstacles[],double margin,int indice){
   do 
   {
    obs->centre = g2x_RandPoint(margin);
    obs->r = RandDouble(1,4);
    obs->col = G2Xr;
   } while(chevauchement(obs,obstacles,indice));
}
