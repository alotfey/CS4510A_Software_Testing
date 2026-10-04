package edu.baker;

import edu.baker.UnitConversion.DistanceUnit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import edu.baker.UnitConversion.TimeUnit;
import edu.baker.UnitConversion.WeightUnit;
import edu.baker.UnitConversion.VolumeUnit;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for UnitConversion, written one group of units at a time using
 * Test-Driven Development (red, green, refactor).
 */
class UnitConversionTest {

    /**
     * Allowed error, as a fraction of the expected value (one part in a billion).
     * Doubles cannot store most decimals exactly, so results are compared
     * with a small tolerance instead of exact equality.
     */
    private static final double RELATIVE_TOLERANCE = 1e-9;

    /**
     * Supplies distance test cases: value, from unit, to unit, expected result.
     * The first rows cover the metric units: a same-unit conversion (m to m),
     * conversions up and down the metric scale, the largest metric jump
     * (km to mm) in both directions, and 0, which must stay 0 in any unit.
     * The last rows cover the US units: the textbook relationships between
     * them (12 inches in a foot, 3 feet in a yard, 5280 feet and 1760 yards
     * in a mile), plus conversions that cross between US and metric units
     * in both directions.
     *
     * @return the list of argument rows for convertDistanceTest
     */
    static List<Arguments> distanceConversions() {
        return Arrays.asList(
                Arguments.of(1.0, DistanceUnit.m, DistanceUnit.m, 1.0),
                Arguments.of(1.0, DistanceUnit.km, DistanceUnit.m, 1000.0),
                Arguments.of(1.0, DistanceUnit.m, DistanceUnit.cm, 100.0),
                Arguments.of(1.0, DistanceUnit.cm, DistanceUnit.mm, 10.0),
                Arguments.of(2500.0, DistanceUnit.mm, DistanceUnit.m, 2.5),
                Arguments.of(1.0, DistanceUnit.km, DistanceUnit.mm, 1000000.0),
                Arguments.of(5.0, DistanceUnit.mm, DistanceUnit.km, 0.000005),
                Arguments.of(0.0, DistanceUnit.m, DistanceUnit.km, 0.0),
                Arguments.of(1.0, DistanceUnit.foot, DistanceUnit.inch, 12.0),
                Arguments.of(1.0, DistanceUnit.yard, DistanceUnit.foot, 3.0),
                Arguments.of(1.0, DistanceUnit.mile, DistanceUnit.foot, 5280.0),
                Arguments.of(1.0, DistanceUnit.mile, DistanceUnit.yard, 1760.0),
                Arguments.of(1.0, DistanceUnit.inch, DistanceUnit.cm, 2.54),
                Arguments.of(3.0, DistanceUnit.foot, DistanceUnit.m, 0.9144),
                Arguments.of(1.0, DistanceUnit.mile, DistanceUnit.km, 1.609344),
                Arguments.of(100.0, DistanceUnit.m, DistanceUnit.yard, 109.36132983377078)
        );
    }

    /**
     * Checks requirement 5: convertDistance() converts a value from one
     * DistanceUnit to another. Each row comes from distanceConversions().
     *
     * @param value    the distance to convert
     * @param from     the unit the value is in
     * @param to       the unit to convert into
     * @param expected the correct converted distance
     */
    @ParameterizedTest
    @MethodSource("distanceConversions")
    void convertDistanceTest(double value, DistanceUnit from, DistanceUnit to, double expected) {
        assertEquals(expected, UnitConversion.convertDistance(value, from, to),
                Math.abs(expected) * RELATIVE_TOLERANCE);
    }
    /**
     * Supplies time test cases: value, from unit, to unit, expected result.
     * The first rows cover the units up to one second: a same-unit conversion
     * (s to s), each step of 1000 up the scale (ns to µs to ms to s), the
     * largest jump (s to ns) in both directions, and 0, which must stay 0 in
     * any unit. The last rows cover the longer units: each everyday step
     * (60 seconds, 60 minutes, 24 hours, 365 days), multi-step conversions to
     * seconds, a fractional result (90 min = 1.5 hours), and the widest jump
     * in the enum (year to ns) to check very large results.
     *
     * @return the list of argument rows for convertTimeTest
     */
    static List<Arguments> timeConversions() {
        return Arrays.asList(
                Arguments.of(1.0, TimeUnit.s, TimeUnit.s, 1.0),
                Arguments.of(1.0, TimeUnit.s, TimeUnit.ms, 1000.0),
                Arguments.of(1.0, TimeUnit.ms, TimeUnit.µs, 1000.0),
                Arguments.of(1.0, TimeUnit.µs, TimeUnit.ns, 1000.0),
                Arguments.of(1.0, TimeUnit.s, TimeUnit.ns, 1000000000.0),
                Arguments.of(2500000.0, TimeUnit.ns, TimeUnit.ms, 2.5),
                Arguments.of(5.0, TimeUnit.µs, TimeUnit.s, 0.000005),
                Arguments.of(0.0, TimeUnit.s, TimeUnit.ms, 0.0),
                Arguments.of(1.0, TimeUnit.min, TimeUnit.s, 60.0),
                Arguments.of(1.0, TimeUnit.hour, TimeUnit.min, 60.0),
                Arguments.of(1.0, TimeUnit.day, TimeUnit.hour, 24.0),
                Arguments.of(1.0, TimeUnit.year, TimeUnit.day, 365.0),
                Arguments.of(1.0, TimeUnit.hour, TimeUnit.s, 3600.0),
                Arguments.of(1.0, TimeUnit.day, TimeUnit.s, 86400.0),
                Arguments.of(90.0, TimeUnit.min, TimeUnit.hour, 1.5),
                Arguments.of(7200000.0, TimeUnit.ms, TimeUnit.hour, 2.0),
                Arguments.of(1.0, TimeUnit.year, TimeUnit.ns, 31536000000000000.0)
        );
    }

    /**
     * Checks requirement 6: convertTime() converts a value from one
     * TimeUnit to another. Each row comes from timeConversions().
     *
     * @param value    the time to convert
     * @param from     the unit the value is in
     * @param to       the unit to convert into
     * @param expected the correct converted time
     */
    @ParameterizedTest
    @MethodSource("timeConversions")
    void convertTimeTest(double value, TimeUnit from, TimeUnit to, double expected) {
        assertEquals(expected, UnitConversion.convertTime(value, from, to),
                Math.abs(expected) * RELATIVE_TOLERANCE);
    }

    /**
     * Supplies weight test cases: value, from unit, to unit, expected result.
     * The first rows cover the metric units: a same-unit conversion (g to g),
     * each step of 1000 up the scale (g to kg to metric_ton), the largest jump
     * (metric_ton to g) in both directions, and 0, which must stay 0 in any
     * unit. The last rows cover the US units: the everyday relationships
     * (16 ounces in a pound, 2000 pounds in a ton), the exact metric values
     * of each US unit, and conversions from metric back to US, including
     * metric_ton to ton, two units that are easy to mix up.
     *
     * @return the list of argument rows for convertWeightTest
     */
    static List<Arguments> weightConversions() {
        return Arrays.asList(
                Arguments.of(1.0, WeightUnit.g, WeightUnit.g, 1.0),
                Arguments.of(1.0, WeightUnit.kg, WeightUnit.g, 1000.0),
                Arguments.of(1.0, WeightUnit.metric_ton, WeightUnit.kg, 1000.0),
                Arguments.of(1.0, WeightUnit.metric_ton, WeightUnit.g, 1000000.0),
                Arguments.of(2500.0, WeightUnit.g, WeightUnit.kg, 2.5),
                Arguments.of(5.0, WeightUnit.g, WeightUnit.metric_ton, 0.000005),
                Arguments.of(0.0, WeightUnit.kg, WeightUnit.g, 0.0),
                Arguments.of(1.0, WeightUnit.pound, WeightUnit.ounce, 16.0),
                Arguments.of(1.0, WeightUnit.ton, WeightUnit.pound, 2000.0),
                Arguments.of(1.0, WeightUnit.pound, WeightUnit.g, 453.59237),
                Arguments.of(1.0, WeightUnit.ounce, WeightUnit.g, 28.349523125),
                Arguments.of(1.0, WeightUnit.ton, WeightUnit.kg, 907.18474),
                Arguments.of(1.0, WeightUnit.kg, WeightUnit.pound, 2.2046226218487757),
                Arguments.of(1.0, WeightUnit.metric_ton, WeightUnit.ton, 1.1023113109243878)
        );
    }

    /**
     * Checks requirement 7: convertWeight() converts a value from one
     * WeightUnit to another. Each row comes from weightConversions().
     *
     * @param value    the weight to convert
     * @param from     the unit the value is in
     * @param to       the unit to convert into
     * @param expected the correct converted weight
     */
    @ParameterizedTest
    @MethodSource("weightConversions")
    void convertWeightTest(double value, WeightUnit from, WeightUnit to, double expected) {
        assertEquals(expected, UnitConversion.convertWeight(value, from, to),
                Math.abs(expected) * RELATIVE_TOLERANCE);
    }

    /**
     * Supplies volume test cases: value, from unit, to unit, expected result.
     * The first rows cover the metric units: a same-unit conversion (ml to ml),
     * cc to ml (two names for the same amount), liters to ml and to cc, a
     * fractional result (2500 ml = 2.5 liters), and 0, which must stay 0 in
     * any unit. The last rows cover the US units: the everyday relationships
     * (4 quarts in a gallon, 32 fluid ounces in a quart, 128 in a gallon),
     * the exact metric value of each US unit, and conversions from liters
     * back to US units.
     *
     * @return the list of argument rows for convertVolumeTest
     */
    static List<Arguments> volumeConversions() {
        return Arrays.asList(
                Arguments.of(1.0, VolumeUnit.ml, VolumeUnit.ml, 1.0),
                Arguments.of(1.0, VolumeUnit.cc, VolumeUnit.ml, 1.0),
                Arguments.of(1.0, VolumeUnit.liters, VolumeUnit.ml, 1000.0),
                Arguments.of(1.0, VolumeUnit.liters, VolumeUnit.cc, 1000.0),
                Arguments.of(2500.0, VolumeUnit.ml, VolumeUnit.liters, 2.5),
                Arguments.of(0.0, VolumeUnit.liters, VolumeUnit.ml, 0.0),
                Arguments.of(1.0, VolumeUnit.gallon, VolumeUnit.qt, 4.0),
                Arguments.of(1.0, VolumeUnit.qt, VolumeUnit.fluid_oz, 32.0),
                Arguments.of(1.0, VolumeUnit.gallon, VolumeUnit.fluid_oz, 128.0),
                Arguments.of(1.0, VolumeUnit.gallon, VolumeUnit.liters, 3.785411784),
                Arguments.of(1.0, VolumeUnit.qt, VolumeUnit.ml, 946.352946),
                Arguments.of(1.0, VolumeUnit.fluid_oz, VolumeUnit.ml, 29.5735295625),
                Arguments.of(1.0, VolumeUnit.liters, VolumeUnit.gallon, 0.2641720523581484),
                Arguments.of(1.0, VolumeUnit.liters, VolumeUnit.fluid_oz, 33.814022701842994)
        );
    }

    /**
     * Checks step 12 (the instructor's added requirement): convertVolume()
     * converts a value from one VolumeUnit to another. Each row comes from
     * volumeConversions().
     *
     * @param value    the volume to convert
     * @param from     the unit the value is in
     * @param to       the unit to convert into
     * @param expected the correct converted volume
     */
    @ParameterizedTest
    @MethodSource("volumeConversions")
    void convertVolumeTest(double value, VolumeUnit from, VolumeUnit to, double expected) {
        assertEquals(expected, UnitConversion.convertVolume(value, from, to),
                Math.abs(expected) * RELATIVE_TOLERANCE);
    }
}