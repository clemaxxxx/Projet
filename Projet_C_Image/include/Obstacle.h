#ifndef _OBSTACLE_H_
    #define _OBSTACLE_H_
    #include <g2x.h>
    #include <time.h>
    #include <stdlib.h>

    typedef struct{
      G2Xpoint centre;
      double r;
      G2Xcolor col;
    }Obstacle;


    void obs_create(Obstacle * obs, G2Xpoint pos, G2Xcolor col, double r);

    void obs_draw(Obstacle *obs);

    bool chevauchement(Obstacle * obs,Obstacle obstacles[], int indice);

    void obs_generate(Obstacle*obs,Obstacle obstacles[],double margin,int indice);















#endif
