#ifndef TRADUCTION_H
#define TRADUCTION_H
#include "tree.h"
#include "symbol_table.h"
#include <stdio.h>
#include <stdlib.h>


extern FILE * f;
void open_file();
void close_file();
void Translate(Node *node,SymbolTable * t);
void translate_egalite(Node * node,SymbolTable * t);
void translate_expr(Node *node,SymbolTable * t);
void translate_if(Node* node,SymbolTable * t);
void translate_expr_bool(Node * node, int current_label,SymbolTable * t);
void translate_while(Node * node,SymbolTable * t);
const char* recup_size( char * name,SymbolTable * t);
int recup_value(Node *n);
void translate_return(Node * node, SymbolTable * t);
void translate_expr_bool_while(Node *node, int current_label, SymbolTable *t);
void translate_main_body(Node *node,SymbolTable * t,int cpt);
void translate_global(Node * node);
#endif 