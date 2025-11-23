#include "Buffer.h"

/*
 * Module : Buffer.c
 * -----------------
 * Ce module gère l'écriture et la lecture de bits dans un fichier en utilisant 
 * un buffer. Il permet de manipuler des flux de bits
 * 
 */




/*
 * Fonction : initBitStream
 * ---------------------------
 * Initialise le buffer de bits pour l'écriture dans un fichier.
 *
 * Paramètres :
 * - stream : Pointeur vers la structure BitStream à initialiser.
 * - fichier : Fichier où les bits seront écrits.
 */
void initBitStream(BitStream *stream, FILE *fichier) {
    stream->buffer = 0;
    stream->bit_count = 0;
    stream->fichier = fichier;
}


/*
 * Fonction : writeBit
 * ---------------------
 * Écrit un bit dans le flux de bits. Si 8 bits ont été écrits, 
 * le buffer est vidé dans le fichier. Un bit est ajouté à la fin 
 * du buffer, et le compteur de bits est incrémenté.
 * Si 8 bits sont présents dans le buffer, il est écrit dans le fichier.
 *
 * Paramètres :
 * - stream : Pointeur vers la structure BitStream.
 * - bit : Le bit (0 ou 1) à écrire.
 */
void writeBit(BitStream *stream,int bit) {
    stream->buffer = (stream->buffer << 1) | (bit & 1);
    stream->bit_count++;
    if(stream->bit_count==8){
        fwrite(&stream->buffer,sizeof(unsigned char),1,stream->fichier);
        stream->buffer=0;
        stream->bit_count=0;
    }
}


/*
 * Fonction : `writeBits`
 * ----------------------
 * Écrit plusieurs bits dans le flux de bits. Chaque bit est écrit 
 * un par un dans le buffer. Une fois que tous les bits sont écrits, 
 * ils sont ajoutés dans le fichier à travers writeBit.
 *
 * Paramètres :
 * - stream : Pointeur vers la structure BitStream.
 * - value : La valeur à écrire, divisée en plusieurs bits.
 * - nbits : Le nombre de bits à écrire à partir de la valeur.
 */
void writeBits(BitStream *stream, unsigned int value, int nbits) {
    for(int i =nbits-1; i >= 0; i--){
        writeBit(stream,(value>>i) & 1);
    }
}

/*
 * Fonction : flushBitStream
 * ---------------------------
 * Vide le flux de bits, en écrivant les bits restants.
 *
 * Paramètre :
 * - stream : Pointeur vers la structure BitStream.
 */
void flushBitStream(BitStream *stream) {
    if(stream->bit_count > 0){
        stream->buffer<<=(8 - stream->bit_count); 
        fwrite(&stream->buffer, sizeof(unsigned char), 1, stream->fichier);
    }
}



/*
 * Fonction : readBit
 * --------------------
 * Lit un bit à partir du flux de bits.Si le tampon est vide ), 
 * un byte est lu depuis le fichier pour recharger le tampon.
 * Le bit est extrait du tampon et renvoyé. Si la fin du fichier est atteinte, 
 * la fonction renvoie -1.
 *
 * Paramètre :
 * - stream : Pointeur vers la structure BitStream.
 *
 * Retourne :
 * - Le bit lu ou -1 si fin de fichier.
 */
unsigned char readBit(BitStream *stream) {
    if(stream->bit_count == 0){
        size_t bytes_read = fread(&stream->buffer, sizeof(unsigned char), 1, stream->fichier);
        if (bytes_read == 0) {
            return -1;  
        }
        stream->bit_count = 8; 
    }
    unsigned char bit = (stream->buffer >> (stream->bit_count - 1)) & 1;
    stream->bit_count--; 
    return bit;
}


/*
 * Fonction : readBits
 * ---------------------
 * Lit plusieurs bits depuis le flux de bits.
 *
 * Paramètres :
 * - stream : Pointeur vers la structure BitStream.
 * - nbits : Le nombre de bits à lire.
 *
 * Retourne les bits lus sous forme d'une valeur entière ou -1 en cas de fin de fichier.
 */
unsigned char readBits(BitStream *stream, int nbits) {
    unsigned char result = 0;
    for(int i = 0; i < nbits; i++){
        int bit = readBit(stream);
        if(bit == -1){
            return -1;  
        }
        result = (result << 1) | bit;  
    }
    return result;
}

