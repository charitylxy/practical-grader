package com.example.prac_grader.sql_database;

public class DBSchema {
    public static class AccountTable{
        public static final String NAME = "account";
        public static class Cols {
            public static final String USERNAME = "account_username";
            public static final String PIN = "account_pin";
            public static final String NAME = "account_name";
            public static final String EMAIL = "account_email";
            public static final String COUNTRY = "account_country";
            public static final String TYPE = "account_type";
        }
    }

    public static class StudentTable{
        public static final String NAME = "student";
        public static class Cols {
            public static final String USERNAME = "student_username";
            public static final String INSTRUCTOR = "student_instructor";

        }
    }

    public static class PracticalTable{
        public static final String NAME = "practical";
        public static class Cols {
            public static final String ID = "practical_id";
            public static final String TITLE = "practical_title";
            public static final String DESC = "practical_desc";
            public static final String MAX_MARKS = "practical_max_marks";
        }
    }

    public static class ResultTable{
        public static final String NAME = "result";
        public static class Cols {
            public static final String ID = "result_id";
            public static final String PRAC_ID = "result_prac_id";
            public static final String STUDENT = "result_student";
            public static final String MARKS = "result_marks";
        }
    }
}
