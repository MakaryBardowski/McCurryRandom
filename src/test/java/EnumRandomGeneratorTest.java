import generators.EnumRandomGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnumRandomGeneratorTest {

    @Test
    void shouldReturnRandomEnumValueGivenEnumClass(){
        //given
        var gen = new EnumRandomGenerator<>(SwordType.class);

        // when
        var result = gen.getRandom();

        //then
        assertNotNull(result);
    }

    private enum SwordType{
        SHORT_SWORD, LONGSWORD, BROADSWORD, GREATSWORD, RAPIER
    }
}
