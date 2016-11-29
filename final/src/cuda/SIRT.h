//
// Created by van quach on 11/26/16.
//

#ifndef CUDA_SIRT_H
#define CUDA_SIRT_H


#include "CTMatrix.h"

class SIRT {
public:
    SIRT(int width, int height, weak_ptr<CTMatrix<float>> pSystemMatrix, weak_ptr<CTMatrix<float>> pProjections, float lambda, float esp, int maxRun);
    void run();

private:
    weak_ptr<CTMatrix<float>> pSystemMatrix;
    weak_ptr<CTMatrix<float>> pProjections;
    float lambda;
    int width;
    int height;
    int maxRun;
    float esp;
    Matrix<float, Dynamic, 1> result;


};

#endif //CUDA_SIRT_H
