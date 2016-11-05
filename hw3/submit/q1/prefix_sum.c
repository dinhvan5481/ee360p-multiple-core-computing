#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

void parallelPrefixSum(double * a, int count) {
    int i;
    double * b = malloc(count * sizeof(double));

    #pragma omp parallel for
    for(i = 0; i < count; i++) {
        b[i] = a[i];
    }

    #pragma omp parallel 
    {
        #pragma omp for nowait
        for (i = 1; i < count; i++) {
            while(i != 1 && a[i - 1] == b[i - 1]) {
                // A little busy wait for the partner.
                #pragma omp taskyield
            }
            // printf("[%d]i: %d\n", omp_get_thread_num(), i);
            a[i] = a[i - 1] + a[i];
        }
    }

    free(b);
}

int main() {
    double a[] = {1, 4, 9, 16};
    int a_size = 4;
    int i = 0;
    parallelPrefixSum(a, a_size);
    for(;i < a_size; i++) {
        printf("%f ", a[i]);
    }
    printf("\n");
    return 0;
}