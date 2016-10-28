#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

double randd(int r) {
    double random = 1.0 * r / RAND_MAX;
    random -= 0.5; // Center it.
    random *= 2.0; // Expand it and now is [-1, 1];
    return random;
}

double MonteCarloPI(int s) {
    int i, c = 0;
    const double RADIUS = 1.0;
    srand(time(NULL));

    #pragma omp parallel for reduction (+:c)
    for(i = 0; i < s; i++) {
        
        int xr = rand(), yr = rand();
        double x = randd(xr) * RADIUS, y = randd(yr) * RADIUS;
        // printf("x: %lf, y: %lf", x, y);
        if(x * x + y * y < RADIUS * RADIUS) {
            c++;
            // printf(" [In circle (%d)]", c);
        }
        // printf("\n");
    }

    // c / s = Pi / 4
    return 4.0 * c / s;
}

int main() {

    int s = 100000000;
    
    printf("MonteCarlo PI Approximation: S -> %d\n", s);
    // scanf("%d", &s);
    double pi = MonteCarloPI(s);
    printf("Calculated PI: %lf\n", pi);

    return 0;
}