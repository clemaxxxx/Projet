CC  = clang
STD = -std=c17

ifeq ($(DBG),1)
  CFLAGS = -g -Wpointer-arith -Wall
  EXT = .gdb
else
  CFLAGS = -O2
endif

EXEC = main

SRC = src/
INC = include/
BIN = bin/

PFLAGS = $(incG2X) -I$(INC)
LFLAGS = $(libG2X)$(EXT)

main : traj.o particule.o snake.o Obstacle.o Chain.o main.o
	$(CC) $^ $(LFLAGS) -o $@

%.o : $(SRC)%.c
	$(CC) $(STD) $(PFLAGS) $(CFLAGS) -c $^ -o $@

clean :
	rm -f *.o *~
cleanall :
	rm -f *.o *~  $(EXEC)

.PHONY : clean cleanall ? arch

? :
	@echo "---------informations de compilation----------"
	@echo "  compilateur : $(CC)"
	@echo "  PFLAGS      : $(PFLAGS)"
	@echo "  CFLAGS      : $(CFLAGS)"
	@echo "  LFLAGS      : $(LFLAGS)"

DATE = $(shell date +%y%m%d-%H%M)
NAME = $(shell basename $(PWD))

arch : Make* $(INC) $(SRC)
	@echo "archivage sources > $(NAME).$(DATE).tgz"
	@tar -zcf $(NAME).$(DATE).tgz $^
