#include <snake.h>


void init_chain(Snake *S, Particule *head, G2Xcolor col, double ray, double beta){
	S->head = head;
	S->tail = head;
	S->col = col;
	S->beta = beta;
	S->ray = ray;

}


void snake_eat_part(Snake *S, Particule *p){
	p->col = S->tail->col;
	p->ray = S->tail->ray - 0.1;
	p->vit.u.x = 0;
	p->vit.u.y = 0;
	p->vit.n = 0;
	p->pos = S->tail->pos;
	part_insert_between(S->tail,p,NULL);
	S->tail = p;
}


void snake_draw(Snake *S){
	Particule * tmp = S->head;
	while(tmp!=NULL){
		part_draw(tmp);
		tmp = tmp->next;
	}
}


void snake_move(Snake *S){
	part_move(S->head);
	Particule *tmp = S->head->next;
	while(tmp!=NULL){
		part_pursuit(tmp,tmp->prev,S->beta);
		part_move(tmp);
		tmp= tmp->next;
	}

}

void snake_bounce(Snake *S){
	part_bounce(S->head);

}

void snake_track(Snake *S, G2Xpoint *pos, double alpha){
	part_track(S->head,pos,alpha);
}

