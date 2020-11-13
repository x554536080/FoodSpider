import javafx.scene.transform.Scale;

import java.util.Scanner;

public class nm {

    static int m;
    static int n;
    static int k;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        m = scanner.nextInt();
        n = scanner.nextInt();
        k = scanner.nextInt();
        System.out.println(findKthNumber(m,n,n*m+1-k));

    }

    static int findKthNumber(int m, int n, int k) {
        int left = 1 * 1;
        int right = m * n;
        while (left < right) {
            int mid = (left + right) >> 1;
            int count = findNum(mid, m, n);
            if (count < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return right;
    }

    static int findNum(int val, int m, int n) {
        int i = m;
        int j = 1;
        int sum = 0;
        while (i > 0 && j <= n) {
            if (i * j <= val) {
                sum += i;
                j++;
            } else {
                i--;
            }
        }
        return sum;
    }
}
