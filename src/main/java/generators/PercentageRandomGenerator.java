package generators;

import data.ChanceEntry;

import java.util.List;

public class PercentageRandomGenerator<T> extends AbstractRandomGenerator<T>{
    private static final float EPSILON = 0.000001f;
    private static final float HUNDRED_PERCENT = 100f;
    private static final float NORMALIZATION_COEFFICIENT = 0.01f;
    private static final String CHANCE_PERCENT_OVER_HUNDRED_MESSAGE = "total chance needs to be exactly 100%%. Provided: %.5f%%";
    private static final String NEGATIVE_PERCENT_CHANCE_ENCOUNTERED_MESSAGE = "chance cannot be negative. For item '%s' chance provided was: %.5f%%";

    protected final List<ChanceEntry<T>> resultsByChance;
    /***
     * returns T based on chances provided. total chance cannot exceed 100%.
     */
    public PercentageRandomGenerator(List<ChanceEntry<T>> resultsByChance){
        validateResultsByChance(resultsByChance);
        this.resultsByChance = resultsByChance;
    }

    @Override
    public T getRandom() {
        var lowerBound = 0f;
        var randomFloat = random.nextFloat();
        T currentItem = null;
        for(final var entry : resultsByChance){
            var normalizedItemProbability = entry.getChance()*NORMALIZATION_COEFFICIENT;
            currentItem = entry.getItem();
            if(randomFloat >= lowerBound && randomFloat <= lowerBound + normalizedItemProbability){
                return currentItem;
            }
            lowerBound += normalizedItemProbability;
        }
        return currentItem;
    }

    private void validateResultsByChance(List<ChanceEntry<T>> resultsByChance){
        float totalProbability = 0f;
        for (final var chanceEntry : resultsByChance) {
            float probability = chanceEntry.getChance();
            if (probability < 0) {
                throw new IllegalArgumentException(String.format(NEGATIVE_PERCENT_CHANCE_ENCOUNTERED_MESSAGE,chanceEntry.getItem(),probability));
            }
            totalProbability += probability;
        }
        if(Math.abs(totalProbability - HUNDRED_PERCENT) > EPSILON){
            throw new IllegalArgumentException(String.format(CHANCE_PERCENT_OVER_HUNDRED_MESSAGE,totalProbability));
        }
    }
}
