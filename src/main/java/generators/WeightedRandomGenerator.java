package generators;

import data.ChanceEntry;

import java.util.List;

public class WeightedRandomGenerator<T> extends AbstractRandomGenerator<T>{
    private static final String TOTAL_WEIGHT_ZERO_MESSAGE = "Total weight needs to over 0.";
    private static final String NEGATIVE_PERCENT_CHANCE_ENCOUNTERED_MESSAGE = "Weight cannot be negative. For item '%s' weight provided was: %.5f%%";

    protected final List<ChanceEntry<T>> resultsByChance;
    private float totalWeight;
    /***
     * returns T based on weights provided. total weight cannot be less than 0.
     * Providing two entries, 20 and 40, will make first item appear with 33.3% percent probability and second with 66.6%.
     */
    public WeightedRandomGenerator(List<ChanceEntry<T>> resultsByChance){
        validateResultsByChanceAndGetTotalWeight(resultsByChance);
        this.resultsByChance = resultsByChance;
    }

    @Override
    public T getRandom() {
        var lowerBound = 0f;
        var randomFloat = random.nextFloat(0, totalWeight);
        T currentItem = null;
        for(final var entry : resultsByChance){
            var itemWeight = entry.getChance();
            currentItem = entry.getItem();
            if(randomFloat >= lowerBound && randomFloat <= lowerBound + itemWeight){
                return currentItem;
            }
            lowerBound += itemWeight;
        }
        return currentItem;
    }

    private void validateResultsByChanceAndGetTotalWeight(List<ChanceEntry<T>> resultsByChance){
        float totalProbability = 0f;
        for (final var chanceEntry : resultsByChance) {
            float probability = chanceEntry.getChance();
            if (probability < 0) {
                throw new IllegalArgumentException(String.format(NEGATIVE_PERCENT_CHANCE_ENCOUNTERED_MESSAGE,chanceEntry.getItem(),probability));
            }
            totalProbability += probability;
        }
        if(totalProbability <= 0){
            throw new IllegalArgumentException(String.format(TOTAL_WEIGHT_ZERO_MESSAGE,totalProbability));
        }
        totalWeight = totalProbability;
    }
}
