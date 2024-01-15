/**
 * Check if the element is visible on the page.
 * @param element - The DOM element for which the visibility is needed.
 * @returns {boolean} True if the element is visible, false otherwise.
 * @private
 */
function _elementIsVisible(element) {
    return !(getComputedStyle(element).visibility === 'hidden' ||
        element.getBoundingClientRect().height === 0);
}

/**
 * Extracts the element from a given XPath.
 * @param xpath - The XPath from which the element is needed.
 * @returns {{}} The element.
 */
function extract_element_from_xpath(xpath) {
    const element = document.evaluate(xpath, document, null,
        XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;

    return extract_element_from_web_element(element);
}

/**
 * Extracts the element from a given web element.
 * @param webElement - The web element from which the element is needed.
 * @returns {Element} The element.
 */
function extract_element_from_web_element(webElement) {
    const rect = webElement.getBoundingClientRect();

    return {
        // Basic Properties
        tag: webElement.tagName,
        innerText: webElement.innerText || null,
        textContent: webElement.textContent || null,
        nodeText: direct_text_extractor(webElement) || null,
        childrenLength: webElement.children.length,

        // Identification Properties
        xpath: {
            chrome: chromeXPathExtractor(webElement),
            selenium: seleniumXPathExtractor(webElement),
            idxpath: idXPathExtractor(webElement),
        },
        cssSelector: css_selector_extractor(webElement),

        // Position & Size
        x: Math.round(rect.x),
        y: Math.round(rect.y),
        width: Math.round(rect.width),
        height: Math.round(rect.height),

        // Attributes & Styles
        attributes: attributes_extractor(webElement),
        cssStyles: css_styles_extractor(webElement)
    };
}

function extractVisualOverlapFromWebElements(webElement) {
    const targetRect = webElement.getBoundingClientRect();

    // Get all web elements
    const webElements = document.querySelectorAll('*');

    const overlap = [];
    for (let i = 0; i < webElements.length; i++) {
        const webElement2 = webElements[i];
        if (_elementIsVisible(webElement2) && webElement2 !== webElement) {

            const candidateRect = webElement2.getBoundingClientRect();

            const intersection = _intersection(targetRect, candidateRect);
            const union = _union(targetRect, candidateRect);

            const intersectionArea = intersection.width * intersection.height;
            const unionArea = union.width * union.height;

            const overlapRatio = intersectionArea / unionArea;

            if (overlapRatio > 0.85 && _centerIsIn(targetRect, candidateRect)) {
                overlap.push(webElement2);
            }
        }
    }

    return {
        target: extract_element_from_web_element(webElement),
        overlap: overlap.map(x => extract_element_from_web_element(x))
    }
}

/**
 * Calculates the intersection of two rectangles.
 * @param targetRect {DOMRect} The target rectangle.
 * @param candidateRect {DOMRect} The candidate rectangle.
 * @returns {DOMRect} The intersection rectangle.
 * @private
 */
function _intersection(targetRect, candidateRect) {
    let tx1 = targetRect.x;
    let ty1 = targetRect.y;

    let rx1 = candidateRect.x;
    let ry1 = candidateRect.y;

    let tx2 = tx1;
    tx2 += targetRect.width;
    let ty2 = ty1;
    ty2 += targetRect.height;

    let rx2 = rx1;
    rx2 += candidateRect.width;
    let ry2 = ry1;
    ry2 += candidateRect.height;

    if (tx1 < rx1) {
        tx1 = rx1;
    }
    if (ty1 < ry1) {
        ty1 = ry1;
    }
    if (tx2 > rx2) {
        tx2 = rx2;
    }
    if (ty2 > ry2) {
        ty2 = ry2;
    }

    tx2 -= tx1;
    ty2 -= ty1;

    if (tx2 < (-Infinity)) tx2 = (-Infinity);
    if (ty2 < (-Infinity)) ty2 = (-Infinity);

    return new DOMRect(tx1, ty1, tx2, ty2);
}

/**
 * Calculates the union of two rectangles.
 * @param targetRect {DOMRect} The target rectangle.
 * @param candidateRect {DOMRect} The candidate rectangle.
 * @returns {DOMRect} The union rectangle.
 * @private
 */
function _union(targetRect, candidateRect) {
    let tx2 = targetRect.width;
    let ty2 = targetRect.height;

    if ((tx2 | ty2) < 0) {
        return candidateRect;
    }

    let rx2 = candidateRect.width;
    let ry2 = candidateRect.height;

    if ((rx2 | ry2) < 0) {
        return targetRect;
    }

    let tx1 = targetRect.x;
    let ty1 = targetRect.y;

    tx2 += tx1;
    ty2 += ty1;

    let rx1 = candidateRect.x;
    let ry1 = candidateRect.y;

    rx2 += rx1;
    ry2 += ry1;

    if (tx1 > rx1) {
        tx1 = rx1;
    }
    if (ty1 > ry1) {
        ty1 = ry1;
    }
    if (tx2 < rx2) {
        tx2 = rx2;
    }
    if (ty2 < ry2) {
        ty2 = ry2;
    }

    tx2 -= tx1;
    ty2 -= ty1;

    if (tx2 > Infinity) {
        tx2 = Infinity;
    }
    if (ty2 > Infinity) {
        ty2 = Infinity;
    }

    return new DOMRect(tx1, ty1, tx2, ty2);
}

/**
 * Checks if the center of the target rectangle is inside the candidate rectangle.
 * @param targetRect {DOMRect} The target rectangle.
 * @param candidateRect {DOMRect} The candidate rectangle.
 * @returns {boolean} True if the center of the target rectangle is inside the candidate rectangle, false otherwise.
 */
function _centerIsIn(targetRect, candidateRect) {
    const targetCenterX = targetRect.x + targetRect.width / 2;
    const targetCenterY = targetRect.y + targetRect.height / 2;

    return targetCenterX >= candidateRect.x && targetCenterX <= candidateRect.x + candidateRect.width &&
        targetCenterY >= candidateRect.y && targetCenterY <= candidateRect.y + candidateRect.height;
}

/**
 *
 * @returns {Array} An array of all the elements on the page.
 */
function extractAllElements() {
    const elements = document.querySelectorAll('*');
    const allElements = [];
    for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        if (_elementIsVisible(element)) {
            allElements.push([extract_element_from_web_element(element), element]);
        }
    }
    return allElements;
}
