/**
 * Class to handle return form tryReadInt function
 * @param success If no errors occur
 * @param value Value from console
 */
public record SafeInt(boolean success, int value){}
