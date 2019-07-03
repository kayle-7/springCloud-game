package springCloud;

import java.util.Iterator;

public class Game {
}

class Lol {
    public static Iterable<Integer> makeA() {

        return new Iterable<Integer>() {

            @Override
            public Iterator<Integer> iterator() {
                return null;
            }
        };
    }
}