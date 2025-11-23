#include "semantique.h"


void check_error(Node * root,SymbolTable * t){
    if(root==NULL) return;
    if (root->label == egalite){
        check_egalite(root, t);
    }

    if(root->label ==  divstar){
        check_divstar(root);
    }
    if(root->label == DECLFONCT ){
        char * f = root->firstChild->firstChild->value;
        if(strcmp(f,"main") !=0){
            Symbol * function = FindSymbol(t,f);
            check_return(root,t,function);
        }
    }
    if(root->label == CORPS){
        check_Double_ident(root);
    }
    if(root->label == PROG){
        check_Double_ident(root);
    }

    if (root->label == divstar || root->label == addsub) {
        check_operation(root, t);
    }
    if(root->label == iF){
        check_if(root);
    }
    if(root->label == While){
        check_while(root);
    }

    check_error(root->firstChild,t);
    check_error(root->nextSibling,t);
}



void check_divstar(Node * root){
    if(root->label == divstar && (strcmp(root->value,"/") == 0)){
        Node * r = root->firstChild;
        Node * l = r->nextSibling;
        if(recup_value(r) == 0 || recup_value(l) == 0){
            fprintf(stderr,"Warning Division par 0\n");
            return ;
        }

    }

}

void check_return(Node *root, SymbolTable *t, Symbol *f) {
    if (root == NULL || f == NULL) return;

    if (root->label == Return) {
        VarType expected_type = f->var_type;
        Node *r = root->firstChild;


        if (expected_type == VOOID && r != NULL) {
            fprintf(stderr, "Erreur Semantique Return : return avec valeur dans une fonction void\n");
            exit(2);
        }


        if (expected_type != VOOID && r == NULL) {
            fprintf(stderr, "Erreur Semantique Return : fonction doit retourner une valeur\n");
            exit(2);
        }

        if (r != NULL) {
            if ((expected_type == INT || expected_type == DOUUBLE) && r->label == character) {
                fprintf(stderr, "Erreur Semantique Return : return char alors que la fonction retourne int/double\n");
                exit(2);
            }

            if (expected_type == CHAR && r->label == num) {
                fprintf(stderr, "Warning Return : return int alors que la fonction retourne char\n");
                return;
            }


            if (r->label == ident) {
                Symbol *returned_sym = FindSymbol(t, r->value);
                if (!returned_sym) {
                    fprintf(stderr, "Erreur Semantique Return : variable %s non déclarée\n", r->value);
                    return ;
                }
                if (returned_sym->var_type != expected_type) {
                    fprintf(stderr, "Erreur Semantique Return : mauvais type retourné par la variable %s\n", r->value);
                    exit(2);
                }
            }
        }
    }

    check_return(root->firstChild, t, f);
    check_return(root->nextSibling, t, f);
}


void check_Double_ident(Node * root){
    Node * instr = root->firstChild;
    char * tab[100];
    int i = 0;
    while(instr->label == DECLVARS){
        char * ident = instr->firstChild->value;
        for (int j = 0; j < i; j++) {
            if (strcmp(tab[j], ident) == 0) {
                    fprintf(stderr, "Erreur Semantique : identifiant '%s' déclaré plusieurs fois dans le même bloc\n", ident);
                    exit(2);
            }
        }
        tab[i++] = ident;
        instr = instr->nextSibling;
    }

}


void check_operation(Node *root, SymbolTable *table) {
    if (root == NULL) return;

    if (root->label == addsub || root->label == divstar) {
        Node *left = FIRSTCHILD(root);
        Node *right = SECONDCHILD(root);
        if(left == NULL || right == NULL){
            return ;
        }

        VarType left_type = -1;
        if (left->label == ident) {
            Symbol *symL = FindSymbol(table, left->value);
            if (symL) {
                left_type = symL->var_type;
            } else {
                fprintf(stderr, "Erreur Semantique : Identifiant '%s' non déclaré.\n", left->value);

            }
        } else if (left->label == num) {
            left_type = INT;
        } else if (left->label == character) {
            left_type = CHAR;
        }

        VarType right_type = -1;
        if (right->label == ident) {
            Symbol *symR = FindSymbol(table, right->value);
            if (symR) {
                right_type = symR->var_type;
            } else {
                fprintf(stderr, "Erreur Semantique : Identifiant '%s' non déclaré.\n", right->value);

            }
        } else if (right->label == num) {
            right_type = INT;
        } else if (right->label == character) {
            right_type = CHAR;
        }


        if (left_type == INT && right_type != INT) {
            fprintf(stderr, "Erreur Semantique : Opération arithmétique avec mauvais type.\n");

        }

        if (left_type != INT && right_type == INT) {
            fprintf(stderr, "Erreur Semantique : Opération arithmétique avec mauvais type.\n");

        }
    }


    check_operation(root->firstChild, table);
    check_operation(root->nextSibling, table);
}


void check_egalite(Node *root, SymbolTable *t) {
    if (root == NULL) return;

    if (root->label == egalite) {
        Node *left = root->firstChild;
        Node *right = root->firstChild ? root->firstChild->nextSibling : NULL;

        if (!left || !right) return;
        if (left->label != ident) return;

        Symbol *sym = FindSymbol(t, left->value);
        if (!sym) return;

        VarType type = sym->var_type;

        if ((type == INT || type == DOUUBLE) && right->label == character) {
            fprintf(stderr, "Warning Egalite : int = char \n");
        }
        if ((type == CHAR) && right->label == num) {
            fprintf(stderr, "Erreur Semantique Egalite : char = int\n");
            exit(2);
        }
        if(right->label == divstar || right->label == addsub){
            return;
        }
    }

    check_egalite(root->firstChild, t);
    check_egalite(root->nextSibling, t);
}
void check_if(Node * node){
    if(node == NULL) return;

    if(node->label == iF){
        Node * f = node->firstChild;
        if(f==NULL){
            exit(3);
        }
        if(f->label == num){
            fprintf(stderr,"Erreur num dans un if (Pas mis en place)\n");
            return ;
        }
        if(f->label != order && f->label !=eq && f->label != or && f->label != and){
            fprintf(stderr,"Erreur Semantique if : Pas une expression boolean dans le if\n");
            return ;
        }

    }

}

void check_while(Node * node){
    if(node == NULL) return;

    if(node->label == While){
        Node * f = node->firstChild;
        if(f==NULL){
            exit(3);
        }
        if(f->label == num){
            fprintf(stderr,"Erreur boucle infinis\n");
            return ;
        }
        if(f->label != order && f->label !=eq && f->label != or && f->label != and){
            fprintf(stderr,"Erreur Semantique while : Pas une expression boolean dans le if\n");
            return ;
        }

    }

}
