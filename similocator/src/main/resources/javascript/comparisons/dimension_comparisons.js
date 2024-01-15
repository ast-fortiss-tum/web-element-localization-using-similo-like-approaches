function _ration(a, b) {
    if (a === 0 || b === 0) {
        return 0.0;
    }
    return Math.min(a, b) / Math.max(a, b);
}

function area(width1, height1, width2, height2) {
    return ratio(width1 * height1, width2 * height2);
}

function aspectRatio(width1, height1, width2, height2) {
    if (width1 === 0 || height1 === 0 || width2 === 0 || height2 === 0) {
        return 0.0;
    }

    return _ration(width1 / height1, width2 / height2);
}

function perimeter(width1, height1, width2, height2) {
    return ratio((width1 * 2 + height1 * 2), (width2 * 2 + height2 * 2));
}
