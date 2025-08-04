package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/** Models a single dish. */
record Dish(
    DishType type,
    String name,
    Set<Additive> additives,
    Set<Allergen> allergens,
    Set<Tag> tags,
    BigDecimal studentPrice,
    BigDecimal staffPrice,
    BigDecimal guestPrice,
    LocalDate date) {

  /**
   * A builder for a {@link Dish}.
   *
   * <p>Note that you have to fill at least the values {@link #type}, {@link #name}, {@link
   * #studentPrice}, {@link #staffPrice}, {@link #guestPrice}, and {@link #date}.
   */
  public static class Builder {

    private DishType type;
    private String name;
    private Set<Additive> additives = new LinkedHashSet<>();
    private Set<Allergen> allergens = new LinkedHashSet<>();
    private Set<Tag> tags = new LinkedHashSet<>();
    private BigDecimal studentPrice;
    private BigDecimal staffPrice;
    private BigDecimal guestPrice;
    private LocalDate date;

    public Builder setType(DishType type) {
      this.type = Preconditions.checkNotNull(type);
      return this;
    }

    public Builder setName(String name) {
      this.name = Preconditions.checkNotNull(name);
      return this;
    }

    public Builder setAdditives(Collection<Additive> additives) {
      this.additives = new LinkedHashSet<>(additives);
      return this;
    }

    public Builder setAllergens(Collection<Allergen> allergens) {
      this.allergens = new LinkedHashSet<>(allergens);
      return this;
    }

    public Builder setTags(Collection<Tag> tags) {
      this.tags = new LinkedHashSet<>(tags);
      return this;
    }

    public Builder setStudentPrice(BigDecimal studentPrice) {
      Preconditions.checkState(studentPrice.signum() > 0);
      this.studentPrice = studentPrice;
      return this;
    }

    public Builder setStaffPrice(BigDecimal staffPrice) {
      Preconditions.checkState(staffPrice.signum() > 0);
      this.staffPrice = staffPrice;
      return this;
    }

    public Builder setGuestPrice(BigDecimal guestPrice) {
      Preconditions.checkState(guestPrice.signum() > 0);
      this.guestPrice = guestPrice;
      return this;
    }

    public Builder setDate(LocalDate date) {
      this.date = Preconditions.checkNotNull(date);
      return this;
    }

    public Dish build() {
      Preconditions.checkNotNull(type);
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(studentPrice);
      Preconditions.checkNotNull(staffPrice);
      Preconditions.checkNotNull(guestPrice);
      Preconditions.checkNotNull(date);
      return new Dish(
          type, name, additives, allergens, tags, studentPrice, staffPrice, guestPrice, date);
    }
  }
}
