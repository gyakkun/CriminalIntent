package database.HeroDbSchema;

public class HeroDbSchema {

    public static final class HeroTable {
        public static final String NAME = "heros";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }


}
