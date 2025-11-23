CC = gcc
CFLAGS = -Wall -g3 -DYYDEBUG=1 -Wextra -Iobj -Isrc
LEX = flex
BISON = bison
LEXER = lexer
PARSER = parser
EXEC = bin/tpcc
OBJDIR = obj
BINDIR = bin

SRC = src/test.l src/parser.y src/tree.c src/symbol_table.c src/Traduction.c src/main.c src/semantique.c
OBJ = $(OBJDIR)/$(LEXER).o $(OBJDIR)/$(PARSER).o $(OBJDIR)/tree.o $(OBJDIR)/symbol_table.o $(OBJDIR)/Traduction.o $(OBJDIR)/semantique.o $(OBJDIR)/main.o
LEXER_C = $(OBJDIR)/$(LEXER).c
PARSER_C = $(OBJDIR)/$(PARSER).c
PARSER_H = $(OBJDIR)/$(PARSER).h
TREE_H = src/tree.h
SYMBOLTABLE_H = src/symbol_table.h
TRADUCTION_H = src/Traduction.h
SEMANTIQUE_H = src/semantique.h
all: $(BINDIR)/tpcc

$(BINDIR)/tpcc: $(OBJ)
	@mkdir -p $(BINDIR)
	$(CC) -o $@ $^ -lfl

$(OBJDIR)/%.o: src/%.c
	@mkdir -p $(OBJDIR)
	$(CC) -c $(CFLAGS) -o $@ $<

$(LEXER_C): src/$(LEXER).l $(PARSER_H)
	@mkdir -p $(OBJDIR)
	$(LEX) -o $@ $<

$(PARSER_C) $(PARSER_H): src/$(PARSER).y
	@mkdir -p $(OBJDIR)
	$(BISON) -d -v -Wcounterexamples -o $(PARSER_C) $<

$(OBJDIR)/$(LEXER).o: $(LEXER_C)
$(OBJDIR)/$(PARSER).o: $(PARSER_C) $(PARSER_H) $(TREE_H)
$(OBJDIR)/symbol_table.o: src/symbol_table.c $(SYMBOLTABLE_H)
$(OBJDIR)/Traduction.o: src/Traduction.c $(TRADUCTION_H)
$(OBJDIR)/semantique.o: src/semantique.c $(SEMANTIQUE_H)
$(OBJDIR)/tree.o: src/tree.c $(TREE_H)
$(OBJDIR)/main.o: src/main.c

clean:
	rm -rf $(OBJDIR) $(BINDIR)

re: clean all
