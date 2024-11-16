package generators;

import java.util.Map;

public class PercentageRandomGenerator<T> extends AbstractRandomGenerator<T>{
    private static final float EPSILON = 0.000001f;
    private static final float HUNDRED_PERCENT = 100f;
    private static final float NORMALIZATION_COEFFICIENT = 0.01f;
    private static final String CHANCE_PERCENT_OVER_HUNDRED_MESSAGE = "total chance needs to be exactly 100%%. Provided: %.5f%%";
    private static final String NEGATIVE_PERCENT_CHANCE_ENCOUNTERED_MESSAGE = "chance cannot be negative. For item '%s' chance provided was: %.5f%%";

    protected final Map<Float, T> resultsByChance;
    /***
     * returns T based on chances provided. total chance cannot exceed 100%.
     * If chances are less than 100%, null can be returned as result
     */
    public PercentageRandomGenerator(Map<Float,T> resultsByChance){
        validateResultsByChance(resultsByChance);
        this.resultsByChance = resultsByChance;
    }

    @Override
    public T getRandom() {
        var lowerBound = 0f;
        var randomFloat = random.nextFloat();
        T currentItem = null;
        for(Map.Entry<Float, T> entry : resultsByChance.entrySet()){
            var normalizedItemProbability = entry.getKey()*NORMALIZATION_COEFFICIENT;
            currentItem = entry.getValue();
            if(randomFloat >= lowerBound && randomFloat <= lowerBound + normalizedItemProbability){
                return currentItem;
            }
            lowerBound += normalizedItemProbability;
        }
        return currentItem;
    }

    public static <T> void validateResultsByChance(Map<Float, T> resultsByChance){
        float totalProbability = 0f;
        for (Float probability : resultsByChance.keySet()) {
            if (probability < 0) {
                throw new IllegalArgumentException(String.format(NEGATIVE_PERCENT_CHANCE_ENCOUNTERED_MESSAGE,resultsByChance.get(probability),probability));
            }
            totalProbability += probability;
        }
        if(Math.abs(totalProbability - HUNDRED_PERCENT) > EPSILON){
            throw new IllegalArgumentException(String.format(CHANCE_PERCENT_OVER_HUNDRED_MESSAGE,totalProbability));
        }
    }
}
