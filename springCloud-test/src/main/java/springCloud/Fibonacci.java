package springCloud;

public class Fibonacci implements Generator<Integer> {

    private int count = 0;

    @Override
    public Integer next() {return fi(count ++);}

    private int fi(int i) {
        if (i < 2) {
            return 1;
        }
        return fi(i - 2) + fi(i - 1);
    }

    public static void main(String[] args) {
        Fibonacci fibonacci = new Fibonacci();
        for (int i = 0; i < 18; i++) {
            System.out.print(fibonacci.next() + " ");
        }
    }
}
