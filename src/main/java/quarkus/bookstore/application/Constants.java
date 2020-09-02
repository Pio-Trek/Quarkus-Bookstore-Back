package quarkus.bookstore.application;

public class Constants {

    private Constants() {
        throw new AssertionError();
    }

    // Logs
    public static final String LOG_FORMAT_API_RESULT = ("API_RESULT - class=[%s], method=[%s], [%s]=[%s]");
    public static final String LOG_FORMAT_API = ("API_CALL - class=[%s], method=[%s], path=[%s]");
    public static final String LOG_FORMAT_API_PARAM = ("API_CALL - class=[%s], method=[%s], path=[%s], param=[%s]");
    public static final String LOG_FORMAT_ERROR = ("class=[%s], method=[%s], path=[%s], code=[%s], error=[%s]");

    // Repository
    public static final String DEFAULT_SORT_COLUMN = "title";

}
