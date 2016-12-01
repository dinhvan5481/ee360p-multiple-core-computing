//
// Created by van quach on 11/25/16.
//

#ifndef CUDA_PPMWRITER_H
#define CUDA_PPMWRITER_H

#include <memory>
#include <Eigen/Dense>

#include "CTMatrix.h"

class PPMWriter {
public:

    static void create(string fileName, weak_ptr<MatrixXf> data, int height, int width);

};


#endif //CUDA_PPMWRITER_H
