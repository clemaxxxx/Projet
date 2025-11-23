#include "qtc.h"




int main(int argc, char* argv[]) {
    Options o;
    gestionOptions(argc,argv,&o);
    if(o.modeEncodeur){
        Encodage(o);
    }else{
        Decodage(o);
    }

}