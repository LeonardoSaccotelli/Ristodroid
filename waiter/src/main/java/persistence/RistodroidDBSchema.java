package persistence;

/**
 * Definizione dello schema del DB
 */
public class RistodroidDBSchema {

    public static final class SeatTable {
        public static final String NAME = "seat";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String PRICE = "price";
        }
    }

    public static final class TableTable {
        public static final String NAME = "tables";

        public static final class Cols {
            public static final String ID = "id";
        }
    }
}
