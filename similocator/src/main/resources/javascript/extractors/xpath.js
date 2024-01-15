/**
 * Calculate the index of the current element among its siblings of the same tag name.
 * @param {Element} element - The current element.
 * @returns {number} The index of the current element among its siblings.
 */
function getElementIndex(element) {
    let count = 0;
    let sibling = element;
    while (sibling) {
        if (sibling.localName === element.localName) count++;
        sibling = sibling.previousElementSibling;
    }
    return count;
}

/**
 * Determine if we should append an index to the element in its XPath segment.
 * @param {Element} element - The DOM element to check.
 * @returns {boolean} True if the element should have an index appended, false otherwise.
 */
function shouldAppendIndex(element) {
    const siblings = element.parentElement ? Array.from(element.parentElement.children) : [];
    const similarSiblings = siblings.filter(el => el.localName === element.localName).length;
    return similarSiblings > 1;
}

/**
 * Generate the XPath segments for the given element, considering its hierarchy.
 * @param {Element} element - The element for which the XPath segment is being generated.
 * @param {boolean} cleanIndexed  - True if the XPath should contain indexes for all elements, false otherwise.
 * @returns {Array} An array of XPath segments representing the hierarchy of the element.
 */
function generateXpathSegments(element, cleanIndexed) {
    // Return empty array for invalid nodes
    if (!element || element.nodeType !== 1) {
        return [];
    }

    let index;
    if (cleanIndexed) {
        index = shouldAppendIndex(element) ? `[${getElementIndex(element)}]` : '';
    } else {
        index = `[${getElementIndex(element)}]`;
    }

    const currentSegment = element instanceof HTMLElement
        ? `${element.localName}${index}`
        : `*[local-name()="${element.localName}"]${index}`;

    const parentElement = element.parentElement instanceof Element ? element.parentElement : null;
    return [...generateXpathSegments(parentElement, cleanIndexed), currentSegment];
}

/**
 * Extracts the XPath of the given element.
 * @param {Element} element - The DOM element for which the XPath is needed.
 * @returns {string} The XPath of the element.
 */
function chromeXPathExtractor(element) {
    return "/" + generateXpathSegments(element, true).join('/');
}

/**
 * Extracts the XPath of the given element.
 * @param element - The DOM element for which the XPath is needed.
 * @returns {string} The XPath of the element.
 */
function seleniumXPathExtractor(element) {
    return "/" + generateXpathSegments(element, false).join('/');
}

function isUniqueID(element) {
    return element.id && document.getElementById(element.id) === element;
}

function generateIdXpathSegments(element) {
    if (!element || element.nodeType !== 1) {
        return [];
    }

    if (isUniqueID(element)) {
        return [`//*[@id='${element.id}']`];
    }

    const appendIndex = shouldAppendIndex(element);
    const index = appendIndex ? `[${getElementIndex(element)}]` : '';

    const xpathSegment = element instanceof HTMLElement
        ? `${element.localName}${index}`
        : `*[local-name()="${element.localName}"]${index}`;

    const parentSegments = generateIdXpathSegments(element.parentNode);
    parentSegments.push(xpathSegment);
    return parentSegments;
}

function idXPathExtractor(element) {
    const segments = generateIdXpathSegments(element);
    const xpath = '/' + segments.join('/');
    return segments.length > 0 ? xpath.replaceAll(/\/\/\/+/g, '//') : null;
}
