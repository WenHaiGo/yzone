package com.yzone.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

//        List<Integer> list = new LinkedList<>();
//
//        Scanner scanner = new Scanner(System.in);
//        int temp = scanner.nextInt();
        //int[] a = new int[]{1, 3, 5, 2, 4, 6, 5, 7, 8};
        int[] a = new int[] { 12, 1, 50, 8, 9, 8, 6, 7, 10, 3 };
        quickSort(a, 0, a.length - 1);
        for (int i : a) {
            System.out.print(i+" ");
        }
    }
    public static void quickSort(int[] a, int left, int right) {
        if (left < right) {
            int i = left;
            int j = right;
            int x = a[left];
            while (i < j) {
                while (i < j && a[j] >= x) {
                    j--;
                }
                if (i < j) a[i++] = a[j];
                while (i < j && a[i] < x) {
                    i++;
                }
                if (i < j) a[j--] = a[i];
            }
            a[i] = x;
            quickSort(a, left, i - 1);
            quickSort(a, i + 1, right);
        }
    }
}
