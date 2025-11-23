#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tree.h"


static const char *StringFromLabel[] = {
    "PROG", "DECLVARS", "DECLARATEUR", "DECLFONCT", "ENTETE_FONCT",
    "PARAMETRES", "INSTR", "EXP", "TB", "FB", "M", "E", "T", "F",
    "CORPS", "SUITEINSTR", "LISTEXP", "LISTTYPVAR", "ident", "num",
    "character", "addsub", "divstar", "eq", "and", "negation", "order",
    "EGALITE", "OR", "IF", "WHILE", "RETURN", "TYPE", "DECLVARS_static"
};


Node *makeF(label_t label, char *value) { 
    Node *node = (Node *)malloc(sizeof(Node));
    if (!node) {
        fprintf(stderr, "Erreur : mémoire insuffisante\n");
        exit(EXIT_FAILURE);
    }
    node->label = label;
    node->value = value ? strdup(value) : NULL;  
    node->firstChild = node->nextSibling = NULL;
    return node;
}

Node *makeNode(label_t label) {
    Node *node = (Node *)malloc(sizeof(Node));
    if (!node) {
        fprintf(stderr, "Erreur : mémoire insuffisante\n");
        exit(EXIT_FAILURE);
    }
    node->label = label;
    node->value = NULL;
    node->firstChild = node->nextSibling = NULL;
    return node;
}

void addSibling(Node *node, Node *sibling) {
    if (!node || !sibling) return;
    while (node->nextSibling != NULL) {
        node = node->nextSibling;
    }
    node->nextSibling = sibling;
}

void addChild(Node *parent, Node *child) {
    if (!parent){
        return;
    } 
    if(!child){
        return;
    }
    if (!parent->firstChild) {
        parent->firstChild = child;
    } else {
        addSibling(parent->firstChild, child);
    }
}

void deleteTree(Node *node) {
    if (!node) return;
    deleteTree(node->firstChild);
    deleteTree(node->nextSibling);
    free(node);
}

void printTree(Node *node) {
    static bool rightmost[128]; // tells if node is rightmost sibling
    static int depth = 0;       // depth of current node
    for (int i = 1; i < depth; i++) { // 2502 = vertical line
        printf(rightmost[i] ? "    " : "\u2502   ");
    }
    if (depth > 0) { // 2514 = L form; 2500 = horizontal line; 251c = vertical line and right horiz
        printf(rightmost[depth] ? "\u2514\u2500\u2500 " : "\u251c\u2500\u2500 ");
    }

    printf("%s", StringFromLabel[node->label]);
    if (node->value != NULL) {
        printf(" (%s)", node->value);
    }

    printf("\n");
    depth++;
    for (Node *child = node->firstChild; child != NULL; child = child->nextSibling) {
        rightmost[depth] = (child->nextSibling) ? false : true;
        printTree(child);
    }
    depth--;
}
