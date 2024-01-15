function direct_text_extractor(element) {
    let node = arguments[0].firstChild;
    while (node) {
        if (node.nodeType === 3) {  // Check if it's a text node
            return node.nodeValue.trim();
        }
        node = node.nextSibling;
    }
    return null;  // Return null if no direct text found
}