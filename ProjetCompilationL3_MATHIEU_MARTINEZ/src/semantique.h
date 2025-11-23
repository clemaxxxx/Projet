#ifndef SEMANTIQUE_H
#define SEMANTIQUE_H
#include "tree.h"
#include "symbol_table.h"
#include "Traduction.h"
#include <stdio.h>
#include <stdlib.h>

void check_error(Node * root,SymbolTable * t);
void check_divstar(Node * root);
void check_return(Node * root,SymbolTable * t,Symbol * f);
void check_Double_ident(Node * root);
void check_operation(Node *root, SymbolTable *table);
void check_egalite(Node *root, SymbolTable *t);
void check_if(Node * node);
void check_while(Node * node);

#endif 