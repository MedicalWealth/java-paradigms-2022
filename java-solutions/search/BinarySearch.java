package search;

public class BinarySearch {

    // let descendingSortedArray(int[] a, int pos): for i=pos..(a.size - 2): a[i] >= a[i + 1]
    // let a[-1] = inf && a[a.size] = -inf
    // let immutable(Object[] a): for i=0..(n - 1): a'[i] == a[i]

    // Contract
    // Pred: (for i=0..(args.size - 1): args[i] is integer) && (descendingSortedArray(int(args), 1))
    // Post: print(min(i)) && int(args[i + 1]) <= int(args[0]) && i=1..(args.size)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        // immutable(args)
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        // descendingSortedArray(a, 0) && immutable(a)
        // System.out.println(recursiveBinarySearch(a, x));
        System.out.println(iterativeBinarySearch(a, x));
    }

    // Contract
    // Pred: descendingSortedArray(a, 0)
    // Post: R == min(i) && a[i] <= x && i=0..a.size
    public static int iterativeBinarySearch(final int[] a, final int x) {
        // descendingSortedArray(a, 0) && immutable(a)
        int l = -1, r = a.length;
        // a[l] == inf, a[r] == -inf
        // r == r' && l == l'
        // a[r'] <= x && a[l'] > x && l' < r'
        while (r - l > 1) {
            // a[r'] <= x && a[l'] > x && l' + 1 < r'
            int m = (l + r) / 2;
            // a[r'] <= x && a[l'] > x && l' + 1 < r' && m' == m
            // a[m'] <= x || a[m'] > x
            if (a[m] <= x) {
                r = m;
                // (r' == m' && a[r'] <= x && a[l'] > x && l' < r') && immutable(a)
            } else {
                // (l' == m' && a[r'] <= x && a[l'] > x && l' < r') && immutable(a)
                l = m;
            }
        }
        // (a[r'] <= x && a[l'] > x) => r' == min(i)
        return r;
    }

    // Contract
    // Pred: descendingSortedArray(a, 0)
    // Post: R == min(i) && a[i] <= x && i=0..a.size
    public static int recursiveBinarySearch(final int[] a, final int x) {
        return recursiveBinarySearch(a, x, -1, a.length);
    }

    // Contract
    // Pred: descendingSortedArray(a, 0) && l < r &&
    //       a[r] <= x && x < a[l]
    // Post: R == min(i) && a[i] <= x && i == {0, ..., a.size}
    private static int recursiveBinarySearch(final int[] a, final int x, final int l, final int r) {
        // l' == l && r' == r
        // l' == r' - 1 || l' < r' - 1
        if (r - l == 1) {
            // (a[r'] <= x && x < a[l'] && l' == r' - 1) => r = min(i)
            return r;
        }
        // l' < r' - 1
        int m = (l + r) / 2;
        // m' == m
        // l' <= l' / 2 + r' / 2 == m' < r'
        // (a[m'] <= x || a[m'] > x) && immutable(a)
        if (a[m] <= x) {
            // l' < m' && a[m'] <= x && x < a[l']
            // immutable(a) && descendingSortedArray(a, 0) && l' < m' && a[m'] <= x && x < a[l']
            return recursiveBinarySearch(a, x, l, m);
        } else {
            // r' > m' && a[r'] <= x && x < a[m']
            // immutable(a) && descendingSortedArray(a, 0) && m' < r' && a[r'] <= x && x < a[m']
            return recursiveBinarySearch(a, x, m, r);
        }
    }
}
