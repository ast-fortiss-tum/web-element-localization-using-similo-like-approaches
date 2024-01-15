const THRESHOLD = 50;

function _nullOrEmpty(s1, s2) {
    return s1 === null || s2 === null || s1 === '' || s2 === '';
}


function equality(s1, s2) {
    if (_nullOrEmpty(s1, s2)) {
        return 0.0;
    }

    return s1 === s2 ? 1.0 : 0.0;
}

function equalityIgnoreCase(s1, s2) {
    if (_nullOrEmpty(s1, s2)) {
        return 0.0;
    }

    return s1.toLowerCase() === s2.toLowerCase() ? 1.0 : 0.0;
}

// This implementation of the Levenshtein distance algorithm is adapted from
// Apache Commons Lang, Apache Commons Text (specifically, the StringUtils and LevenshteinDistance classes).
// https://commons.apache.org/proper/commons-text/

function levenshtein(s1, s2) {
    if (_nullOrEmpty(s1, s2)) {
        return 0.0;
    }

    let n = s1.length;
    let m = s2.length;

    if (n === 0) return m <= THRESHOLD ? m : -1;
    if (m === 0) return n <= THRESHOLD ? n : -1;

    if (n > m) {
        [s1, s2] = [s2, s1];
        [n, m] = [m, n];
    }

    if (m - n > THRESHOLD) {
        return -1;
    }

    const previous = new Array(n + 1);
    const current = new Array(n + 1);

    let boundary = Math.min(n, THRESHOLD) + 1;
    for (let i = 0; i < boundary; i++) {
        previous[i] = i;
    }
    previous.fill(Infinity, boundary);

    current.fill(Infinity);

    for (let j = 1; j <= m; j++) {
        const charS2 = s2.charAt(j - 1);
        current[0] = j;

        let min = Math.max(1, j - THRESHOLD);
        let max = j > Infinity - THRESHOLD ? n : Math.min(n, j + THRESHOLD);

        if (min > 1) current[min - 1] = Infinity;

        let lowerBound = Infinity;
        for (let i = min; i <= max; i++) {
            if (s1.charAt(i - 1) === charS2) {
                current[i] = previous[i - 1];
            } else {
                current[i] = 1 + Math.min(current[i - 1], previous[i], previous[i - 1]);
            }
            lowerBound = Math.min(lowerBound, current[i]);
        }

        if (lowerBound > THRESHOLD) {
            return -1;
        }

        [previous, current] = [current, previous];
    }

    return previous[n] <= THRESHOLD ? previous[n] : -1;
}

function jaccard(s1, s2) {
    if (_nullOrEmpty(s1, s2)) {
        return 0.0;
    }

    const s1Tokens = s1.split(/\s+|\n/);
    const s2Tokens = s2.split(/\s+|\n/);

    const set1 = new Set(s1Tokens);
    const set2 = new Set(s2Tokens);

    const intersection = new Set([...set1].filter(x => set2.has(x)));

    return intersection.size === 0 ? 0.0 : intersection.size / (set1.size + set2.size - intersection.size);
}

function matches(first, second) {
    let max, min;
    if (first.length > second.length) {
        max = first;
        min = second;
    } else {
        max = second;
        min = first;
    }

    const range = Math.max(Math.floor(max.length / 2) - 1, 0);
    const matchIndexes = new Array(min.length).fill(-1);
    const matchFlags = new Array(max.length).fill(false);

    let matches = 0;
    for (let mi = 0; mi < min.length; mi++) {
        const c1 = min.charAt(mi);
        for (let xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length); xi < xn; xi++) {
            if (!matchFlags[xi] && c1 === max.charAt(xi)) {
                matchIndexes[mi] = xi;
                matchFlags[xi] = true;
                matches++;
                break;
            }
        }
    }

    const ms1 = [];
    const ms2 = [];
    for (let i = 0, si = 0; i < min.length; i++) {
        if (matchIndexes[i] !== -1) {
            ms1[si] = min.charAt(i);
            si++;
        }
    }
    for (let i = 0, si = 0; i < max.length; i++) {
        if (matchFlags[i]) {
            ms2[si] = max.charAt(i);
            si++;
        }
    }

    let halfTranspositions = 0;
    for (let mi = 0; mi < ms1.length; mi++) {
        if (ms1[mi] !== ms2[mi]) {
            halfTranspositions++;
        }
    }

    let prefix = 0;
    for (let mi = 0; mi < Math.min(4, min.length); mi++) {
        if (first.charAt(mi) !== second.charAt(mi)) {
            break;
        }
        prefix++;
    }

    return [matches, halfTranspositions, prefix];
}


function jaroWinkler(s1, s2) {
    const defaultScalingFactor = 0.1;

    if (s1 === null || s2 === null) {
        throw new Error("Strings must not be null");
    }

    if (s1 === s2) {
        return 1.0;
    }

    const mtp = matches(s1, s2);
    const m = mtp[0];
    if (m === 0) {
        return 0.0;
    }
    const j = (m / s1.length + m / s2.length + (m - mtp[1] / 2) / m) / 3;
    return j < 0.7 ? j : j + defaultScalingFactor * mtp[2] * (1 - j);
}

function stringSet(s1, s2) {
    if (_nullOrEmpty(s1, s2)) {
        return 0.0;
    }

    const s1Tokens = s1.split(/\s+|\n/);
    const s2Tokens = s2.split(/\s+|\n/);

    const set1 = new Set(s1Tokens);
    const set2 = new Set(s2Tokens);

    const union = new Set([...set1, ...set2]);

    const intersection = new Set([...set1].filter(x => set2.has(x)));

    return intersection.size === 0 ? 0.0 : intersection.size / union.size;
}
