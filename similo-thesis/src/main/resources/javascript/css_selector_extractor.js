function css_selector_extractor(element) {
    if (!(element instanceof Element)) return;

    if (element.id) return '#' + cssEscape(element.id);

    const path = [];
    while (element && element.nodeType === Node.ELEMENT_NODE) {
        let selector = element.nodeName.toLowerCase();

        if (element.className && typeof element.className === 'string') {
            const classes = element.className.split(/\s+/).filter(Boolean);
            selector += classes.length ? '.' + classes.map(name => cssEscape(name)).join('.') : '';
        }

        let nth = 1, hasSameTypeSib = false;
        let sib = element.previousElementSibling;
        while (true) {
            if (!sib) break; // Exit loop if sib is null or undefined

            if (sib.nodeName.toLowerCase() === selector) {
                nth++;
                hasSameTypeSib = true;
            }

            sib = sib.previousElementSibling;
        }

        if (hasSameTypeSib) {
            selector += ':nth-of-type(' + nth + ')';
        }

        path.unshift(selector);
        element = element.parentNode;
    }

    return path.join(' > ');
}

function cssEscape(value) {
    return String(value).replace(/[!"#$%&'()*+,./:;<=>?@[\\]^{|}~]/g, '\\$&');
}
