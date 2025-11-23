%{
#include "tree.h"
#include "parser.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
void yyerror(char * msg);
int yylex();
%}

%option yylineno
%option nounput
%option noinput
%option noyywrap


%%

"if"  return IF;
"else" return ELSE;
"return" return RETURN;
"while" return WHILE;

"||"  return OR;
"&&"  return AND;

[*/%] { strncpy(yylval.operation, yytext, sizeof(yylval.operation) - 1) ;return DIVSTAR;}

[+-]  { strncpy(yylval.operation, yytext, sizeof(yylval.operation) - 1) ;return ADDSUB; }

">="  { strncpy(yylval.boolean, yytext, sizeof(yylval.boolean) - 1) ;return ORDER;}
"<="  { strncpy(yylval.boolean, yytext, sizeof(yylval.boolean) - 1) ;return ORDER;}
[<>]  { strncpy(yylval.boolean, yytext, sizeof(yylval.boolean) - 1) ;return ORDER;}

"=="  { strncpy(yylval.boolean, yytext, sizeof(yylval.boolean) - 1) ;return EQ;}
"!="  { strncpy(yylval.boolean, yytext, sizeof(yylval.boolean) - 1) ;return EQ;}
"="   return EGALITE;

"int"  {strncpy(yylval.type, yytext, sizeof(yylval.type) - 1);return TYPE;}
"char" {strncpy(yylval.type, yytext, sizeof(yylval.type) - 1);return TYPE;}
"double" {strncpy(yylval.type, yytext, sizeof(yylval.type) - 1);return DOUBLE;}
"static" return STATIC;

"void"  {strncpy(yylval.type, yytext, sizeof(yylval.type) - 1);return VOID;}

[A-Za-z_][A-Za-z0-9_]*   { strncpy(yylval.ident, yytext, sizeof(yylval.ident) - 1);
    yylval.ident[sizeof(yylval.ident) - 1] = '\0'; 
    return IDENT;}

[0-9]+ { strncpy(yylval.num, yytext, sizeof(yylval.num) - 1);
    yylval.num[sizeof(yylval.num) - 1] = '\0'; 
    return NUM;}

\'"\\n"\' { strncpy(yylval.character, yytext, sizeof(yylval.character) - 1);
    yylval.character[sizeof(yylval.character) - 1] = '\0';
    return CHARACTER;}
\'"\\t"\' { strncpy(yylval.character, yytext, sizeof(yylval.character) - 1);
    yylval.character[sizeof(yylval.character) - 1] = '\0'; 
    return CHARACTER;}
\'.\'   { strncpy(yylval.character, yytext, sizeof(yylval.character) - 1);
    yylval.character[sizeof(yylval.character) - 1] = '\0'; 
    return CHARACTER;}



\n  yylineno++;
[ \t]+     ;
"//".*       ;
"/*"([^*]|\*+[^*/])*"*/"  ;
"(" return PARANTHESE_OUVERT;
")" return PARANTHESE_FERMER;
";" return POINT_VIRGULE;
"," return VIRGULE;
"{" return CROCHET_OUVERT;
"}" return CROCHET_FERMER;
"!" return NEGATION;


.  {printf("Caractère non reconnu : %c\n", yytext[0]); yyerror("Caractère invalide"); }


%%