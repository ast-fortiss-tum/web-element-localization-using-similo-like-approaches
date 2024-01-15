/**
 * Extracts direct text from the element
 * @param element - The DOM element for which the text is needed.
 * @returns {null|string} The direct text of the element.
 */
function direct_text_extractor(element) {
    let node = arguments[0].firstChild;
    while (node) {
        if (node.nodeType === 3) {  // Check if it's a text node
            return node.nodeValue.trim();
        }
        node = node.nextSibling;
    }
    return null;
}
