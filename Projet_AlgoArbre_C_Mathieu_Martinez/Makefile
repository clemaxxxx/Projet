saage : arbres_binaires.o greffe.o saage.o main.o
	gcc -o saage arbres_binaires.o greffe.o saage.o main.o

greffe.o : greffe.c greffe.h 
	gcc  -c -o greffe.o greffe.c

arbres_binaires.o : arbres_binaires.c arbres_binaires.h
	gcc  -c -o arbres_binaires.o arbres_binaires.c

saage.o : saage.c saage.h arbres_binaires.h
	gcc  -c -o saage.o saage.c

main.o : main.c arbres_binaires.h greffe.h saage.h
	gcc -c -o main.o main.c
