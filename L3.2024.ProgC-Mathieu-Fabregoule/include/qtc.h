#ifndef QTC_H
#define QTC_H

#include <stdio.h>
#include <stdlib.h>
#include "Buffer.h"
#include "LirePgm.h"
#include "QuadTree.h"
#include "codeur.h"
#include "decodeur.h"
#include "options.h"

   void gestionOptions(int argc, char** argv, Options* options);
   void Encodage(Options o);
   void Decodage(Options o);

#endif