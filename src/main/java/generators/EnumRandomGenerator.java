package generators;

public class EnumRandomGenerator<T extends Enum<T>> extends AbstractRandomGenerator<T> {
    private final Class<T> enumClass;

    public EnumRandomGenerator(Class<T> enumClass){
        this.enumClass = enumClass;
    }

    @Override
    public T getRandom() {
        var enumConstants = enumClass.getEnumConstants();
        var randomNumber = random.nextInt(enumConstants.length);
        return enumConstants[randomNumber];
    }
}
