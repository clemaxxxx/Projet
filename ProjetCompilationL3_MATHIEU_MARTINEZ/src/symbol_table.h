#ifndef SYMBOL_TABLE_H
#define SYMBOL_TABLE_H
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tree.h"
#define MAX_SYMBOLS 100  


typedef enum {
    VAR_GLOBAL,    
    VAR_LOCAL,     
    FUNC           
} SymbolType;

typedef enum {
    INT,
    CHAR,
    VOOID,
    DOUUBLE
} VarType;


typedef struct Symbol {
    char *name;             
    SymbolType type;   
    VarType var_type;     
    int address;            
    struct Symbol *next;
    int est_static;
} Symbol;

typedef struct SymbolTable {
    Symbol *tab[MAX_SYMBOLS];
} SymbolTable;


unsigned int hachage(const char *name);
SymbolTable *CreateTable();
Symbol *FindSymbol(SymbolTable *table, const char *name);
void AddSymbol(SymbolTable *table, const char *name, SymbolType type, VarType var_type, int address, int est_static);
void FreeTable(SymbolTable *table);
void PrintTable(SymbolTable *table);
SymbolTable *SymbolTableFromTree(Node *root); 

#endif 