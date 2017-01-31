/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.liaocy.ml4j.math;

import Jama.Matrix;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liaocy
 */
public class MatrixOpr {

    private static double[][] combine(double m0[][], double m1[][]) {
        double result[][] = new double[m0.length + m1.length][];
        for (int i = 0; i < m0.length; i++) {
            result[i] = m0[i].clone();
        }
        for (int i = 0; i < m1.length; i++) {
            result[m0.length + i] = m1[i].clone();
        }
        return result;
    }

    public static Matrix joinRow(Matrix m0, Matrix m1) {
        return new Matrix(combine(m0.getArray(), m1.getArray()));
    }
    
    public static void printMatrixToCSV(String path, Matrix m, List<String> rowDescriptions) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8");
            double[][] matrix = m.getArray();
            String str = "";
            int i = 0;
            for (double[] row : matrix) {
                str = "";
                for (double cell : row) {
                    str += cell + ",";
                }
                if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                    writer.write(str + rowDescriptions.get(i++).replaceAll(",", " ") + "\r\n");
                }
            }
            writer.close();
        } catch (Exception ex) {

        }
    }

    public static void printMatrixToCSV(String path, Matrix m) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8");
            double[][] matrix = m.getArray();
            String str = "";
            for (double[] row : matrix) {
                str = "";
                for (double cell : row) {
                    str += cell + ",";
                }
                if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                    writer.write(str.substring(0, str.length() - 1) + "\r\n");
                }
            }
            writer.close();
        } catch (Exception ex) {

        }
    }

    public static void printMatrixToCSV(String path, int id, Matrix m) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8");
            double[][] matrix = m.getArray();
            String str = "";
            for (double[] row : matrix) {
                str = id + ",";
                for (double cell : row) {
                    str += cell + ",";
                }
                if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                    writer.write(str.substring(0, str.length() - 1) + "\r\n");
                }
            }
            writer.close();
        } catch (Exception ex) {

        }
    }
}
