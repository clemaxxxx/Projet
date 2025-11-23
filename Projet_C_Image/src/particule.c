#include <particule.h>


void part_move(Particule *part){
  part->pos.x += part->vit.n * part->vit.u.x;
  part->pos.y += part->vit.n * part->vit.u.y;
}

void part_bounce(Particule *part){
  if (part->pos.x <= g2x_GetXMin()) {part->vit.u.x = part->vit.u.x * -1;}
  if (part->pos.y <= g2x_GetYMin()) {part->vit.u.y = part->vit.u.y * -1;}
  if (part->pos.x >= g2x_GetXMax()) {part->vit.u.x = part->vit.u.x * -1;}
  if (part->pos.y >= g2x_GetYMax()) {part->vit.u.y = part->vit.u.y * -1;}
}


void part_draw(Particule *part){
  g2x_Plot(part->pos.x, part->pos.y, part->col, part->ray);
}

 void part_cross(Particule *part){
   if (part->pos.x < g2x_GetXMin()) {part->pos.x = g2x_GetXMax();}
   if (part->pos.y < g2x_GetYMin()) {part->pos.y = g2x_GetYMax();}

   if (part->pos.x > g2x_GetXMax()) {part->pos.x = g2x_GetXMin();}
   if (part->pos.y > g2x_GetYMax()) {part->pos.y = g2x_GetYMin();}

 }

bool part_pursuit(Particule *a,Particule *b, double alpha){
  G2Xvector pa_pb = g2x_Vector2p(a->pos,b->pos);
  Traj ab = traj_1v(pa_pb);
  /*if(ab.n < (a->ray+b->ray)){
    return false;
  }*/
  G2Xvector Va;
  double va_x = (1. - alpha)  * a->vit.u.x + alpha  * ab.u.x;
  double va_y = (1. - alpha)  * a->vit.u.y + alpha  * ab.u.y;
  //double va_x = (1. - alpha) * a->vit.n * a->vit.u.x + alpha * b->vit.n * ab.u.x;
  //double va_y = (1. - alpha) * a->vit.n * a->vit.u.y + alpha * b->vit.n * ab.u.y;
  Va.x = va_x;
  Va.y = va_y;
  a->vit.n =   (1. - alpha)  * a->vit.n + alpha  * b->vit.n;
  /*a->vit.n =*/ g2x_Normalize(&Va);
  //a->vit.u.x = (1.00/a->vit.n) * Va.x;
  //a->vit.u.y = (1.00/a->vit.n) * Va.y;
  a->vit.u = Va;
  return true;
}

bool part_track(Particule *a, G2Xpoint *pos, double alpha){
  G2Xvector PAM = g2x_Vector2p(a->pos, *pos);
  Traj am = traj_1v(PAM);
  /*if(alpha == 0 ||  am.n < 2*(a->ray)){
    return false;
  }*/
  G2Xvector Ua;
  double ua_x = (1. - alpha) * a->vit.u.x + alpha * am.u.x;
  double ua_y = (1. - alpha) * a->vit.u.y + alpha * am.u.y;
  Ua.x = ua_x;
  Ua.y = ua_y;

  a->vit.u = g2x_NormalVector(Ua);
  return true;

}


Particule * part_create(G2Xpoint pos, Traj vit, G2Xcolor col, double ray){
  Particule* A = (Particule*)malloc(sizeof(Particule));
  if(A==NULL){
    printf("Erreur d'allocation");
    return NULL;
  }
  A->pos = pos;
  A->vit = vit;
  A->col = col;
  A->ray = ray;
  A->next = NULL;
  A->prev = NULL;
  return A;
}

void part_insert_between(Particule *prev, Particule *new, Particule *next){
  if(prev!=NULL){
    prev->next = new;
  }
  new->prev = prev;
  new->next = next;
  if(next!=NULL){
    next->prev=new;
  }
}

bool part_bypass_circle(Particule *p, Obstacle *C, double omega){
  /*G2Xvector AC = g2x_Vector2p(p->pos,C->centre);
  double t = g2x_ProdScal(AC, p->vit.u);
  AC.x = -AC.x;
  AC.y = -AC.y;
  double d = -g2x_ProdVect(AC, p->vit.u);
  if(t<0 || d > C->r){
    return false;
  }
  double alpha = pow((C->r/t),omega);
  p->vit.u.x = (1-alpha) * p->vit.u.x + alpha * 1;
  p->vit.u.y = (1-alpha) * p->vit.u.y + alpha * 1;
  g2x_Normalize(&p->vit.u);
  return true;*/

  Traj AC = traj_2p(p->pos,C->centre);
  double t = AC.n * g2x_ProdScal(p->vit.u,AC.u); // Distance de A a p, proj de C sur Au
  double d = AC.n * g2x_ProdVect(p->vit.u,AC.u); //Distance de C a P
  if(t<0 || fabs(d) > C->r){
    return false;
  }
  G2Xpoint P;
  double alpha = pow((C->r/t),omega);
  P.x = p->pos.x + t* p->vit.u.x;
  P.y = p->pos.y + t* p->vit.u.y;
  G2Xvector CP = g2x_Vector2p(P,C->centre);
  p->vit.u.x = (1.-alpha) * p->vit.u.x + alpha * (CP.x/d);
  p->vit.u.y = (1.-alpha) * p->vit.u.y + alpha * (CP.y/d);
  g2x_Normalize(&p->vit.u);
  return true;
}


bool hunter_prey(Particule *a, double alpha, Particule *b, double beta, double d0) {
    Traj ab = traj_2p(a->pos, b->pos);
    double distance = ab.n; 
    if (distance > d0) {
        return false;  
    }
    double gamma_a = alpha / (1. + distance*distance);
    double gamma_b = beta  / (1. + distance*distance);

    a->vit.u.x = (1. - gamma_a) * a->vit.n * a->vit.u.x + gamma_a * b->vit.n * (ab.u.x / distance);
    a->vit.u.y = (1. - gamma_a) * a->vit.n * a->vit.u.y + gamma_a * b->vit.n * (ab.u.y / distance);

    b->vit.u.x = (1. - gamma_b) * b->vit.n * b->vit.u.x - gamma_b * a->vit.n * (ab.u.x / distance);
    b->vit.u.y = (1. - gamma_b) * b->vit.n * b->vit.u.y - gamma_b * a->vit.n * (ab.u.y / distance);

    a->vit.n = fmax(a->vit.n,(1. - gamma_a) * a->vit.n + gamma_a * b->vit.n);
    b->vit.n = fmax(b->vit.n,(1. - gamma_b) * b->vit.n + gamma_b * a->vit.n);

    g2x_Normalize(&a->vit.u);  
    g2x_Normalize(&b->vit.u);

    return true;  
}