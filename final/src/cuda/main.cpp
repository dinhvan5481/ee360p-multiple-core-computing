#include <iostream>
#include <vector>
#include <fstream>
#include <Eigen/Dense>

#include "CTMatrix.h"
#include "SIRT.h"
#include "PPMWriter.h"
#include "SART.h"

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


//    string systemMatrixFileName = "data/system_matrix_50_50.csv";
//    string projectionsFileName = "data/projections_50_50.csv";
//    shared_ptr<CTMatrix<float>> psCTSystemMatrix = make_shared<CTMatrix<float>>(systemMatrixFileName);
//    shared_ptr<CTMatrix<float>> psCTProjections = make_shared<CTMatrix<float>>(projectionsFileName);
//    int imageWidth = 50;
//    int imageHeight = 50;
//    float lambda = 1.9; // magic number
//    float esp = 0.01;
//    int maxRun = 100;
//    weak_ptr<CTMatrix<float>> pwSystemMatrix(psCTSystemMatrix);
//    weak_ptr<CTMatrix<float>> pwProjections(psCTProjections);
//    SIRT sirt(imageWidth, imageHeight, pwSystemMatrix, pwProjections, lambda, esp, maxRun);
//    sirt.run();

    string systemMatrixFileName = "data/system_matrix_128_128.csv";
    string projectionsFileName = "data/projections_128_128.csv";
    shared_ptr<CTMatrix<float>> psCTSystemMatrix = make_shared<CTMatrix<float>>(systemMatrixFileName, true);
    shared_ptr<CTMatrix<float>> psCTProjections = make_shared<CTMatrix<float>>(projectionsFileName, false);
    int imageWidth = 128;
    int imageHeight = 128;
    float lambda = 1.9; // magic number
    float esp = 0.01;
    int maxRun = 10;
    int nSensors = 75;
    int nProjections = 36;
    weak_ptr<CTMatrix<float>> pwSystemMatrix(psCTSystemMatrix);
    weak_ptr<CTMatrix<float>> pwProjections(psCTProjections);

    SART sart(imageWidth, imageHeight, pwSystemMatrix, pwProjections, lambda, esp, maxRun, nSensors, nProjections);

    SIRT sirt(imageWidth, imageHeight, pwSystemMatrix, pwProjections, lambda, esp, maxRun);
    for (int numOfRun = 0; numOfRun < 10; ++numOfRun) {
        cout << "Run " << numOfRun << endl;
        sart.run();
        sirt.run();
    }
//    MatrixXf testMatrix(4,4);
//    Matrix<float, Dynamic, Dynamic> tmp;
//    testMatrix << 1, 2, 3, 4,
//            5, 6, 7, 8,
//            9,10,11,12,
//            13,14,15,16;
//
//
//    cout << testMatrix.block(1, 1, 2, 2)/2 << endl;

    return 0;
}
