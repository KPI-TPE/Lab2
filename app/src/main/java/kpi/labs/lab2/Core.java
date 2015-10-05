package kpi.labs.lab2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Beohrn on 22.09.2015.
 */
public class Core {

    private String text = new String("");
    private int minX1;
    private int minX2;
    private int maxX1;
    private int maxX2;
    private int minY;
    private int maxY;

    private int[][] tableX = {{-1, 1, -1},{-1, -1, 1}};
    private int[][] tableY;
    private double[] vectorY = new double[3];
    private double[] sigma = new double[3];
    private double Q;
    private double[] Fuv = new double[3];
    private double[] Quv = new double[3];
    private double[] Ruv = new double[3];
    private double dx1;
    private double dx2;
    private double x10;
    private double x20;
    private double a0;
    private double a1;
    private double a2;

    boolean flag = true;
    int n = 3;
    int m = 5;

    List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }


    public Core(int minX1, int minX2, int maxX1, int maxX2, int numberV) {
        this.minX1 = minX1;
        this.minX2 = minX2;
        this.maxX1 = maxX1;
        this.maxX2 = maxX2;
        this.maxY = (30 - numberV) * 10;
        this.minY = (20 - numberV) * 10;
        tableY = new int[n][m];
    }

    public void calculation() {
        while(flag) {
            generateY();
            meanY();

            disperse();
            mainDevivation();

            Fuv[0] = sigma[0] / sigma[1];
            Fuv[1] = sigma[2] / sigma[0];
            Fuv[2] = sigma[2] / sigma[1];

            for (int i = 0; i < n; i++) {
                Quv[i] = (m - 2 / m) * Fuv[i];
            }

            for (int i = 0; i < n; i++) {
                Ruv[i] = Math.abs((Quv[i] - 1) / Q);
            }

            if (Ruv[0] < 2.16 & Ruv[1] < 2.16 & Ruv[2] < 2.16) {
                text = text +  "\nПЕРЕВІРИМО ОДНОРІДНІСТЬ ДИСПЕРСІЙ ЗА КРИТЕРІЄМ РОМАНОВСЬКОГО\n";

                text = text + "\n1) Знайдемо середнє значення функції відгуку в рядку: ";
                for (int i = 0; i < vectorY.length; i++) {
                    text = text + "\n<Y[" + i + "]> = " + vectorY[i] + ";";
                }

                text += "\n\n2) Знайдемо дисперсії по рядках:";
                for (int i = 0; i < sigma.length; i++) {
                    text += "\nsigma^2{y" + i + "} = " + sigma[i] + ";";
                }

                text += "\n\n3) Обчислимо основне відхилення:";
                text += "\nQ = " + Q;

                text += "\n\n4) Обчислимо Fuv:";
                for (int i = 0; i < Fuv.length; i++) {
                    text += "\nFuv[" + (i + 1) + "] = " + Fuv[i];
                }

                text += "\n\n5) :";
                for (int i = 0; i < Quv.length; i++) {
                    text += "\nQuv[" + (i + 1) + "] = " + Quv[i];
                }

                text += "\n\n6) :";
                for (int i = 0; i < Ruv.length; i++) {
                    text += "\nRuv[" + (i + 1) + "] = " + Ruv[i];
                }

                text += "\n\n<<<<Дисперсія однорідна!>>>>\n";


                //System.out.println("Дисперсія однорідна");
                flag = false;
            }
        }

        text = text +  "\nРОЗРАХУНОК НОРМОВАНИХ КОЕФІЦІЄНТІВ РІВНЯННЯ РЕГРЕСІЇ.\n";

        double mx1 = -0.33;
        double mx2 = - 0.33;
        double my = (vectorY[0] + vectorY[1] + vectorY[2]) / 3;
        double a1 = 1;
        double a2 = -0.33;
        double a3 = 1;
        text = text +  "\nmx1 = " + mx1 + "\nmx2 = " + mx2 + "\nmy = " + my +
                "\na1 = " + a1 + "\na2 = " + a2 + "\na3 = " + a3;
        double a11 = (tableX[0][0] * vectorY[0] + tableX[0][1] * vectorY[1] + tableX[0][2] * vectorY[2]) / 3;
        double a22 = (tableX[1][0] * vectorY[0] + tableX[1][1] * vectorY[1] + tableX[1][2] * vectorY[2]) / 3;
        text += "\n\na11 = " + a11 + "\na22 = " + a22;
        double div = costyl(1, mx1, mx2, mx1, a1, a2, mx2, a2, a3);

        double b0 = costyl(my, mx1, mx2, a11, a1, a2, a22, a2, a3) / div;
        double b1 = costyl(1, my, mx2, mx1, a11, a2, mx2, a22, a3) / div;
        double b2 = costyl(1, mx1, my, mx1, a1, a11, mx2, a2, a22) / div;
        text += "\n\nb0 = " + b0 + "\nb1 = " + b1 + "\nb2 = " + b2;
        double y1 = b0 + b1 * tableX[0][0] + b2 * tableX[1][0];
        double y2 = b0 + b1 * tableX[0][1] + b2 * tableX[1][1];
        double y3 = b0 + b1 * tableX[0][2] + b2 * tableX[1][2];
        text += "\n\nНормоване рівняння регресії:";
        text += "\ny = " + b0 + " + " + b1 + " * x1 " + b2 + " * x2";
        text += "\nЗробимо перевірку: ";
        text += "\ny1 = " + y1 + "\ny2 = " + y2 + "\ny3 = " + y3;
        //text = "y1 = " + y1 + "; y2 = " + y2 + "; y3 = " + y3 + ";";
        //System.out.println(text);

        text = text +  "\nПРОВЕДЕМО НАТУРАЛІЗАЦІЮ КОЕФІЦІЄНТІВ.\n";
        dx1 = Math.abs(maxX1 - minX1) / 2;
        dx2 = Math.abs(maxX2 - minX2) / 2;
        x10 = (maxX1 + minX1) / 2;
        x20 = (maxX2 +  minX2) / 2;

        this.a0 = b0 - b1 * (x10 / dx1) - b2 * (x20 / dx2);
        this.a1 = b1 / dx1;
        this.a2 = b2 / dx2;

        text += "\ndx1 = " + dx1 + "\ndx2 = " + dx2 + "\nx10 = " + x10 +
                "\nx20 = " + x20 + "\na0 = " + this.a0 + "\na1 = " + this.a1 +
                "\na2 = " + this.a2;

        text += "\n\nЗапишемо натуралізоване рівняння регресії:";
        double yn1 = this.a0 + this.a1 * minX1 + this.a2 * minX2;
        double yn2 = this.a0 + this.a1 * maxX1 + this.a2 * minX2;
        double yn3 = this.a0 + this.a1 * minX1 + this.a2 * maxX2;
        text += "\ny = " + this.a0 + " + " + this.a1 + " * x1 + " + this.a2 + " * x2";
        text += "\n\nЗробимо первіку по рядках:";
        text += "\ny1 = " + yn1 + "\ny2 = " + yn2 + "\ny3 = " + yn3;


        if ((int) y1 == (int) yn1 & (int) y2 == (int) yn2 & (int) y3 == (int) yn3) {
            text += "\n\n<<<<<Коефіцієнти натуралізованого рівняння співпадають>>>>>";
        } else {
            text += "\n\n<<<<<Коефіцієнти натуралізованого рівняння не співпадають>>>>>";
        }


    }


    private double costyl(double a00, double a01, double a02,
                          double a10, double a11, double a12,
                          double a20, double a21, double a22) {

        return a00 * a11 * a22 + a10 * a21 * a02 + a01 * a12 * a20 -
                a02 * a11* a20 - a21 * a12 * a00 - a10 * a01 * a22;

    }


    private void generateY() {
        for (int i = 0; i < tableY.length; i++) {
            for (int j = 0; j < tableY[i].length; j++) {
                tableY[i][j] = minY + (int) (Math.random() * ((maxY - minY) + 1));
                System.out.print(tableY[i][j]+ " ");

            }
            System.out.println();
        }
    }

    public String[] generateTable() {

        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < 3) {
            arrayList.add(Integer.toString(i + 1));
            arrayList.add(Integer.toString(tableX[0][i]));
            arrayList.add(Integer.toString(tableX[1][i]));
            for (int l = 0; l < tableY[i].length; l++) {
                arrayList.add(Integer.toString(tableY[i][l]));
            }
            i++;
        }

        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private void meanY() {
        for (int i = 0; i < tableY.length; i++) {
            for (int j = 0; j < tableY[i].length; j++) {
                vectorY[i] += tableY[i][j];
            }
            vectorY[i] /= m;
            System.out.println("<Y[" + i + "]> = " + vectorY[i]);
        }
    }

    private void disperse() {
        for (int i = 0; i < tableY.length; i++) {
            for (int j = 0; j < tableY[i].length; j++) {
                sigma[i] += Math.pow(tableY[i][j] - vectorY[i], 2);
            }
            sigma[i] /= m;
            System.out.println("sigma[" + i + "] = " + sigma[i]);
        }
    }

    private void mainDevivation() {
        Q = Math.sqrt(2 * (2 * m - 2) / m / (m - 4));
        System.out.println("Q = " + Q);
    }

    public String getText() {
        return text;
    }

}
