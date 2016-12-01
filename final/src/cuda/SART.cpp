//
// Created by van quach on 11/29/16.
//

#include "SART.h"
#include "PPMWriter.h"

SART::SART(int nRows, int nCols, weak_ptr<CTMatrix<float>> pSystemMatrix,
           weak_ptr<CTMatrix<float>> pProjections, float lambda, float esp,
           int maxRun, int nSensors, int nProjections)
        : nRows(nRows), nCols(nCols), maxRun(maxRun), nSensors(nSensors),
          nProjections(nProjections), lambda(lambda), esp(esp)
{
    this->pwSystemMatrix = pSystemMatrix;
    this->pwProjections = pProjections;
    result = MatrixXf::Zero(nRows*nCols, 1);
}

void SART::run() {
    shared_ptr<CTMatrix<float>> psSystemMatrix = this->pwSystemMatrix.lock();
    shared_ptr<CTMatrix<float>> psProjections = this->pwProjections.lock();
    if(psSystemMatrix && psProjections) {
        MatrixXf C, R;
        R = psSystemMatrix->rawData.rowwise().sum().asDiagonal().inverse();
        C = psSystemMatrix->rawData.transpose().rowwise().sum().asDiagonal().inverse();
        for (int rowIndex = 0; rowIndex < R.rows(); ++rowIndex) {
            if(std::isinf(R(rowIndex, rowIndex))) {
                R(rowIndex, rowIndex) = 0;
            }
        }
        for (int rowIndex = 0; rowIndex < C.rows(); ++rowIndex) {
            if(std::isinf(C(rowIndex, rowIndex))) {
                C(rowIndex, rowIndex) = 0;
            }
        }
        MatrixXf CATR = C * psSystemMatrix->rawData.transpose() * R;
        float a_sum = psSystemMatrix->rawData.sum();
        MatrixXf xTemp = MatrixXf::Zero(this->nRows*this->nCols, this->nProjections);
        float norm;
        for (int runIndex = 0; runIndex < this->maxRun; ++runIndex) {
            #pragma omp parallel for
            for (int theta = 0; theta < this->nProjections; ++theta) {
                int idx_start = this->nProjections * theta;
                float a_theta_sum = psSystemMatrix->rawData.block(idx_start, 0,
                                                                  this->nSensors, this->nCols * this->nRows).sum();
                xTemp.col(theta) = (psSystemMatrix->rawData.block(idx_start, 0,
                                                                 this->nSensors, this->nCols * this->nRows).transpose()
                                   * (psProjections->rawData.block(idx_start, 0, this->nSensors, 1)
                                      - psSystemMatrix->rawData.block(idx_start, 0,
                                                                      this->nSensors, this->nCols * this->nRows) * this->result)) / a_theta_sum;

            }
            result = result + CATR * (psProjections->rawData - psSystemMatrix->rawData * xTemp.rowwise().sum());
            result /= a_sum;
            norm = (psProjections->rawData - psSystemMatrix->rawData * result).norm();
            if(norm <= esp) {
                break;
            }
            
        }

        Map<MatrixXf> imgData(this->result.data(), this->nRows, this->nCols);
        ostringstream ss;
        ss << "sart_" << this->nRows << "_" << this->nCols << ".pgm";
        shared_ptr<MatrixXf> pImgData = make_shared<MatrixXf>(imgData);
        weak_ptr<MatrixXf> pwImgData(pImgData);
        PPMWriter::create(ss.str(), pwImgData, this->nRows, this->nCols);

    }
}
