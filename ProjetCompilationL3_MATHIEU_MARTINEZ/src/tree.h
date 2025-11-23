#ifndef TREE_H
#define TREE_H

typedef enum {
    PROG,
    DECLVARS,
    DECLARATEUR,
    DECLFONCT,
    ENTETE_FONCT,
    PARAMETRES,
    INSTR,
    EXP,
    TB,
    FB,
    M,
    E,
    T,
    F,
    CORPS,
    SUITEINSTR,
    LISTEXP,
    LISTTYPVAR,
    ident,
    num,
    character,
    addsub,
    divstar,
    eq,
    and,
    negation,
    order,
    egalite,
    or,
    iF,
    While,
    Return,
    type,
    DECLVARS_static,
} label_t;

typedef struct Node {
    label_t label;
    char *value;
    char * type; // Pour les type des variables
    struct Node *firstChild, *nextSibling;
    int lineno;
} Node;

Node *makeNode(label_t label);
void addSibling(Node *node, Node *sibling);
void addChild(Node *parent, Node *child);
void deleteTree(Node *node);
void printTree(Node *node);
Node *makeF(label_t label, char *value);
#define FIRSTCHILD(node) (node ? node->firstChild : NULL)
#define SECONDCHILD(node) (node && node->firstChild ? node->firstChild->nextSibling : NULL)
#define THIRDCHILD(node) (node && node->firstChild && node->firstChild->nextSibling ? node->firstChild->nextSibling->nextSibling : NULL)

#endif /* TREE_H */
