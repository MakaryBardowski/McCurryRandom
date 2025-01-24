import data.ChanceEntry;
import generators.PercentageRandomGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PercentageRandomGeneratorTest {

    private static final int MAX_RETRIES = 100000;

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidatingChanceExceedsHundred(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(80f,"zwykly miecz"),
                new ChanceEntry<>(30f,"rzadki miecz")
        );

        //then
        var exception = assertThrows(IllegalArgumentException.class, () -> new PercentageRandomGenerator<>(itemsByChance));
        assertEquals("total chance needs to be exactly 100%. Provided: 110,00000%",exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidatingChanceBelowHundred(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(80f,"zwykly miecz"),
                new ChanceEntry<>(15f,"rzadki miecz")
        );
        //then
        var exception = assertThrows(IllegalArgumentException.class, () -> new PercentageRandomGenerator<>(itemsByChance));
        assertEquals("total chance needs to be exactly 100%. Provided: 95,00000%",exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenValidatingChanceBelowZero(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(80f,"zwykly miecz"),
                new ChanceEntry<>(-15f,"rzadki miecz")
        );

        //then
        var exception = assertThrows(IllegalArgumentException.class, () -> new PercentageRandomGenerator<>(itemsByChance));
        assertEquals("chance cannot be negative. For item 'rzadki miecz' chance provided was: -15,00000%",exception.getMessage());
    }

    @Test
    void shouldValidateChanceEqualToHundred(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(80f,"zwykly miecz"),
                new ChanceEntry<>(20f,"rzadki miecz")
        );
        //then
        assertDoesNotThrow(() -> new PercentageRandomGenerator<>(itemsByChance));
    }

    @Test
    void shouldNeverReturnNullWhenNullNotProvidedInResultTable(){
        // given
        var itemsByChance = List.of(
                new ChanceEntry<>(80f,"zwykly miecz"),
                new ChanceEntry<>(20f,"rzadki miecz")
        );
        var gen = new PercentageRandomGenerator<>(itemsByChance);

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
        assertTrue(normalSwordCount <= MAX_RETRIES*0.85f && normalSwordCount >= MAX_RETRIES*0.75f);
        assertTrue(rareSwordCount <= MAX_RETRIES*0.25f && rareSwordCount >= MAX_RETRIES*0.15f);
    }
}
