#ifndef BUFFER_H
    #define BUFFER_H
    #include <stdio.h>
    #include <stdlib.h>


    typedef unsigned char uchar;

    typedef struct {
        uchar buffer;  // Buffer pour les bits
        int bit_count; // Nombre de bits stockés dans le buffer   
        FILE *fichier; // Pointeur vers le fichier 
    } BitStream;


   void initBitStream(BitStream *stream, FILE *fichier);
   void writeBit(BitStream *stream, int bit);
   void writeBits(BitStream *stream, unsigned int value, int nbits);
   void flushBitStream(BitStream *stream);
   unsigned char readBit(BitStream *stream);
   unsigned char readBits(BitStream *stream, int nbits);


#endif
