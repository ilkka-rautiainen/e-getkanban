var backendHost = process.env.NODE_ENV === "development" ? "http://localhost:8080/" : "http://e-kanban.aalto.fi:8080/";

const constants = {
  GAME_DIFFICULTY_NORMAL: 'NORMAL',
  GAME_DIFFICULTY_MEDIUM: 'MEDIUM',
  GAME_DIFFICULTY_ADVANCED: 'ADVANCED',
  GAMES_PATH: "games/",
  BACKEND_HOST: backendHost,
  GAME_ENDED_MESSAGE: "Game Has Ended!",
  START_OVER_MESSAGE: "Start Over",
  ANALYSIS: "ANALYSIS",
  TEST: "TEST",
  DEPLOYED: "DEPLOYED",
  ENTERED_BOARD: "ENTERED_BOARD",
  ENTERED_BOARD_TITLE: "Entered Board",
  GAME_ERROR: "Game Error, Start Over!"
};

export default constants;
