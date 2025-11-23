#include "symbol_table.h"

#define HASH_CONST 613


unsigned int hachage(const char *name) {
    unsigned int h = 0;
    while (*name) {
        h = (h * HASH_CONST + *name) % MAX_SYMBOLS;
        name++;
    }
    return h;
}

SymbolTable *CreateTable() {
    SymbolTable *table = (SymbolTable *)malloc(sizeof(SymbolTable));
    if (!table) {
        fprintf(stderr, "Erreur : malloc échoué pour SymbolTable\n");
        exit(EXIT_FAILURE);
    }
    memset(table->tab, 0, sizeof(table->tab));
    return table;
}

Symbol *FindSymbol(SymbolTable *table, const char *name) {
    unsigned int index = hachage(name);
    Symbol *current = table->tab[index];
    while (current) {
        if (strcmp(current->name, name) == 0) {
            return current;
        }
        current = current->next;
    }
    return NULL;
}

void AddSymbol(SymbolTable *table, const char *name, SymbolType type, VarType var_type, int address, int est_static) {
    if (FindSymbol(table, name)) {
        printf("Erreur : symbole '%s' déjà présent\n", name);
        return;
    }

    Symbol *new_symbol = (Symbol *)malloc(sizeof(Symbol));
    if (!new_symbol) {
        fprintf(stderr, "Erreur : malloc échoué pour Symbol\n");
        exit(EXIT_FAILURE);
    }

    new_symbol->name = strdup(name);
    new_symbol->type = type;
    new_symbol->var_type = var_type;
    new_symbol->address = address;
    new_symbol->est_static = est_static;

    unsigned int index = hachage(name);
    new_symbol->next = table->tab[index];
    table->tab[index] = new_symbol;


}

void FreeTable(SymbolTable *table) {
    for (int i = 0; i < MAX_SYMBOLS; i++) {
        Symbol *courant = table->tab[i];
        while (courant) {
            Symbol *tmp =courant;
            courant= courant->next;
            free(tmp->name);
            free(tmp);
        }
    }
    free(table);
}

void PrintTable(SymbolTable * t){
    if (t == NULL) {
        printf("Erreur : Table des symboles NULL\n");
        return;
    }
    printf("Symbol Table  \n");
    for(int i=0; i<MAX_SYMBOLS; i++){
        Symbol * courant = t->tab[i];
        while(courant){
            char * name = courant->name;
            SymbolType type = courant->type;
            VarType var_type = courant->var_type;
            int address = courant->address;
            printf("Nom : %s (type: %s, var_type: %s, adresse: %d)\n",name,(type == FUNC) ? "Function" : (type == VAR_GLOBAL ? "Global Var" : "Local Var"),(var_type == INT) ? "int" : (var_type == CHAR ? "char" : "void"),address);
            courant = courant->next;
        }
    }
}

void buildSymbolTable(Node *root, SymbolTable *table, int *address, int inFunction) {
    if (root == NULL)
        return;

    switch (root->label) {
        case CORPS:
            inFunction = 1;
            break;

        case DECLVARS:
            if (root->firstChild && root->firstChild->firstChild) {
                VarType v_type;
                char *type = root->firstChild->firstChild->value;  
                char *name = root->firstChild->value; 
                if (strcmp(type, "int") == 0) {
                    v_type = INT;
                } else if (strcmp(type, "char") == 0) {
                    v_type = CHAR;
                } else {
                    fprintf(stderr, "Erreur : type inconnu %s\n", type);
                    break;
                }

                if (inFunction)
                    AddSymbol(table, name, VAR_LOCAL, v_type, (*address)++, 0);
                else
                    AddSymbol(table, name, VAR_GLOBAL, v_type, (*address)++, 0);
            } else {
                fprintf(stderr, "Erreur : déclaration mal formée\n");
            }
            break;

        case DECLVARS_static:
            if (root->firstChild && root->firstChild->firstChild) { 
                VarType v_type;
                char *name = root->firstChild->value;           
                char *type = root->firstChild->firstChild->value; 

                if (!inFunction) {
                    fprintf(stderr, "Erreur : variable static déclarée hors fonction\n");
                    break;
                }

                if (strcmp(type, "int") == 0) {
                    v_type = INT;
                } else if (strcmp(type, "char") == 0) {
                    v_type = CHAR;
                } else {
                    fprintf(stderr, "Erreur : type inconnu %s\n", type);
                    break;
                }

                AddSymbol(table, name, VAR_LOCAL, v_type, (*address)++, 1);
            } else {
                fprintf(stderr, "Erreur : déclaration static mal formée\n");
            }
            break;

        case ENTETE_FONCT:
            if (root->firstChild && root->firstChild->firstChild) {
                VarType v_type;
                char *name = root->firstChild->value;          
                char *type = root->firstChild->firstChild->value; 
                
                if (strcmp(type, "int") == 0) {
                    v_type = INT;
                } else if (strcmp(type, "char") == 0) {
                    v_type = CHAR;
                } else if (strcmp(type, "void") == 0) {
                    v_type = VOOID;
                } else {
                    fprintf(stderr, "Erreur : type inconnu %s\n", type);
                    break;
                }
                
                AddSymbol(table, name, FUNC, v_type, (*address)++, 0);
            } else {
                fprintf(stderr, "Erreur : entête fonction mal formée\n");
            }
            break;
         case LISTTYPVAR:{
           
            Node *n = root->firstChild;
            while (n != NULL) {
                if (n->firstChild && n->firstChild->value != NULL) {
                    VarType v_type;
                    char *name = n->value;        
                    char *type = n->firstChild->value; 
                    
                    if (strcmp(type, "int") == 0) {
                        v_type = INT;
                    } else if (strcmp(type, "char") == 0) {
                        v_type = CHAR;
                    } else {
                        fprintf(stderr, "Erreur : type inconnu %s\n", type);
                        break;
                    }
                    AddSymbol(table, name, VAR_LOCAL, v_type, (*address)++, 0);
                } else {
                    fprintf(stderr, "Erreur : liste des types de variables mal formée\n");
                }
                n = n->nextSibling;
            }
         }
            break;

        default:
            break;
    }

    buildSymbolTable(root->firstChild, table, address, inFunction);
    buildSymbolTable(root->nextSibling, table, address, inFunction);
}




SymbolTable *SymbolTableFromTree(Node *root) {
    SymbolTable *table = CreateTable();
    int address = 0;
    if (root) {
        buildSymbolTable(root, table,&address, 0);
    }

    return table;
}