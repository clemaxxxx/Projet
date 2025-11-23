#ifndef _CHAIN_H_
    #define _CHAIN_H_
    #include <g2x.h>
    #define VREF 0.15
    #include <g2x.h>
    #include <particule.h>
	#include <snake.h>

	
	typedef struct{
		Particule *head;
	}Chain;

	void eat(Chain * hunter,Chain * prey, Particule * miam);

	void init_prey(Chain * preys,int n);
	void draw_Chain(Chain * preys);
	void Chain_bounce(Chain *preys);
	void Chain_move(Chain *preys);
	void Chain_bypass_circle(Chain*Preys,Obstacle * obs,double omega);



#endif
