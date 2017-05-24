package com.mobile.esprit.sensor.Utils;

/**
 * Created by Souhaib on 04/02/2017.
 */

public class URL {

    public static String DEFAULT_RECIPE = "https://firebasestorage.googleapis.com/v0/b/brackets-157800.appspot.com/o/default_recipe.png?alt=media&token=d931ce53-520e-4ad8-906b-d8648f23c731";

    public static final String IP_ADDRESS = "51.15.135.234:8090";

    public static String ADD_USER = "";
    public static String REGISTER_USER = "";
    public static String SIGNIN_USER = "";
    public static String REGISTER_DEVICE_TOKEN = "";
    public static String MY_PREFS_NAME = "SENSORS";
    public static String GET_RECIPE_BY_ID = "";
    public static String GET_ALL_RECIPES = "";
    public static String GET_COMMENT_BY_RECIPE = "";
    public static String DELETE_COMMENT = "";
    public static String ADD_COMMENT = "";
    public static String ADD_RATING = "";
    public static String GET_USER_RATING_BY_RECIPE = "";
    public static String GET_RECIPE_RATING = "";
    public static String LOAD_ALL_BASES = "";
    public static String FIND_ENABLED_USER_AROMA = "";
    public static String FIND_DISABLED_USER_AROME = "";
    public static String FIND_AROMA_BY_USER = "";
    public static String ADD_USER_AROMA = "";
    public static String ADD_USER_RECIPE_FAVORIS = "";
    public static String DELETE_USER_RECIPE_FAVORIS = "";
    public static String GET_USER_RECIPE_FAVORIS = "";
    public static String GET_USER_DEVICE_CONFIG = "";
    public static String LOAD_DEVICE_CONFIG = "";
    public static String FIND_RECIPE_BY_AROME = "";
    public static String ADD_USER_RECIPE = "";
    public static String FIND_RECIPE_BY_USER = "";
    public static String FIND_RECIPE_BY_TAG = "";
    public static String FIND_DEVICE_CONFIG_RECIPE_BY_DEVICE_CONFIG = "";

    //Gadour URLs
    public static String LOAD_All_AROMAS = "";
    public static String LOAD_All_MANUFACTURERS = "";
    public static String LOAD_ALL_AROMAS_BY_MANUFACTURER = "";
    public static String LOAD_ALL_CATEGORIES = "";
    public static String LOAD_ALL_AROMAS_BY_CATEGORY = "";
    public static String AROMA_IMAGE = "";
    public static String FIND_RECIPE_FAVORIS = "";
    public static String FIND_USER_BY_ID = "";
    public static String UPDATE_DEVICE_CONF_IS_DEFAULT = "";
    public static String LOAD_DEVICE_CONFIG_HISTORY = "";
    public static String UPDATE_DEVICE_CONF_HISTORY_IS_DEFAULT = "";
    public static String ADD_DEVICE_CONF_RECIPE = "";
    public static String SET_USER_DEVICE = "";


    public static void toTheServer(String server) {

        ADD_USER = server + "/user";
        REGISTER_USER = server + "/user/register";
        SIGNIN_USER = server + "/user/signin";
        REGISTER_DEVICE_TOKEN = server + "/device";

    }

    public static void toTheLocalServer() {
        ADD_USER = "http://" + IP_ADDRESS + "/user";
        REGISTER_USER = "http://" + IP_ADDRESS + "/user/register";
        SIGNIN_USER = "http://" + IP_ADDRESS + "/user/signin";
        REGISTER_DEVICE_TOKEN = "http://" + IP_ADDRESS + "/device";
        LOAD_All_AROMAS = "http://" + IP_ADDRESS + "/arome";
        LOAD_All_MANUFACTURERS = "http://" + IP_ADDRESS + "/manufacture";
        LOAD_ALL_AROMAS_BY_MANUFACTURER = "http://" + IP_ADDRESS + "/arome/manufacturer/";
        LOAD_ALL_CATEGORIES = "http://" + IP_ADDRESS + "/category";
        LOAD_ALL_AROMAS_BY_CATEGORY = "http://" + IP_ADDRESS + "/arome/category/";
        GET_RECIPE_BY_ID = "http://" + IP_ADDRESS + "/recipe/";
        GET_ALL_RECIPES = "http://" + IP_ADDRESS + "/recipe";
        GET_COMMENT_BY_RECIPE = "http://" + IP_ADDRESS + "/comment/";
        DELETE_COMMENT = "http://" + IP_ADDRESS + "/comment/";
        ADD_COMMENT = "http://" + IP_ADDRESS + "/comment/";
        ADD_RATING = "http://" + IP_ADDRESS + "/rating/";
        GET_USER_RATING_BY_RECIPE = "http://" + IP_ADDRESS + "/rating/";
        GET_RECIPE_RATING = "http://" + IP_ADDRESS + "/rating/";
        LOAD_ALL_BASES = "http://" + IP_ADDRESS + "/base";
        FIND_ENABLED_USER_AROMA = "http://" + IP_ADDRESS + "/arome/enabled/";
        FIND_DISABLED_USER_AROME = "http://" + IP_ADDRESS + "/arome/disabled/";
        FIND_AROMA_BY_USER = "http://" + IP_ADDRESS + "/arome/";
        ADD_USER_AROMA = "http://" + IP_ADDRESS + "/arome/";


        ADD_USER_RECIPE_FAVORIS = "http://" + IP_ADDRESS + "/recipe/favoris/";
        DELETE_USER_RECIPE_FAVORIS = "http://" + IP_ADDRESS + "/recipe/favoris/";
        GET_USER_RECIPE_FAVORIS = "http://" + IP_ADDRESS + "/recipe/favoris/";
        FIND_RECIPE_FAVORIS = "http://" + IP_ADDRESS + "/recipe/favoris/find/";
        GET_USER_DEVICE_CONFIG = "http://" + IP_ADDRESS + "/device_config/";
        FIND_USER_BY_ID = "http://" + IP_ADDRESS + "/user/";
        LOAD_DEVICE_CONFIG = "http://" + IP_ADDRESS + "/device_config";
        FIND_RECIPE_BY_AROME = "http://" + IP_ADDRESS + "/recipe/arome/";
        LOAD_DEVICE_CONFIG_HISTORY = "http://" + IP_ADDRESS + "/device_config_history/";
        UPDATE_DEVICE_CONF_IS_DEFAULT = "http://" + IP_ADDRESS + "/device_config/find_default/";
        UPDATE_DEVICE_CONF_HISTORY_IS_DEFAULT = "http://" + IP_ADDRESS + "/device_config_history/find_default/";
        ADD_USER_RECIPE = "http://" + IP_ADDRESS + "/recipe/user/";
        FIND_RECIPE_BY_USER = "http://" + IP_ADDRESS + "/recipe/user/";
        ADD_DEVICE_CONF_RECIPE = "http://" + IP_ADDRESS + "/deviceConfigRecipe/";
        FIND_RECIPE_BY_TAG = "http://" + IP_ADDRESS + "/recipe/tag/";
        FIND_DEVICE_CONFIG_RECIPE_BY_DEVICE_CONFIG = "http://" + IP_ADDRESS + "/deviceConfigRecipe/deviceConfig/";
        SET_USER_DEVICE = "http://" + IP_ADDRESS + "/user/boitier/";

    }

    public static void toTheGadourServer(String server) {

        LOAD_All_AROMAS = server + "Edash/selectAroma.php";
        AROMA_IMAGE = server + "/aroma/";

    }
}
