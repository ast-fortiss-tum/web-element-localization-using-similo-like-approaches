const HEIGHT = 1080;
const WIDTH = 1920;
const MAX_DISTANCE = Math.sqrt(HEIGHT * HEIGHT + WIDTH * WIDTH);

const SMALL_LAMBDA = 0.005;
const MEDIUM_LAMBDA = 0.001;
const LARGE_LAMBDA = 0.01;

function _distance(x1, y1, x2, y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
}

function _distanceDecay(x1, y1, x2, y2, lambda) {
    return Math.exp(-lambda * _distance(x1, y1, x2, y2));
}

function distanceLinear(x1, y1, x2, y2) {
    return Math.max(1.0 - _distance(x1, y1, x2, y2) / MAX_DISTANCE, 0.0);
}

function distanceManhattan(x1, y1, x2, y2) {
    return Math.max(1.0 - (Math.abs(x1 - x2) + Math.abs(y1 - y2)) / MAX_DISTANCE, 0.0);
}

function distanceDecaySmall(x1, y1, x2, y2) {
    return _distanceDecay(x1, y1, x2, y2, SMALL_LAMBDA);
}

function distanceDecayMedium(x1, y1, x2, y2) {
    return _distanceDecay(x1, y1, x2, y2, MEDIUM_LAMBDA);
}

function distanceDecayLarge(x1, y1, x2, y2) {
    return _distanceDecay(x1, y1, x2, y2, LARGE_LAMBDA);
}
