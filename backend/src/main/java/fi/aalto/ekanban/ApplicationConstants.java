package fi.aalto.ekanban;

public class ApplicationConstants {

    public static final String GAME_PATH = "games";
    public static final String PLAY_TURN_PATH = "/{gameId}";
    public static final String APPLICATION_JSON_TYPE = "application/json";

    public static final String ANALYSIS_PHASE_ID = "ANALYSIS";
    public static final String DEVELOPMENT_PHASE_ID = "DEVELOPMENT";
    public static final String TEST_PHASE_ID = "TEST";
    public static final String DEPLOYED_PHASE_ID = "DEPLOYED";

    public static final String IN_PROGRESS_COLUMN = "In Progress";
    public static final String WAITING_FOR_NEXT_TEMPLATE = "Waiting for ";

    public static final Integer RESOURCE_DICE_MIN = 3;
    public static final Integer RESOURCE_DICE_MAX = 6;

    public static final Integer DAY_THRESHOLD_TO_RETURN_DICE_CAST_ACTIONS = 2;

}
