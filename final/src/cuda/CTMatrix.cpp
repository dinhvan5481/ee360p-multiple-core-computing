//
// Created by van quach on 11/24/16.
//

#include "CTMatrix.h"
#include <iostream>
#include <fstream>
#include <vector>
#include <Eigen/Dense>

using namespace std;

template <class T>
CTMatrix<T>::CTMatrix(string const& dataFileName) {
    this->csvFileName = dataFileName;
    ifstream csvDataFile(this->csvFileName);
    vector<T> results = getNextLineAndSplitIntoTokens(csvDataFile);
    this->nRows = (int)results.at(0);
    this->nCols = (int)results.at(1);

    while(!csvDataFile.eof()) {
        results = getNextLineAndSplitIntoTokens(csvDataFile);
        this->entries.insert(end(this->entries), begin(results), end(results));

    }
    this->rawData = Matrix<T, Dynamic, Dynamic, RowMajor>::Map(&this->entries[0], this->nRows, this->nCols);
}

template <class T>
CTMatrix<T>::CTMatrix(Matrix<T, Dynamic, Dynamic, RowMajor> data) {
    this->nCols = data.cols();
    this->nRows = data.rows();
    this->rawData = data;
}

template <class T>
int CTMatrix<T>::getNRows() {
    return this->nRows;
}

template <class T>
int CTMatrix<T>::getNColumns(){
    return this->nCols;
}


template <class T>
vector<T> CTMatrix<T>::getNextLineAndSplitIntoTokens(istream &str) {
    vector<T>   result;
    string line;
    getline(str,line);
    stringstream lineStream(line);
    T cell;
    while(lineStream >> cell)
    {
        result.push_back(cell);
        if(lineStream.peek() == ',') {
            lineStream.ignore();
        }
    }
    return result;
}

template class CTMatrix<float>;
template class CTMatrix<int>;