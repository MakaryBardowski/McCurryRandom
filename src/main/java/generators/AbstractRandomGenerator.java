package generators;

import java.util.Random;

public abstract class AbstractRandomGenerator<T> implements RandomGenerator<T> {
    protected final Random random = new Random();
}
