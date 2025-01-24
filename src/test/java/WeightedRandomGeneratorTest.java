import data.ChanceEntry;
import generators.WeightedRandomGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedRandomGeneratorTest {

    private static final int MAX_RETRIES = 100000;

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidatingWeightIsZero(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(0f,"zwykly miecz"),
                new ChanceEntry<>(0f,"rzadki miecz")
        );

        //then
        var exception = assertThrows(IllegalArgumentException.class, () -> new WeightedRandomGenerator<>(itemsByChance));
        assertEquals("Total weight needs to over 0.",exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidatingOneOfWeightsProvidedBelowZero(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(80f,"zwykly miecz"),
                new ChanceEntry<>(-15f,"rzadki miecz")
        );

        //then
        var exception = assertThrows(IllegalArgumentException.class, () -> new WeightedRandomGenerator<>(itemsByChance));
        assertEquals("Weight cannot be negative. For item 'rzadki miecz' weight provided was: -15,00000%",exception.getMessage());
    }

    @Test
    void shouldValidateChanceEqualToHundred(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(80f,"zwykly miecz"),
                new ChanceEntry<>(20f,"rzadki miecz")
        );
        //then
        assertDoesNotThrow(() -> new WeightedRandomGenerator<>(itemsByChance));
    }

    @Test
    void shouldNeverReturnNullWhenNullNotProvidedInResultTable(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(40f,"zwykly miecz"),
                new ChanceEntry<>(20f,"rzadki miecz")
        );
        var gen = new WeightedRandomGenerator<>(itemsByChance);

        var nullResultCount = 0;
        var normalSwordCount = 0;
        var rareSwordCount = 0;

        // then
        for(int i = 0; i < MAX_RETRIES; i++){
            var result = gen.getRandom();
            if(result.equals("zwykly miecz")){
                normalSwordCount++;
                continue;
            }
            rareSwordCount++;
        }

        assertEquals(0,nullResultCount);
        assertTrue(normalSwordCount <= MAX_RETRIES*0.71f && normalSwordCount >= MAX_RETRIES*0.61f);
        assertTrue(rareSwordCount <= MAX_RETRIES*0.38f && rareSwordCount >= MAX_RETRIES*0.28f);
    }
}
