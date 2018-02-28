// API Paths
var api = {};
api.query1 = "/api/v1/demo.php";
api.query2 = "/api/v1/queries.php";
api.query3 = "/api/v1/columns.php";
api.query4 = "services/verticals.php";
api.query5 = "http://localhost:8174/progsets/rest/psql/exe?return.as=verticals";
api.visualquery= "http://localhost:8174/progsets/rest/psql/exe";
api.base = "http://localhost:8174/progsets/rest/psql/entity/";
api.auth = "Basic e3ea861571c789c088722da097a50d14414a3c8ccc34f842:93d2cbd59c74a848e158ab7b2d10ab2eb76e084d7ccabd09";

function millisToMinutesAndSeconds(millis) {
    var minutes = Math.floor(millis / 60000);
    var seconds = ((millis % 60000) / 1000).toFixed(0);
    return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
}

// Global params
var global = {};
global.timeout = 1000*60*3;
global.timeoutinmutes = millisToMinutesAndSeconds(global.timeout);
