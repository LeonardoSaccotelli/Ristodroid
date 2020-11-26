package persistence;

/**
 * Definizione dello schema del DB
 */
public class RistodroidDBSchema {
    public static final class AllergenicTable {
        public static final String NAME = "allergenic";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
        }
    }

    public static final class AllergenicDishTable {
        public static final String NAME = "allergenicdish";

        public static final class Cols {
            public static final String DISH = "dish";
            public static final String ALLERGENIC = "allergenic";
        }
    }

    public static final class AvailabilityTable {
        public static final String NAME = "availability";

        public static final class Cols {
            public static final String MENU = "menu";
            public static final String DISH = "dish";
            public static final String STARTDATE = "startdate";
            public static final String ENDDATE = "enddate";
        }
    }

    public static final class CategoryTable {
        public static final String NAME = "category";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String PHOTO = "photo";
        }
    }

    public static final class CategoryVariationTable {
        public static final String NAME = "categoryvariation";

        public static final class Cols {
            public static final String VARIATION = "variation";
            public static final String CATEGORY = "category";
        }
    }

    public static final class DishTable {
        public static final String NAME = "dish";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String DESCRIPTION = "description";
            public static final String PRICE = "price";
            public static final String CATEGORY = "category";
            public static final String PHOTO = "photo";
        }
    }

    public static final class IngredientTable {
        public static final String NAME = "ingredient";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
        }
    }

    public static final class IngredientDishTable {
        public static final String NAME = "ingredientdish";

        public static final class Cols {
            public static final String DISH = "dish";
            public static final String INGREDIENT = "ingredient";
        }
    }

    public static final class MenuTable {
        public static final String NAME = "menu";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
        }
    }

    public static final class VariationTable {
        public static final String NAME = "variation";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String PRICE = "price";
        }
    }

    public static final class JsonOrderTable {
        public static final String NAME = "jsonorder";

        public  static final class Cols {
            public static final String ID = "id";
            public static final String JSON = "json";
        }
    }

    public static final class ReviewTable {
        public static final String NAME = "review";

        public static final class Cols {
            public static final String ID = "id";
            public static final String SCORE = "score";
            public static final String TEXT = "text_review";
            public static final String DATE = "reviewDate";
            public static final String DISH ="dish";
        }
    }


}
