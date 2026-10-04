package edu.baker;

/**
 * Converts measurements between units of distance, time, weight and volume.
 * Built one requirement at a time using Test-Driven Development.
 */
public class UnitConversion {

    /**
     * Units of distance that convertDistance() understands.
     */
    public enum DistanceUnit { mm, cm, m, km, inch, foot, yard, mile }

    /**
     * Units of time that convertTime() understands.
     */
    public enum TimeUnit { ns, µs, ms, s, min, hour, day, year }

    /**
     * Units of weight that convertWeight() understands.
     */
    public enum WeightUnit { g, kg, metric_ton, ounce, pound, ton }

    /**
     * Units of volume that convertVolume() understands. Added for step 12
     * from the instructor's forum post. "fluid oz" is written fluid_oz
     * because Java names cannot contain spaces.
     */
    public enum VolumeUnit { cc, ml, liters, fluid_oz, qt, gallon }

    /**
     * Converts a distance from one unit to another. The value is first turned
     * into meters using the "from" unit, then turned from meters into the
     * "to" unit.
     *
     * @param value the distance to convert
     * @param from  the unit the value is in
     * @param to    the unit to convert into
     * @return the converted distance
     */
    public static double convertDistance(double value, DistanceUnit from, DistanceUnit to) {
        return value * metersPer(from) / metersPer(to);
    }

    /**
     * Returns how many meters are in one of the given distance unit.
     * The US values are the exact definitions: 1 inch = 0.0254 m,
     * 1 foot = 0.3048 m, 1 yard = 0.9144 m and 1 mile = 1609.344 m.
     *
     * @param unit the distance unit
     * @return the number of meters in one of that unit
     * @throws IllegalArgumentException if the unit has no conversion factor
     */
    private static double metersPer(DistanceUnit unit) {
        switch (unit) {
            case mm:
                return 0.001;
            case cm:
                return 0.01;
            case m:
                return 1.0;
            case km:
                return 1000.0;
            case inch:
                return 0.0254;
            case foot:
                return 0.3048;
            case yard:
                return 0.9144;
            case mile:
                return 1609.344;
            default:
                throw new IllegalArgumentException("Unknown distance unit: " + unit);
        }
    }

    /**
     * Converts a length of time from one unit to another. The value is first
     * turned into seconds using the "from" unit, then turned from seconds
     * into the "to" unit.
     *
     * @param value the time to convert
     * @param from  the unit the value is in
     * @param to    the unit to convert into
     * @return the converted time
     */
    public static double convertTime(double value, TimeUnit from, TimeUnit to) {
        return value * secondsPer(from) / secondsPer(to);
    }

    /**
     * Returns how many seconds are in one of the given time unit.
     * A year is taken as 365 days.
     *
     * @param unit the time unit
     * @return the number of seconds in one of that unit
     * @throws IllegalArgumentException if the unit has no conversion factor
     */
    private static double secondsPer(TimeUnit unit) {
        switch (unit) {
            case ns:
                return 0.000000001;
            case µs:
                return 0.000001;
            case ms:
                return 0.001;
            case s:
                return 1.0;
            case min:
                return 60.0;
            case hour:
                return 60.0 * 60.0;
            case day:
                return 24.0 * 60.0 * 60.0;
            case year:
                return 365.0 * 24.0 * 60.0 * 60.0;
            default:
                throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
    }

    /**
     * Converts a weight from one unit to another. The value is first turned
     * into grams using the "from" unit, then turned from grams into the
     * "to" unit.
     *
     * @param value the weight to convert
     * @param from  the unit the value is in
     * @param to    the unit to convert into
     * @return the converted weight
     */
    public static double convertWeight(double value, WeightUnit from, WeightUnit to) {
        return value * gramsPer(from) / gramsPer(to);
    }

    /**
     * Returns how many grams are in one of the given weight unit.
     * A pound is exactly 453.59237 g, an ounce is 1/16 of a pound, and a
     * ton is the US short ton of 2000 pounds.
     *
     * @param unit the weight unit
     * @return the number of grams in one of that unit
     * @throws IllegalArgumentException if the unit has no conversion factor
     */
    private static double gramsPer(WeightUnit unit) {
        switch (unit) {
            case g:
                return 1.0;
            case kg:
                return 1000.0;
            case metric_ton:
                return 1000000.0;
            case ounce:
                return 453.59237 / 16.0;
            case pound:
                return 453.59237;
            case ton:
                return 453.59237 * 2000.0;
            default:
                throw new IllegalArgumentException("Unknown weight unit: " + unit);
        }
    }
    /**
     * Converts a volume from one unit to another. The value is first turned
     * into milliliters using the "from" unit, then turned from milliliters
     * into the "to" unit.
     *
     * @param value the volume to convert
     * @param from  the unit the value is in
     * @param to    the unit to convert into
     * @return the converted volume
     */
    public static double convertVolume(double value, VolumeUnit from, VolumeUnit to) {
        return value * millilitersPer(from) / millilitersPer(to);
    }

    /**
     * Returns how many milliliters are in one of the given volume unit.
     * A cc and a ml are the same amount. The US gallon is exactly
     * 3785.411784 ml (231 cubic inches), a quart is 1/4 of a gallon, and a
     * fluid ounce is 1/128 of a gallon.
     *
     * @param unit the volume unit
     * @return the number of milliliters in one of that unit
     * @throws IllegalArgumentException if the unit has no conversion factor
     */
    private static double millilitersPer(VolumeUnit unit) {
        switch (unit) {
            case cc:
                return 1.0;
            case ml:
                return 1.0;
            case liters:
                return 1000.0;
            case fluid_oz:
                return 3785.411784 / 128.0;
            case qt:
                return 3785.411784 / 4.0;
            case gallon:
                return 3785.411784;
            default:
                throw new IllegalArgumentException("Unknown volume unit: " + unit);
        }
    }
}