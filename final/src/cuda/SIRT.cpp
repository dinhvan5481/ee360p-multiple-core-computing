//
// Created by van quach on 11/26/16.
//

#include "SIRT.h"

#include "PPMWriter.h"

SIRT::SIRT(int width, int height, weak_ptr<CTMatrix<float>> pSystemMatrix, weak_ptr<CTMatrix<float>> pProjections, float lambda, float esp, int maxRun) {
    this->pSystemMatrix = pSystemMatrix;
    this->pProjections = pProjections;
    this->lambda = lambda;
    this->esp = esp;
    this->maxRun = maxRun;
    this->width = width;
    this->height = height;
    result = MatrixXf::Zero(width * height, 1);

    shared_ptr<CTMatrix<float>> psSystemMatrix = this->pSystemMatrix.lock();
    shared_ptr<CTMatrix<float>> psProjections = this->pProjections.lock();
    if(psSystemMatrix && psProjections) {
        Matrix<float, Dynamic, Dynamic> tmp = psProjections->rawData;
        Map<VectorXf> projections(tmp.data(), tmp.size());

        this->R = psSystemMatrix->rawData.rowwise().sum().asDiagonal().inverse();
        this->C = psSystemMatrix->rawData.transpose().rowwise().sum().asDiagonal().inverse();
        for (int rowIndex = 0; rowIndex < R.rows(); ++rowIndex) {
            if (std::isinf(R(rowIndex, rowIndex))) {
                R(rowIndex, rowIndex) = 0;
            }
        }
        for (int rowIndex = 0; rowIndex < C.rows(); ++rowIndex) {
            if (std::isinf(C(rowIndex, rowIndex))) {
                C(rowIndex, rowIndex) = 0;
            }
        }

        this->CATR = C * psSystemMatrix->rawData.transpose() * R;
    }
}

void SIRT::run() {
    cout << "Start run SIRT" << endl;
    clock_t begin = clock();
    shared_ptr<CTMatrix<float>> psSystemMatrix = this->pSystemMatrix.lock();
    shared_ptr<CTMatrix<float>> psProjections = this->pProjections.lock();
    if(psSystemMatrix && psProjections) {
        float norm;
        Matrix<float, Dynamic, Dynamic> tmp = psProjections->rawData;
        Map<VectorXf> projections(tmp.data(), tmp.size());

        for (int runIndex = 0; runIndex < this->maxRun; ++runIndex) {
            result = result + CATR * (projections - psSystemMatrix->rawData * result );
            norm = (projections - psSystemMatrix->rawData * result).norm();
            if(norm < this->esp) {
                break;
            }
        }
        clock_t end = clock();
        cout << "SIRT " << this->height << ": " << double(end - begin) / CLOCKS_PER_SEC << endl;
        Map<MatrixXf> imgData(this->result.data(), this->height, this->width);
        ostringstream ss;
        ss << "sirt_" << this->height << "_" << this->width << ".pgm";
        shared_ptr<MatrixXf> pImgData = make_shared<MatrixXf>(imgData);
        weak_ptr<MatrixXf> pwImgData(pImgData);
        PPMWriter::create(ss.str(), pwImgData, this->height, this->width);


    } else {
        cerr << "System matrix or projection matrix not existing" << endl;
    }



}