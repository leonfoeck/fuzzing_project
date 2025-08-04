package de.uni_passau.fim.se2.st.fuzzing.fuzztarget;

public class Example {

  public int getAscii(char c) {
    return c;
  }

  public static String evaluateGrade(char grade) {

    return switch (grade) {
      case 'A', 'a' -> "Excellent";
      case 'B', 'b' -> "Good";
      case 'C', 'c' -> "Average";
      case 'D', 'd' -> "Below Average";
      case 'F', 'f' -> "Fail";
      default -> "Invalid Grade";
    };
  }

  public int gcd(int x, int y) {
    int tmp = 0;
    while (y != 0) {
      tmp = x % y;
      x = y;
      y = tmp;
    }

    return x;
  }

  public byte gcd(byte x, byte y) {
    byte tmp = 0;
    while (y != 0) {
      tmp = (byte) (x % y);
      x = y;
      y = tmp;
    }

    return x;
  }

  public short gcd(short x, short y) {
    short tmp = 0;
    while (y != 0) {
      tmp = (short) (x % y);
      x = y;
      y = tmp;
    }

    return x;
  }

  public static double sin(double x) {
    double term2 = (-1.0 / 6.0) * Math.pow(x, 3);
    double term3 = (1.0 / 120.0) * Math.pow(x, 5);

    return x + term2 + term3;
  }

  public static float cos(float x) {
    // Using the first three terms of the Taylor series expansion for cos(x)
    float term1 = 1.0f;
    float term2 = (float) ((-1.0 / 2.0) * Math.pow(x, 2));
    float term3 = (float) ((1.0 / 24.0) * Math.pow(x, 4));

    return term1 + term2 + term3;
  }

  public static int evaluateConditions(boolean condition1, boolean condition2, boolean condition3) {
    if (condition1 && condition2 && condition3) {
      return 0;
    }

    if ((condition1 && condition2) || (condition1 && condition3) || (condition2 && condition3)) {
      return 1;
    }

    if (condition1 || condition2 || condition3) {
      return 2;
    }

    return 3;
  }

  private boolean secretMethod() {
    return true;
  }
}
