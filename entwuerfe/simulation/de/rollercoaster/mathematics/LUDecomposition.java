
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* Modified by Christian Mangelsdorf */
package de.rollercoaster.mathematics;

public class LUDecomposition {

    private static final double SINGULARITY_THRESHOLD = 10E-12;
    private double lu[][];
    private int[] pivot;

    public LUDecomposition(double[][] A) {
        final int n = A.length;

        pivot = new int[n];

        for (int row = 0; row < n; row++) {
            pivot[row] = row;
        }

        lu = new double[n][n];

        for (int row = 0; row < n; row++) {
            if (A[row].length != n) {
                throw new IllegalArgumentException("Matrix is not square");
            }
            System.arraycopy(A[row], 0, lu[row], 0, n);
        }

        // Loop over columns
        for (int col = 0; col < n; col++) {

            double sum = 0;

            // upper
            for (int row = 0; row < col; row++) {
                final double[] luRow = lu[row];
                sum = luRow[col];
                for (int i = 0; i < row; i++) {
                    sum -= luRow[i] * lu[i][col];
                }
                luRow[col] = sum;
            }

            // lower
            int max = col; // permutation row
            double largest = Double.NEGATIVE_INFINITY;
            for (int row = col; row < n; row++) {
                final double[] luRow = lu[row];
                sum = luRow[col];
                for (int i = 0; i < col; i++) {
                    sum -= luRow[i] * lu[i][col];
                }
                luRow[col] = sum;

                // maintain best permutation choice
                if (Math.abs(sum) > largest) {
                    largest = Math.abs(sum);
                    max = row;
                }
            }


            // Singularity check
            if (Math.abs(lu[max][col]) < SINGULARITY_THRESHOLD) {
                throw new IllegalArgumentException("Singular matrix");
            }

            // Pivot if necessary
            if (max != col) {
                double tmp = 0;
                final double[] luMax = lu[max];
                final double[] luCol = lu[col];
                for (int i = 0; i < n; i++) {
                    tmp = luMax[i];
                    luMax[i] = luCol[i];
                    luCol[i] = tmp;
                }
                int temp = pivot[max];
                pivot[max] = pivot[col];
                pivot[col] = temp;
            }

            // Divide the lower elements by the "winning" diagonal elt.
            final double luDiag = lu[col][col];
            for (int row = col + 1; row < n; row++) {
                lu[row][col] /= luDiag;
            }
        }
    }

    public double[] solve(double[] b)
            throws IllegalArgumentException {

        final int n = pivot.length;
        if (b.length != n) {
            throw new IllegalArgumentException("Vector length mismatch");
        }


        final double[] bp = new double[n];

        // Apply permutations to b
        for (int row = 0; row < n; row++) {
            bp[row] = b[pivot[row]];
        }

        // Solve LY = b
        for (int col = 0; col < n; col++) {
            final double bpCol = bp[col];
            for (int i = col + 1; i < n; i++) {
                bp[i] -= bpCol * lu[i][col];
            }
        }

        // Solve UX = Y
        for (int col = n - 1; col >= 0; col--) {
            bp[col] /= lu[col][col];
            final double bpCol = bp[col];
            for (int i = 0; i < col; i++) {
                bp[i] -= bpCol * lu[i][col];
            }
        }

        return bp;

    }
}
