//
// Created by van quach on 11/24/16.
//

#ifndef CUDA_CTMATRIX_H
#define CUDA_CTMATRIX_H

#include <iostream>
#include <vector>
#include <Eigen/Dense>
#include <Eigen/Core>

using namespace std;
using namespace Eigen;

template <class T>
class CTMatrix {
    public:
        CTMatrix(string const& dataFileName, bool isRowMajor);
        CTMatrix(Matrix<T, Dynamic, Dynamic, RowMajor> data);
        int getNRows();
        int getNColumns();
        Matrix<T, Dynamic, Dynamic, RowMajor> rawData;

    private:
        string csvFileName;
        int nRows;
        int nCols;

        vector<T> entries;
        vector<T> getNextLineAndSplitIntoTokens(istream& str);

//    MatrixXf A;
//    std::vector<float> entries;
//    int rows(0), cols(0);
//    while(...) { entries.push_back(...); /* update rows/cols*/ }
//    A = MatrixXf::Map(&entries[0], rows, cols);
};


#endif //CUDA_CTMATRIX_H
