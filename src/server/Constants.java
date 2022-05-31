package server;

public class Constants {
    public static final String FIRST_LINE = "id, util.person name, coordinates x, coordinates y, creationDate, util.person height, util.person weight, util.person passportID, util.person color,location x, location y, location z, location name";
    public static final String HELP_INFO ="help: returns info about available util.commands \n";
    public static final String INFO_INFO ="info: returns info about server.list \n";
    public static final String SHOW_INFO ="show: returns every util.person to String from server.list \n";
    public static final String ADD_INFO ="add: adds util.person to server.list(need to create) \n";
    public static final String UPDATE_INFO ="update: you create a new element and if server.list has util.person with given id, it replaces it \n";
    public static final String REMOVE_BY_ID_INFO ="remove_by_id: removes elements with this id \n";
    public static final String CLEAR_INFO ="clear: server.list becomes empty \n";
    public static final String EXECUTE_SCRIPT_INFO ="execute_script: executes script from file \n";
    public static final String EXIT_INFO ="exit: finishes program \n";
    public static final String ADD_IF_MAX_INFO ="add_if_max: you create util.person and if it's id the biggest, it's added to server.list \n";
    public static final String ADD_IF_MIN_INFO ="add_if_min: you create util.person and if it's id the smallest, it's added to server.list \n";
    public static final String REMOVE_LOWER_INFO ="remove_lower: you create util.person and all elements with id lower than new one deleted \n";
    public static final String REMOVE_ALL_BY_WEIGHT_INFO ="remove_all_by_lower: remove all elements with this weight \n";
    public static final String COUNT_GREATER_THAN_HEIGHT_INFO ="count_greater_than_height: count elements with greater amount of height \n";
    public static final String FILTER_STARTS_WITH_PASSPORT_ID_INFO ="filter_starts_with_passport_i_d: return elements if their passport id contains given string \n";
    public final static int size = 10240;


}
