function intersectValueCompare(aMap, bMap) {
    if (!aMap || !bMap || Object.keys(aMap).length === 0 || Object.keys(bMap).length === 0) {
        return 0.0;
    }

    const maxSize = Math.max(Object.keys(aMap).length, Object.keys(bMap).length);

    let intersect = 0;

    for (const key in aMap) {
        if (aMap.hasOwnProperty(key) && bMap.hasOwnProperty(key)) {
            if (aMap[key] === bMap[key]) {
                intersect++;
            }
        }
    }

    return intersect / maxSize;
}


function intersectKeyCompare(aMap, bMap) {
    if (!aMap || !bMap || Object.keys(aMap).length === 0 || Object.keys(bMap).length === 0) {
        return 0.0;
    }

    const aKeys = new Set(Object.keys(aMap));
    const bKeys = new Set(Object.keys(bMap));

    const union = new Set([...aKeys, ...bKeys]);

    const intersect = new Set([...aKeys].filter(x => bKeys.has(x)));

    return intersect.size === 0 ? 0.0 : intersect.size / union.size;
}
