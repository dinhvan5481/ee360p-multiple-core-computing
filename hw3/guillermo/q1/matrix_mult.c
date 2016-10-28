#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

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

    fscanf(file1, "%d %d", &m1_r, &m1_c);
    matrix1 = malloc(m1_r * sizeof(int*));
    for(r = 0; r < m1_r; r++) {
        matrix1[r] = malloc(m1_c * sizeof(int));
        for(c = 0; c < m1_c; c++) {
            fscanf(file1, "%d", &matrix1[r][c]);
        }
    }

    fscanf(file2, "%d %d", &m2_r, &m2_c);
    matrix2 = malloc(m2_r * sizeof(int*));
    for(r = 0; r < m2_r; r++) {
        matrix2[r] = malloc(m2_c * sizeof(int));
        for(c = 0; c < m2_c; c++) {
            fscanf(file2, "%d", &matrix2[r][c]);
        }
    }
    
    fclose(file1);
    fclose(file2);

    int m3_r = m1_r, m3_c = m2_c;
    int **m3 = malloc(m3_r * sizeof(int*));
    for(r = 0; r < m3_r; r++) {
        m3[r] = malloc(m3_c * sizeof(int));
    }

    #pragma omp parallel for
    for(r = 0; r < m3_r; r++) {
        int k = 0, j;
        for(k = 0; k < m3_c; k++) {
            int sum = 0;
            for(j = 0; j < m2_c; j++) {
                sum += matrix1[r][j] * matrix2[j][k];
            }
            m3[r][k] = sum;
        }
    }
    
    printf("%d %d\n", m3_r, m3_c);
    for(r = 0; r < m3_r; r++) {
        for(c = 0; c < m3_c; c++){
            printf((c == m3_c - 1)? "%d\n" : "%d ", m3[r][c]);
        }
    }

}