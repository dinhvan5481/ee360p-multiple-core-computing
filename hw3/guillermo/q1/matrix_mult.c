#include <omp.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void print_matrix(int **matrix, int rows, int cols) {
    int r, c;
    printf("%d %d\n", rows, cols);
    for(r = 0; r < rows; r++) {
        for(c = 0; c < cols; c++){
            printf((c == cols - 1)? "%d\n" : "%d ", matrix[r][c]);
        }
    }
} 

void load_matrix(FILE *file, int ***matrix, int *rows, int *cols) {
    fscanf(file, "%d %d", rows, cols);
    *matrix = malloc(*rows * sizeof(int*));
    int r, c;
    for(r = 0; r < *rows; r++) {
        (*matrix)[r] = malloc(*cols * sizeof(int));
        for(c = 0; c < *cols; c++) {
            int value = 0;
            fscanf(file, "%d", &value);
            (*matrix)[r][c] = value;
        }
    }
}

void mult_matrix(int **matrix1, int mat1_rows, int mat1_cols, int **matrix2, int mat2_rows, int mat2_cols, int ***matrix3) {
    int m3_r = mat1_rows, m3_c = mat2_cols;
    int r, k;
    #pragma omp parallel for
    for(r = 0; r < m3_r; r++) {
        int k = 0, j;
        for(k = 0; k < m3_c; k++) {
            int sum = 0;
            for(j = 0; j < mat2_cols; j++) {
                sum += matrix1[r][j] * matrix2[j][k];
            }
            (*matrix3)[r][k] = sum;
        }
    }
}

int main(int argc, char **argv) {

    char * file_name1 = argv[1];
    char * file_name2 = argv[2];
    int thread_to_use = -1;
    int **matrix1;
    int **matrix2;

    if(argc != 4) {
        fprintf(stderr, "Usage: %s file1 file2 T\n", argv[0]);
        return 1;
    }
    
    if(!sscanf(argv[3], "%d", &thread_to_use)) {
        fprintf(stderr, "T must be a number.");
        return 2;
    }

    FILE* file1 = fopen (file_name1, "r");
    FILE* file2 = fopen (file_name2, "r");

    if(file1 == NULL) {
        fprintf(stderr, "File 1 %s, doesn't exist.\n", file_name1);
    }

    if(file2 == NULL) {
        fprintf(stderr, "File 2 %s, doesn't exist.\n", file_name2);
    }

    if(file1 == NULL || file2 == NULL) return 3;
    
    int m1_r, m1_c;
    int m2_r, m2_c;
    int r, c;
    clock_t start_time, end_time;

    load_matrix(file1, &matrix1, &m1_r, &m1_c);
    load_matrix(file2, &matrix2, &m2_r, &m2_c);
    
    fclose(file1);
    fclose(file2);

    int m3_r = m1_r, m3_c = m2_c;
    int **m3 = malloc(m3_r * sizeof(int*));
    for(r = 0; r < m3_r; r++) {
        m3[r] = malloc(m3_c * sizeof(int));
    }

    if(thread_to_use == 0) {
        const int THREAD_OPTIONS = 4; 
        int threads[] = {1, 2, 4, 8};
        
        int times = 1000;
        int t_count, thread_index;
        clock_t time;
        for(thread_index = 0; thread_index < THREAD_OPTIONS; thread_index++) {
            time = 0;
            omp_set_num_threads(threads[thread_index]);
            for(t_count = 0; t_count < times; t_count++) {
                start_time = clock();
                mult_matrix(matrix1, m1_r, m1_c, matrix2, m2_r, m2_c, &m3);
                end_time = clock();
                time += end_time - start_time;
            }
            printf("%d, %f\n", threads[thread_index], (float) (time) / CLOCKS_PER_SEC);
        }
    } else {
        omp_set_num_threads(thread_to_use);
        mult_matrix(matrix1, m1_r, m1_c, matrix2, m2_r, m2_c, &m3);
        print_matrix(m3, m3_r, m3_c);
    }

    return 0;
}