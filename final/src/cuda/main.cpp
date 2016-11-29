#include <iostream>
#include <vector>
#include <fstream>
#include <Eigen/Dense>

#include "CTMatrix.h"
#include "SIRT.h"
#include "PPMWriter.h"

using namespace std;


double ramp(float x)
{
    if (std::isinf(x))
        return 0;
    else
        return x;
}

int main() {
    string dataFileName = "rawData/test.csv";

//    CTMatrix<float> ctMatrix(dataFileName);

//    Matrix<int, 128, 128, RowMajor> imgTest;
//
//    int imgData = 0;
//    for (int rowIndex = 0; rowIndex < 128; ++rowIndex) {
//        for (int colIn = 0; colIn < 128; ++colIn) {
//            imgData = rowIndex * colIn;
//            imgData = imgData > 255 ? 255 : imgData;
//            imgTest(rowIndex, colIn) = imgData;
//        }
//    }
//    CTMatrix<int> ctImgTest(imgTest);
//    PPMWriter::create("test.pgm", ctImgTest);
/*    Matrix<float, Dynamic, Dynamic, RowMajor> testMatrix(3,2);
    Matrix<float, Dynamic, Dynamic> tmp;
    testMatrix << -1, 0, 3, 0, 3, 0;
    tmp = testMatrix;
    Matrix<float, Dynamic, Dynamic, RowMajor> m;
    m = testMatrix.colwise().sum().asDiagonal().inverse();
    for (int rowIndex = 0; rowIndex < m.rows(); ++rowIndex) {
        for (int colIndex = 0; colIndex < m.cols(); ++colIndex) {
            if(std::isinf(m(rowIndex, colIndex))) {
                m(rowIndex, colIndex) = 0;
            }
        }
    }

    cout << m;*/

    string systemMatrixFileName = "data/system_matrix_50_50.csv";
    string projectionsFileName = "data/projections_50_50.csv";
    shared_ptr<CTMatrix<float>> psCTSystemMatrix = make_shared<CTMatrix<float>>(systemMatrixFileName);
    shared_ptr<CTMatrix<float>> psCTProjections = make_shared<CTMatrix<float>>(projectionsFileName);
    int imageWidth = 50;
    int imageHeight = 50;
    float lambda = 1.9; // magic number
    float esp = 0.01;
    int maxRun = 100;
    weak_ptr<CTMatrix<float>> pwSystemMatrix(psCTSystemMatrix);
    weak_ptr<CTMatrix<float>> pwProjections(psCTProjections);
    SIRT sirt(imageWidth, imageHeight, pwSystemMatrix, pwProjections, lambda, esp, maxRun);
    sirt.run();


    return 0;
}
