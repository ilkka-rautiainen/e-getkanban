var backendHost = process.env.NODE_ENV === "development" ? "http://localhost:8080/" : "http://localhost:8080/";

const constants = {
  GAME_DIFFICULTY_NORMAL: 'NORMAL',
  GAMES_PATH: "games/",
  BACKEND_HOST: backendHost
};

export default constants;
