package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DishTest {

  private Dish.Builder builder;

  @BeforeEach
  void setup() {
    builder = new Dish.Builder();
  }

  @Test
  void test_buildDish() {
    Dish dish =
        builder
            .setType(DishType.MAIN)
            .setName("Maultaschen mit Specksoße auf Sauerkraut")
            .setAdditives(Set.of(Additive.B, Additive.C, Additive.H))
            .setAllergens(Set.of(Allergen.AA, Allergen.C, Allergen.G, Allergen.I, Allergen.P))
            .setTags(Set.of(Tag.S))
            .setStudentPrice(BigDecimal.valueOf(2.00))
            .setStaffPrice(BigDecimal.valueOf(2.80))
            .setGuestPrice(BigDecimal.valueOf(3.60))
            .setDate(LocalDate.of(2021, 9, 10))
            .build();

    assertAll(
        () -> assertEquals(DishType.MAIN, dish.type()),
        () -> assertEquals("Maultaschen mit Specksoße auf Sauerkraut", dish.name()),
        () -> assertEquals(Set.of(Additive.B, Additive.C, Additive.H), dish.additives()),
        () ->
            assertEquals(
                Set.of(Allergen.AA, Allergen.C, Allergen.G, Allergen.I, Allergen.P),
                dish.allergens()),
        () -> assertEquals(Set.of(Tag.S), dish.tags()),
        () -> assertEquals(BigDecimal.valueOf(2.00), dish.studentPrice()),
        () -> assertEquals(BigDecimal.valueOf(2.80), dish.staffPrice()),
        () -> assertEquals(BigDecimal.valueOf(3.60), dish.guestPrice()),
        () -> assertEquals(Set.of(Tag.S), dish.tags()),
        () -> assertEquals(BigDecimal.valueOf(2.00), dish.studentPrice()),
        () -> assertEquals(BigDecimal.valueOf(2.80), dish.staffPrice()),
        () -> assertEquals(BigDecimal.valueOf(3.60), dish.guestPrice()),
        () -> assertEquals(LocalDate.of(2021, 9, 10), dish.date()));
  }

  @Test
  void test_DishBuilder_invalidStudentPrice() {
    assertThrows(IllegalStateException.class, () -> builder.setStudentPrice(BigDecimal.ZERO));
  }

  @Test
  void test_DishBuilder_invalidStaffPrice() {
    assertThrows(IllegalStateException.class, () -> builder.setStaffPrice(BigDecimal.ZERO));
  }

  @Test
  void test_DishBuilder_invalidGuestPrice() {
    assertThrows(IllegalStateException.class, () -> builder.setGuestPrice(BigDecimal.ZERO));
  }
}
