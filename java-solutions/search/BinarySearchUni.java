package search;

public class BinarySearchUni {

    // let correctArrayUni(int[] a, min(i)): min(i) where i=0..a.size &&
    //     (for j=0..(i - 1): a[j] < a[j + 1] && a[i] >= a[i + 1] && for j=(i + 1)..(a.size - 2): a[j] > a[j + 1])
    // let correctArrayUni(int[] a, exists m): exists m >= 0:
    //     (for i=0..(m - 1): a[i] < a[i + 1] && a[m] >= a[m + 1] && for j=(m + 1)..(a.size - 2): a[j] > a[j + 1])
    // let correctArrayUni(int[] a, int l, int r): l < r &&
    //     (for i=0..(l - 1): a[i] < a[i + 1] && a[r] >= a[r + 1] && for j=(r + 1)..(a.size - 2): a[j] > a[j + 1])
    // let immutable(Object[] a): for i=0..(n - 1): a'[i] == a[i]
    // let a[-1] = -inf && a[a.size] = -inf

    // Contract
    // Pred: for i=0..(args.size - 1): args[i] is integer && correctArrayUni(int(args), exists i)
    // Post: print(min(i)) && correctArrayUni(int(args), min(i))
    public static void main(String[] args) {
        int sum = 0;
        // correctArrayUni(int(args), exists i) && immutable(args)
        final int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            a[i] = Integer.parseInt(args[i]);
            sum += a[i];
        }
        // from correctArrayUni(int(args), exists i) follows correctArrayUni(a, exists i)
//        if (sum % 2 == 0) {
//            // correctArrayUni(a, exists i)
//            System.out.println(recursiveSearch(a));
//        } else {
//            // correctArrayUni(a, exists i)
//            System.out.println(iterativeSearch(a));
//        }
        System.out.println(iterativeSearch(a));
    }

    // Contract
    // Pred: correctArrayUni(a, exists i)
    // Post: R == min(m) && correctArrayUni(a, min(m))
    private static int recursiveSearch(final int[] a) {
        return recursiveSearch(a, -1, a.length - 1);
    }


    // Contract
    // Pred: correctArrayUni(a, l, r) && l < r && a[l] < a[l + 1] && a[r] >= a[r + 1]
    // Post: R == min(m) && correctArrayUni(a, min(m)) && immutable(a)
    private static int recursiveSearch(final int[] a, final int l, final int r) {
        // l' == l && r' == r && immutable(a)
        if (r - l == 1) {
            // r' == l' + 1 => correctArrayUni(a, l', l' + 1) => R == l' - 0 + 1 = l' + 1
            return l + 1;
        }
        // m' == m
        final int m = (l + r) / 2;
        // final int m = l;
        // (a[m'] > a[m' + 1] || a[m'] <= a[m' + 1]) && immutable(a)
        if (a[m] > a[m + 1]) {
            // (a[m'] > a[m' + 1] && correctArrayUni(a, l, r)) => r' == m' && correctArrayUni(a, l', m')
            return recursiveSearch(a, l, m);
        } else {
            // (a[m'] <= a[m' + 1] && correctArrayUni(a, l, r)) => l' == m' && correctArrayUni(a, m', r')
            return recursiveSearch(a, m, r);
        }
    }

    // Contract
    // Pred: correctArrayUni(a, exists m)
    // Post: R == min(m) && correctArrayUni(a, min(m))
    private static int iterativeSearch(final int[] a) {
        // immutable(a)
        int l = -1, r = a.length - 1;
        while (r - l > 1) {
            // m' == m && immutable(a)
            int m = (l + r) / 2;
            // (a[m'] > a[m' + 1] || a[m'] <= a[m' + 1]) && immutable(a)
            if (a[m] > a[m + 1]) {
                // (a[m'] > a[m' + 1] && correctArrayUni(a, l, r)) => r == m' && correctArrayUni(a, l, m')
                r = m;
            } else {
                // (a[m'] <= a[m' + 1] && correctArrayUni(a, l, r)) => l == m && correctArrayUni(a, m', r)
                l = m;
            }
        }
        // r == l + 1 => correctArrayUni(a, l, l + 1) => R == l - 0 + 1 = l + 1
        return l + 1;
    }
}
