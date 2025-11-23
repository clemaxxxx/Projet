CC = gcc
CFLAGS = -Wall
LDFLAGS = -Llib -lqtc -lm 

SRC = src/main.c
OBJ = $(SRC:.c=.o)

INCLUDE_DIR = include

EXEC = codec

$(EXEC): $(OBJ)
	$(CC) $(OBJ) -o $(EXEC) $(LDFLAGS)


%.o: %.c
	$(CC) $(CFLAGS) -I$(INCLUDE_DIR) -c $< -o $@

clean:
	rm -f $(OBJ) $(EXEC)

