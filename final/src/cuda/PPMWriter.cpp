//
// Created by van quach on 11/25/16.
//

#include "PPMWriter.h"
#include <iostream>
#include <Eigen/Dense>
#include <fstream>

#include "CTMatrix.h"

using namespace std;
using namespace Eigen;


void PPMWriter::create(string fileName, weak_ptr<MatrixXf> data, int height, int width) {
    shared_ptr<MatrixXf> pImgData = data.lock();
    ofstream outFile(fileName);
    outFile << "P2" << endl;
    outFile << width << ' ' << height << endl;
    outFile << "255" << endl;
    float min = pImgData->minCoeff();
    float max = pImgData->maxCoeff();
    float slope = 255 / (max - min);
    for(int rowIndex = 0; rowIndex < height; ++rowIndex) {
        for (int colIndex = 0; colIndex < width; ++colIndex) {
            outFile << (int)(slope * (pImgData->coeff(rowIndex, colIndex) - min)) << ' ';
        }
        outFile << endl;
    }

}
