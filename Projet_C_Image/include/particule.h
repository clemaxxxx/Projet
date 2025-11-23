#ifndef _PARTICULE_H_
    #define _PARTICULE_H_
    #include <g2x.h>
    #include <traj.h>
    #include <Obstacle.h>
    



    typedef struct Particule{
      G2Xpoint pos; /* poistion */
      Traj vit; /* vitesse */
      G2Xcolor col; /* couleur */
      double ray; /* rayon */
      /* d’autres paramètres suivront */
      struct Particule *prev;
      struct Particule *next;

    }Particule;

  

    void part_move(Particule *part);

    void part_bounce(Particule *part);

    void part_draw(Particule *part);

    void part_cross(Particule *part);


    bool part_pursuit(Particule *a,Particule *b, double alpha);

    bool part_track(Particule *a, G2Xpoint *pos, double alpha);

    Particule *part_create(G2Xpoint pos, Traj vit, G2Xcolor col, double ray);

    void part_insert_between(Particule *prev, Particule *new, Particule *next);

    bool part_bypass_circle(Particule *p, Obstacle *C, double omega);

    bool hunter_prey(Particule *a, double alpha, Particule *b, double beta, double d0);






#endif
