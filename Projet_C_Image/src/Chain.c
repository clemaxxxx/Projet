#include <Chain.h>



void eat(Chain * hunter,Chain * prey, Particule * miam){
	if(prey->head == miam){
		prey->head = miam->next;
	}

	if(miam->prev){
		miam->prev->next = miam->next;
	}
	if(miam->next){
		miam->next->prev = miam->prev;
	}

	Particule * tmp = hunter->head;
	while(tmp->next != NULL){
		tmp = tmp->next;
	}
	tmp->next = miam;
	miam->prev = tmp;
	miam->next = NULL;
}

void init_prey(Chain * preys,int n){
	preys->head = NULL;
	for(int i =0; i<n; i++){
		G2Xvector va = g2x_RandVector(VREF);
		Particule * a = part_create(g2x_RandPoint(0),traj_un(va,VREF),G2Xr,5);
		a->next = preys->head;
		a->prev = NULL;
		if(preys->head){
			preys->head->prev =a;
		}
		preys->head = a;
	}
}

void draw_Chain(Chain * preys){
	Particule *courant = preys->head;
	while(courant!=NULL){
		part_draw(courant);
		courant = courant->next;
	}
}

void Chain_move(Chain *preys){
	Particule * tmp = preys->head;
	while(tmp!=NULL){
		part_move(tmp);
		tmp= tmp->next;
	}
}
void Chain_bounce(Chain *preys){
	Particule *tmp = preys->head;
	while(tmp!=NULL){
		part_bounce(tmp);
		tmp= tmp->next;
	}
}

void Chain_bypass_circle(Chain*Preys,Obstacle * obs,double omega){
	Particule *tmp = Preys->head;
	while(tmp!=NULL){
		part_bypass_circle(tmp,obs,omega);
		tmp=tmp->next;
	}
}

