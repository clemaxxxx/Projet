%{
#include <stdio.h>
#include <stdlib.h>
#include "tree.h"
#include "parser.h"
#include "symbol_table.h"

void yyerror(char *msg);
int yylex();
extern int yylineno;
extern char *yytext;
Node *root;
SymbolTable * symboltab;
%}

%union {
    Node *node;
    char num[64];
    char character[64];
    char ident[64];
    char type[64];
    char operation[64];
    char boolean[64];
}

%token CHARACTER NUM IDENT TYPE DOUBLE VOID STATIC
%token EGALITE ADDSUB DIVSTAR ORDER EQ OR AND
%token WHILE RETURN IF ELSE
%token PARANTHESE_OUVERT PARANTHESE_FERMER POINT_VIRGULE VIRGULE CROCHET_OUVERT CROCHET_FERMER NEGATION

%type <node>  DeclVarStatic Prog DeclVars Declarateurs DeclFoncts DeclFonct EnTeteFonct Parametres ListTypVar Corps SuiteInstr Instr Exp TB FB M E T F Arguments ListExp
%type <node> STATIC EGALITE OR AND WHILE RETURN IF ELSE
%type <num> NUM
%type <character> CHARACTER
%type <ident> IDENT
%type <type> TYPE VOID DOUBLE
%type <operation> ADDSUB DIVSTAR
%type <boolean> ORDER EQ

%%


Prog:
    DeclVars DeclFoncts {
        $$ = makeNode(PROG);
        addChild($$, $1);
        addChild($$, $2);
        root = $$;
}
    ;


DeclVars:
      DeclVars TYPE Declarateurs POINT_VIRGULE {
            Node *decl = makeNode(DECLVARS);
            Node *current = $3;
            while (current) {
                  addChild(current, makeF(type, $2));
                  current = current->nextSibling;
            }

            addChild(decl, $3);

            if ($1) {
                  addSibling($1, decl);
                  $$ = $1;
            } else {
                  $$ = decl;
            }
      }
    |  {
            $$ = NULL;
      }
    ;


DeclVarStatic:
      DeclVarStatic TYPE Declarateurs POINT_VIRGULE {
            Node *decl = makeNode(DECLVARS);
            Node *current = $3;
            while (current) {
                  addChild(current, makeF(type, $2));
                  current = current->nextSibling;
            }

            addChild(decl, $3);

            if ($1) {
                  addSibling($1, decl);
                  $$ = $1;
            } else {
                  $$ = decl;
            }
      }
    | DeclVarStatic STATIC TYPE Declarateurs POINT_VIRGULE {
            Node *decl = makeNode(DECLVARS_static);
            Node *current = $4;
            while (current) {
                  addChild(current, makeF(type, $3));
                  current = current->nextSibling;
            }

            addChild(decl, $4);

            if ($1) {
                  addSibling($1, decl);
                  $$ = $1;
            } else {
                  $$ = decl;
            }
      }
    | {
            $$ = NULL;
      }
    ;


Declarateurs:
      Declarateurs VIRGULE IDENT {
            $$ = $1;
            Node *newNode = makeF(ident, $3);
            addSibling($$, newNode);
      }
    | IDENT {
            $$ = makeF(ident, $1);
      }
    ;


DeclFoncts:
      DeclFoncts DeclFonct {
            $$ = $1;
            addSibling($$, $2);
      }
    | DeclFonct {
            $$ = $1;
      }
    ;


DeclFonct:
    EnTeteFonct Corps {
            $$ = makeNode(DECLFONCT);
            addChild($$, $1);
            addChild($$, $2);
    }
    ;


EnTeteFonct:
      TYPE IDENT PARANTHESE_OUVERT Parametres PARANTHESE_FERMER {
            $$ = makeNode(ENTETE_FONCT);
            Node * i = makeF(ident,$2);
            addChild(i,makeF(type, $1));
            addChild($$,i);
            addChild($$,$4);
      }
    | VOID IDENT PARANTHESE_OUVERT Parametres PARANTHESE_FERMER {
            $$ = makeNode(ENTETE_FONCT);
            Node * i = makeF(ident,$2);
            addChild(i,makeF(type, $1));
            addChild($$,i);
            addChild($$,$4);
      }
    ;


Parametres:
      VOID {
            $$ = NULL;
      }
    | ListTypVar {
            $$ = $1;
      }
    ;


ListTypVar:
      TYPE IDENT {
            $$ = makeNode(LISTTYPVAR);
            Node *identNode = makeF(ident, $2);
            addChild(identNode, makeF(type, $1));
            addChild($$, identNode);
      }
    | ListTypVar VIRGULE TYPE IDENT {
            Node *identNode = makeF(ident, $4);
            addChild(identNode, makeF(type, $3));
            addChild($1, identNode);
            $$ = $1;
      }
;




Corps:
    CROCHET_OUVERT DeclVarStatic SuiteInstr CROCHET_FERMER {
            $$ = makeNode(CORPS);
            addChild($$, $2);
            addChild($$, $3);
      }
    ;


SuiteInstr:
      SuiteInstr Instr {
            $$ = $1;
            addChild($$, $2);
      }
    | {
            $$ = makeNode(SUITEINSTR);
      }
    ;


Instr:
      IDENT EGALITE Exp POINT_VIRGULE {
            $$ = makeNode(egalite);
            addChild($$, makeF(ident, $1));
            addChild($$, $3);
      }
    | IF PARANTHESE_OUVERT Exp PARANTHESE_FERMER Instr {
            $$ = makeNode(iF);
            addChild($$, $3);
            addChild($$, $5);
      }
    | IF PARANTHESE_OUVERT Exp PARANTHESE_FERMER Instr ELSE Instr {
            $$ = makeNode(iF);
            addChild($$, $3);
            addChild($$, $5);
            addChild($$, $7);
      }
    | WHILE PARANTHESE_OUVERT Exp PARANTHESE_FERMER Instr {
            $$ = makeNode(While);
            addChild($$, $3);
            addChild($$, $5);
      }
    | IDENT PARANTHESE_OUVERT Arguments PARANTHESE_FERMER POINT_VIRGULE {
            $$ = makeF(ident, $1);
            addChild($$, $3);
      }
    | RETURN Exp POINT_VIRGULE {
            $$ = makeNode(Return);
            addChild($$, $2);
      }
    | RETURN POINT_VIRGULE {
            $$ = makeNode(Return);
      }
    | CROCHET_OUVERT SuiteInstr CROCHET_FERMER {
            $$ = makeNode(INSTR);
            addChild($$, $2);
      }
    | POINT_VIRGULE {
            $$ = makeNode(INSTR);
      }
    ;


Exp:
      Exp OR TB {
            $$ = makeNode(or);
            addChild($$, $1);
            addChild($$, $3);
      }
    | TB {
            $$ = $1;
      }
    ;

TB:
      TB AND FB {
            $$ = makeNode(and);
            addChild($$, $1);
            addChild($$, $3);
      }
    | FB {
            $$ = $1;
      }
    ;

FB:
      FB EQ M {
            $$ = makeNode(eq);
            $$->value = strdup($2);
            addChild($$, $1);
            addChild($$, $3);
      }
    | M {
            $$ = $1;
      }
    ;

M:
      M ORDER E {
            $$ = makeNode(order);
            $$->value = strdup($2);
            addChild($$, $1);
            addChild($$, $3);
      }
    | E {
            $$ = $1;
      }
    ;

E:
      E ADDSUB T {
            $$ = makeNode(addsub);
            $$->value = strdup($2);
            addChild($$, $1);
            addChild($$, $3);
      }
    | T {
            $$ = $1;
      }
    ;

T:
      T DIVSTAR F {
            $$ = makeNode(divstar);
            $$->value = strdup($2);
            addChild($$, $1);
            addChild($$, $3);
      }
    | F {
            $$ = $1;
      }
    ;


F:
      ADDSUB F {
            $$ = makeNode(addsub);
            $$->value = strdup($2->value); 
            addChild($$, $2);
      }
    | NEGATION F {
            $$ = makeNode(negation);
            addChild($$, $2);
      }
    | PARANTHESE_OUVERT Exp PARANTHESE_FERMER {
            $$ = $2;
      }
    | NUM {
            $$ = makeF(num, $1);
      }
    | CHARACTER {
            $$ = makeF(character, $1);
      }
    | IDENT {
            $$ = makeF(ident, $1);
      }
    | IDENT PARANTHESE_OUVERT Arguments PARANTHESE_FERMER {
            $$ = makeF(ident, $1);
            addChild($$, $3);
      }
    ;


Arguments:
      ListExp {
            $$ = $1;
      }
    | {
            $$ = makeNode(LISTEXP);
      }
    ;

ListExp:
      Exp {
            $$ = makeNode(LISTEXP);
            addChild($$, $1);
      }
    | ListExp VIRGULE Exp {
            $$ = $1;
            addChild($$, $3);
      }
    ;

%%

void yyerror(char *msg) {
    fprintf(stderr, "Erreur de syntaxe : %s  ligne : %d\n", msg, yylineno);
    exit(1);
}
