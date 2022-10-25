package com.example.m_hikeapp.utils;

public class Constants {
    public static boolean IS_FORM_VALID = false;
    public static String EDIT_DETAILS_KEY;
    public static String HIKE_DETAILS = "Hike Details";
    public static final String DATABASE_NAME = "HikesRecords.db";
    public static final int DATABASE_VERSION = 1;
    public static final String FIRST_TABLE_NAME = "hike_details";
    public static final String SECOND_TABLE_NAME = "hike_observation";

    public static final String FIRST_TABLE_COLUM_1 = "id";
    public static final String FIRST_TABLE_COLUM_2 = "hike_name";
    public static final String FIRST_TABLE_COLUM_3 = "hike_location";
    public static final String FIRST_TABLE_COLUM_4 = "hike_date";
    public static final String FIRST_TABLE_COLUM_5 = "hike_distance";
    public static final String FIRST_TABLE_COLUM_6 = "hike_purpose";
    public static final String FIRST_TABLE_COLUM_7 = "hike_description";
    public static final String FIRST_TABLE_COLUM_8 = "hike_number_of_persons";
    public static final String FIRST_TABLE_COLUM_9 = "hike_parking_available";
    public static final String FIRST_TABLE_COLUM_10 = "hike_camping";
    public static final String FIRST_TABLE_COLUM_11 = "hike_thumbnail";

    public static final String SECOND_TABLE_COLUM_1 = "id";
    public static final String SECOND_TABLE_COLUM_2 = "hike_observation";
    public static final String SECOND_TABLE_COLUM_3 = "hike_time";
    public static final String SECOND_TABLE_COLUM_4 = "hike_additional_comment";
    public static final String SECOND_TABLE_COLUM_5 = "hike_observation_image";

    //   Sample hike thumbnails
    public static final String HIKE_THUMBNAIL1 = "https://images.app.goo.gl/t8qb5RiYePqKoH6a9";
    public static final String HIKE_THUMBNAIL2 = "https://images.app.goo.gl/m2vbxeJd7AkzndBJ7";
    public static final String HIKE_THUMBNAIL3 = "https://images.app.goo.gl/2jEYXuWtp4Dpx8Ht9";
    public static final String HIKE_THUMBNAIL4 = "https://images.app.goo.gl/g25nVqUZmi1nKWXv7";
    public static final String HIKE_THUMBNAIL5 = "https://images.app.goo.gl/s1e4R86UF1hqtpm17";

    //    Database queries
    public static final String GET_ALL_DATA_FROM_FIRST_TABLE = "SELECT * FROM ";
}
