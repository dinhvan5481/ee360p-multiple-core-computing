//
// Created by van quach on 11/29/16.
//

#ifndef CUDA_SART_H
#define CUDA_SART_H

#include <memory>
#include <omp.h>
#include <ctime>
#include "CTMatrix.h"

class SART {
public:
    SART(int nRows, int nCols, weak_ptr<CTMatrix<float>> pSystemMatrix, weak_ptr<CTMatrix<float>> pProjections,
         float lambda, float esp, int maxRun, int numSensors, int nProjections);
    void run();
private:
    int nRows;
    int nCols;
    weak_ptr<CTMatrix<float>> pwSystemMatrix;
    weak_ptr<CTMatrix<float>> pwProjections;
    int maxRun;
    int nSensors;
    int nProjections;
    MatrixXf result;
    float lambda;
    float esp;
    MatrixXf C, R, CATR;
};


#endif //CUDA_SART_H
