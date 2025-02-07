package com.example.bridgeorbust;

import java.util.Scanner;

public class SolveLinearSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get matrix size
        System.out.print("Enter the size of the matrix (n x n): ");
        int n = scanner.nextInt();

        double[][] A = new double[n][n];
        double[] b = new double[n];

        // Input matrix A
        System.out.println("Enter the coefficients of the matrix A (row by row):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = scanner.nextDouble();
            }
        }

        // Input vector b
        System.out.println("Enter the values of the right-hand side vector b:");
        for (int i = 0; i < n; i++) {
            b[i] = scanner.nextDouble();
        }

        // Solve Ax = b
        double[] solution = solveGaussian(A, b);

        // Display solution
        System.out.println("Solution:");
        for (int i = 0; i < n; i++) {
            System.out.printf("x[%d] = %.5f\n", i, solution[i]);
        }

        scanner.close();
    }

    public static double[] solveGaussian(double[][] A, double[] b) {
        int n = A.length;

        for (int i = 0; i < n; i++) {
            // Find the pivot row
            int max = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[k][i]) > Math.abs(A[max][i])) {
                    max = k;
                }
            }

            // Swap rows in A and b
            double[] temp = A[i];
            A[i] = A[max];
            A[max] = temp;

            double t = b[i];
            b[i] = b[max];
            b[max] = t;

            // Make the pivot 1 and eliminate below
            for (int k = i + 1; k < n; k++) {
                double factor = A[k][i] / A[i][i];
                b[k] -= factor * b[i];
                for (int j = i; j < n; j++) {
                    A[k][j] -= factor * A[i][j];
                }
            }
        }

        // Back-substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }

        return x;
    }
}
