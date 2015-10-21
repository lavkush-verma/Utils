public class StringUtils {

    private static final String NULL_STRING = "null";

    /**
     * Checks if is not empty.
     *
     * @param value the value
     * @return true, if is not empty
     */
    public static boolean isNotEmpty(String value) {
        return !TextUtils.isEmpty(value) && !NULL_STRING.equalsIgnoreCase(value);
    }

    /**
     * Checks if is empty.
     *
     * @param value the value
     * @return true, if is empty
     */
    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value) || NULL_STRING.equalsIgnoreCase(value);
    }

}
