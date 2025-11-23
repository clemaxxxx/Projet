#include "Traduction.h"
#include <stdio.h>
FILE * f = NULL;
int label_count = 0;

int recup_value(Node *n) {
    if (!n) return 0;
    if(n->value ==NULL){
            exit(0);
    }
    if (n->label == num) {
        return atoi(n->value);
    } else if (n->label == character ) {
        return n->value[1];  
    }
    return 0;
}


void open_file() {
    f = fopen("_anonymous.asm", "w");
    if (f == NULL) {
        perror("Erreur lors de l'ouverture du fichier de sortie");
        exit(0);
    }
}

void close_file() {
    if (f != NULL) {
        fclose(f);
    }
}

const char* recup_size(char * name,SymbolTable * t) {
    Symbol * symbol = FindSymbol(t,name);
    if(symbol == NULL){
        exit(0);
    }
    if (symbol->var_type == CHAR) return "byte";
    if (symbol->var_type == INT)  return "dword";
    if (symbol->var_type == DOUUBLE) return "qword";
    return "qword"; 
}


void decl_Var(Node * node){
    if(node == NULL) return;

    if(node->label == DECLVARS){
        char * type = node->firstChild->firstChild->value;
        char * name = node->firstChild->value;
        if(type == NULL || name == NULL){
            exit(0);
        }
        
        if (strcmp(type, "char") == 0) {
            fprintf(f, "%s: resb 1\n", name);
        } else if (strcmp(type, "int") == 0) {
            fprintf(f, "%s: resd 1\n", name);
        } else if (strcmp(type, "double") == 0) {
            fprintf(f, "%s: resq 1\n", name);
        }
    }
    if(node->label == LISTTYPVAR){
        Node * param = node->firstChild;
        if(param ==NULL){
            exit(0);
        }
        while(param){
            char * name = param->value;
            char *type = param->firstChild->value;
            if (strcmp(type, "char") == 0)
                fprintf(f, "%s: resb 1\n", name);
            else if (strcmp(type, "int") == 0)
                fprintf(f, "%s: resd 1\n", name);
            else if (strcmp(type, "double") == 0)
                fprintf(f, "%s: resq 1\n", name);
            param = param->nextSibling;
        }
    }
    decl_Var(node->nextSibling);
    decl_Var(node->firstChild);
}

void translate_global(Node * node){
    if(node == NULL) return;
    if(node->label == ENTETE_FONCT && strcmp(node->firstChild->value, "main") != 0){
        char * name = node->firstChild->value;
        if(name==NULL){
            exit(0);
        }
        fprintf(f, "global %s\n",name);
    }
    translate_global(node->firstChild);
    translate_global(node->nextSibling);
}

void Translate(Node *node,SymbolTable * t) { 
    int cpt = 0;
    if (node == NULL) return;
    if(node->label == PROG){
        fprintf(f, "global _start\n");
        translate_global(node);
        fprintf(f,"section .bss\n");
        decl_Var(node);
        fprintf(f, "section .text\n");
    }
    if (node->label == DECLFONCT && strcmp(node->firstChild->firstChild->value, "main") == 0) {
        fprintf(f, "_start:\n");
        cpt = 1;
        translate_main_body(node->firstChild->nextSibling,t,cpt);
        fprintf(f, "  mov rax, 60\n");
        fprintf(f, "  mov rdi, 0\n");
        fprintf(f, "  syscall\n");
    }
    if(node->label == DECLFONCT && strcmp(node->firstChild->firstChild->value, "main") != 0){
        char * name = node->firstChild->firstChild->value;
        fprintf(f,"%s:\n",name);
        Node * n = node->firstChild->nextSibling;
        translate_main_body(n,t,cpt);
    }
    Translate(node->firstChild,t);
    Translate(node->nextSibling,t);
}



void translate_main_body(Node *node,SymbolTable * t,int cpt) {
    if (node == NULL) return;
    if (node->label == CORPS) {
        Node *child = node->firstChild;
        if(child == NULL){
            exit(0);
        }
        while (child != NULL) {
            if (child->label == SUITEINSTR) {
                Node *instr = child->firstChild;
                while (instr != NULL) {
                    if (instr->label == egalite) {
                        translate_egalite(instr,t);
                    } else if (instr->label == iF) {
                        translate_if(instr,t);
                    } else if (instr->label == While){
                        translate_while(instr,t);
                    } else if(instr->label == Return && cpt == 0){
                        translate_return(instr,t);
                    }
                    instr = instr->nextSibling;
                }
            }
            child = child->nextSibling;
        }
    }
}





void translate_expr(Node *node,SymbolTable * t) {
    if (node == NULL) return;

    if (node->label == addsub) {
        Node *left = node->firstChild;
        Node *right = left->nextSibling;
        if(left ==NULL || right == NULL){
            exit(0);
        }
        translate_expr(left,t);
        fprintf(f, "  push rbx\n");
        translate_expr(right,t);
        fprintf(f, "  pop rax\n");

        if (strcmp(node->value, "+") == 0) {
            fprintf(f, "  add rax, rbx\n");
        } else if (strcmp(node->value, "-") == 0) {
            fprintf(f, "  sub rax, rbx\n");
        }
        fprintf(f, "  mov rbx, rax\n");
    }

    else if (node->label == divstar) {
        Node *left = node->firstChild;
        Node *right = left->nextSibling;
        
        if(left ==NULL || right == NULL){
            exit(0);
        }

        translate_expr(left,t);
        fprintf(f, "  push rbx\n");
        translate_expr(right,t);
        fprintf(f, "  pop rax\n");

        if (strcmp(node->value, "*") == 0) {
            fprintf(f, "  imul rax, rbx\n");
        } else if (strcmp(node->value, "/") == 0) {
            fprintf(f, "  xor rdx, rdx\n");
            fprintf(f, "  idiv rbx\n");
            fprintf(f, "  mov rbx, rax\n");
        } else if (strcmp(node->value, "%%") == 0) {
            fprintf(f, "  xor rdx, rdx\n");
            fprintf(f, "  idiv rbx\n");
            fprintf(f, "  mov rbx, rdx\n");
        }

        fprintf(f, "  mov rbx, rax\n");
    }

    else if (node->label == num || node->label == character) {
        int value = recup_value(node);
        fprintf(f, "  mov rbx, %d\n", value);
    }

    else if (node->label == ident) {
        const char* size = recup_size(node->value,t);
        fprintf(f, "  mov rbx, %s [%s]\n", size,node->value);
    }
}


void translate_egalite(Node * node,SymbolTable * t){
    if (node == NULL) return;
    if (node->label == egalite){
        Node *left = node->firstChild;
        Node *right = node->firstChild->nextSibling;
        if(left ==NULL || right == NULL){
            exit(0);
        }
        const char *size = recup_size(left->value,t);

        if(left->label == ident && (right->label == num || right->label == character)){
            int right_value = recup_value(right);
            fprintf(f, "  mov  %s [%s], %d\n", size,left->value,right_value);
        }
        if(left->label == ident && (right->label == addsub || right->label == divstar)){
            translate_expr(right,t);
            fprintf(f, "  mov %s [%s], rbx\n",size,left->value);
        }
        if(left->label == ident && right->label == ident){
            const char *size_r = recup_size(right->value,t);
            fprintf(f, "  mov rbx, %s [%s]\n",size_r, right->value); 
            fprintf(f, "  mov %s [%s], rbx\n",size, left->value);
        }
    }


}



void translate_if(Node* node, SymbolTable* t) {
    if (node == NULL) return;

    if (node->label == iF) {
        int current_label = label_count++;
        Node* condition = node->firstChild;
        if(condition == NULL){
            exit(0);
        }
        if(condition->label == num){
            return;
        }
        if(condition->label != order && condition->label !=eq && condition->label != or && condition->label != and){
            return;
        }


        Node* true_branch = condition ? condition->nextSibling : NULL;
        Node* true_instr = (true_branch && true_branch->firstChild) ? true_branch->firstChild->firstChild : NULL;
        Node* false_branch = (true_branch) ? true_branch->nextSibling : NULL;
        Node* false_instr = (false_branch && false_branch->firstChild) ? false_branch->firstChild->firstChild : NULL;
     
        translate_expr_bool(condition, current_label, t);

        if (false_instr) {
            fprintf(f, "  jmp .if_false_%d\n", current_label);
        } else {
            fprintf(f, "  jmp .end_if_%d\n", current_label);
        }


        fprintf(f, ".if_true_%d:\n", current_label);
        if (true_instr) translate_egalite(true_instr, t);

        if (false_instr) {
            fprintf(f, "  jmp .end_if_%d\n", current_label);
            fprintf(f, ".if_false_%d:\n", current_label);
            translate_egalite(false_instr, t);
        }

        fprintf(f, ".end_if_%d:\n", current_label);
    }
}


void translate_expr_bool(Node * node, int current_label,SymbolTable * t){
    if (node == NULL) return;


    Node* left = node->firstChild;
    Node* right = left->nextSibling;
    
    if(left ==NULL || right == NULL){
            exit(0);
    }


    if ((left->label == num || left->label == character) && (right->label == num || right->label == character)) {
        int l = recup_value(left);
        int r = recup_value(right);
        fprintf(f, "  mov rax, %d\n", l);
        fprintf(f, "  mov rbx, %d\n", r);
        fprintf(f, "  cmp rax, rbx\n");
    }

    else if (left->label == ident && (right->label == num || right->label == character)) {
        int r = recup_value(right);
        fprintf(f, "  mov rax, %s [%s]\n",recup_size(left->value,t), left->value);
        fprintf(f, "  cmp rax, %d\n", r);
    }

    else if ((left->label == num || left->label == character) && right->label == ident) {
        int l = recup_value(left);
        fprintf(f, "  mov rax, %s [%s]\n",recup_size(right->value,t), right->value);
        fprintf(f, "  cmp %d, rax\n", l);
    }
    else if (left->label == ident && right->label == ident) {
        fprintf(f, "  mov rax, %s [%s]\n", recup_size(left->value,t), left->value);
        fprintf(f, "  mov rbx, %s [%s]\n",recup_size(right->value,t), right->value);
        fprintf(f, "  cmp rax, rbx\n");
    }



    if (node->label == order || node->label == eq) {
        if (strcmp(node->value, ">") == 0) fprintf(f, "  jg .if_true_%d\n", current_label);
        else if (strcmp(node->value, "<") == 0) fprintf(f, "  jl .if_true_%d\n", current_label);
        else if (strcmp(node->value, ">=") == 0) fprintf(f, "  jge .if_true_%d\n", current_label);
        else if (strcmp(node->value, "<=") == 0) fprintf(f, "  jle .if_true_%d\n", current_label);
        else if (strcmp(node->value, "==") == 0) fprintf(f, "  je .if_true_%d\n", current_label);
        else if (strcmp(node->value, "!=") == 0) fprintf(f, "  jne .if_true_%d\n", current_label);
    }
}




void translate_while(Node *node, SymbolTable *t) {
    if (node == NULL) return;

    if (node->label == While) {
        int current_label = label_count++;
        Node *condition = node->firstChild;
        Node *branch = condition->nextSibling->firstChild->firstChild;
        if(condition ==NULL || branch == NULL){
            exit(0);
        }
        if(condition->label == num){
            return;
        }
        if(condition->label != order && condition->label !=eq && condition->label != or && condition->label != and){
            return ;
        }


        fprintf(f, ".loop_start_%d:\n", current_label);
        translate_expr_bool_while(condition, current_label, t);
        translate_egalite(branch, t);
        fprintf(f, "  jmp .loop_start_%d\n", current_label);
        fprintf(f, ".loop_end_%d:\n", current_label);
    }
}


void translate_expr_bool_while(Node *node, int current_label, SymbolTable *t) {
    if (node == NULL) return;

    Node* left = node->firstChild;
    Node* right = left->nextSibling;
    if(left ==NULL || right == NULL){
            exit(0);
    }

    

    if ((left->label == num || left->label == character) && (right->label == num || right->label == character)) {
        int l = recup_value(left);
        int r = recup_value(right);
        fprintf(f, "  mov rax, %d\n", l);
        fprintf(f, "  mov rbx, %d\n", r);
        fprintf(f, "  cmp rax, rbx\n");
    }
    else if (left->label == ident && (right->label == num || right->label == character)) {
        int r = recup_value(right);
        fprintf(f, "  mov rax, %s [%s]\n", recup_size(left->value, t), left->value);
        fprintf(f, "  cmp rax, %d\n", r);
    }
    else if ((left->label == num || left->label == character) && right->label == ident) {
        int l = recup_value(left);
        fprintf(f, "  mov rax, %s [%s]\n", recup_size(right->value, t), right->value);
        fprintf(f, "  cmp %d, rax\n", l);
    }
    else if (left->label == ident && right->label == ident) {
        fprintf(f, "  mov rax, %s [%s]\n", recup_size(left->value, t), left->value);
        fprintf(f, "  mov rbx, %s [%s]\n", recup_size(right->value, t), right->value);
        fprintf(f, "  cmp rax, rbx\n");
    }

    if (node->label == order || node->label == eq) {
        if (strcmp(node->value, ">") == 0) fprintf(f, "  jle .loop_end_%d\n", current_label);
        else if (strcmp(node->value, "<") == 0) fprintf(f, "  jge .loop_end_%d\n", current_label);
        else if (strcmp(node->value, ">=") == 0) fprintf(f, "  jl .loop_end_%d\n", current_label);
        else if (strcmp(node->value, "<=") == 0) fprintf(f, "  jg .loop_end_%d\n", current_label);
        else if (strcmp(node->value, "==") == 0) fprintf(f, "  jne .loop_end_%d\n", current_label);
        else if (strcmp(node->value, "!=") == 0) fprintf(f, "  je .loop_end_%d\n", current_label);
    }
}



void translate_return(Node *node, SymbolTable *t) {
    if (node == NULL) return;

    if (node->label != Return) return;

    if (node->firstChild == NULL) {
        fprintf(f, "  leave\n");
        fprintf(f, "  ret\n");
        return;
    }

    switch (node->firstChild->label) {
        case num:
        case character: {
            int val = recup_value(node->firstChild);
            fprintf(f, "  mov eax, %d\n", val);  
            break;
        }
        case ident: {
            char *var_name = node->firstChild->value;
            if(var_name == NULL){
                exit(0);
            }
            const char *prefix = recup_size(var_name, t);  
            fprintf(f, "  mov eax, %s [%s]\n", prefix, var_name); 
            break;
        }
        default:
            fprintf(stderr, "Type de retour non supporté\n");
            break;
    }
    fprintf(f, "  ret\n");
}


