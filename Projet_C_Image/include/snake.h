
#ifndef _SNAKE_H_
    #define _SNAKE_H_
	#include <particule.h>


	typedef struct{
		Particule *head; /* la tête du serpent : interagi avec le monde extérieur */
		Particule *tail; /* la queue du serpent (sa dernière particules) */
		G2Xcolor col; /* couleur de la tête */
		double ray; /* taille de la tête */
		double beta; /* paramètre de poursuite "interne" */
	} Snake;

	/* création d’un Snake avec sa tête, sa couleur, son beta */
	void init_chain(Snake *S, Particule *head, G2Xcolor col, double ray, double beta);
	/* le Snake "absorbe" la particule p */
	void snake_eat_part(Snake *S, Particule *p);
	/* affichage du serpent */
	void snake_draw(Snake *S);
	/* déplacement du serpent */
	void snake_move(Snake *S);
	/* collision (rebond) du Snake sur les bords */
	void snake_bounce(Snake *S);
	/* le Snake poursuit le point pos (souris) */
	void snake_track(Snake *S, G2Xpoint *pos, double alpha);

#endif