package utils;

import com.project.model.Product;
import org.junit.Test;

import java.util.Arrays;

public class TestPrepare {

    static int i;

    public static int run() {
        return i;
    }

    @Test
    public void reverseList() {

        int list[] = {1, 2, 3, 4, 5};

        System.out.println("lenght : " + list.length);

        int low = 0, high = list.length - 1;

        while (low < high) {
            int tem = list[low];
            list[low++] = list[high];
            list[high--] = tem;

        }

        for (int j = 0; j < list.length; j++) {
            System.out.println("Reverse list: " + list[j]);
        }

        int a = 1;
        int b = a++;
        System.out.println("b " + b);

        Product p1 = new Product();
        p1.setId(1);

        Product p2 = p1;

        p2.setId(2);

        System.out.println("p1: " + p1);
        System.out.println("p2: " + p2);

    }

    @Test
    public void reverseList2() {

        int list[] = {1, 2, 3, 4, 5};
        int list2[] = new int[list.length];
        System.out.println("lenght : " + list.length);

        for (int i = 0; i <= list2.length - 1; i++) {
            list2[i] = list[list.length - 1 - i];
        }

        for (int j = 0; j < list2.length; j++) {
            System.out.println("Reverse list: " + list2[j]);
        }


    }

    @Test
    public void test2() {
        int size = 20;
        int t[] = new int[size];
        t[0] = 1;
        t[1] = 3;
        t[2] = 4;
        t[3] = 2;

        int i = 1, value = 9;

        for (int k = size - 2; k >= i; k--) {
            t[k + 1] = t[k];
        }
        t[i] = value;

        for (int j = 0; j < t.length; j++) {
            System.out.println(" list: " + t[j]);
        }
    }

    @Test
    public void test3() {
        int size = 20;
        int t[] = new int[size];


        t[0] = 0;
        t[1] = 0;
        t[2] = 0;
        t[3] = 0;
        t[4] = 1;
        t[5] = 3;
        t[6] = 4;
        t[7] = 2;

        int i = 4, value = 9;

        for (int k = 0; k < size - 1; k++) {
            t[k] = t[k + 1];
        }
        //t[i] = value;

        for (int j = 0; j < t.length; j++) {
            System.out.println(" list: " + t[j]);
        }
    }

    @Test
    public void test4() {
        int[] a = {1, 2, 3, 4, 5};

        int[] b = new int[a.length + a.length / 2];

        for (int i = 0; i <= a.length - 1; i++) {
            b[i] = a[i];
        }

        a = b;

        for (int j = 0; j < b.length; j++) {
            System.out.println(" list: " + a[j]);
        }
    }

    @Test
    public void test6() {
        int[] a = {7, 6, 1, 4, 2};

        for (int i = 1; i < a.length; i++) {
            int curr = a[i];
            int j = i;
            while (j > 0 && curr < a[j - 1]) {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = curr;
        }

        for (int j = 0; j < a.length; j++) {
            System.out.println(" list: " + a[j]);
        }
    }


    @Test
    public void test8() {
        int[] t = {8, 7, 3, 1, 5, 9, 8, 4, 0, 5, 10, 34, 99, 2};

        mergeSort(t);

        for (int j = 0; j < t.length; j++) {
            System.out.println(" list: " + t[j]);
        }
    }

    private void mergeSort(int[] t) {
        int n = t.length;
        if (t.length < 2) {
            return;
        }
        int mid = n / 2;
        int[] left = Arrays.copyOfRange(t, 0, mid);
        int[] right = Arrays.copyOfRange(t, mid, n);

        mergeSort(left);
        mergeSort(right);

        merge(t, left, right);
    }

    private void merge(int[] t, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                t[k] = left[i];
                i++;
            } else {
                t[k] = right[j];
                j++;
            }
            k++;
        }

        System.arraycopy(left, i, t, k, left.length - i);
        System.arraycopy(right, j, t, k, right.length - j);

    }

}
