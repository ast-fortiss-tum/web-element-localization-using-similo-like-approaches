function elementIsVisible(element) {
    return !(getComputedStyle(element).visibility === 'hidden' ||
        element.getBoundingClientRect().height === 0);
}

function element_extractor(element) {
    const rect = element.getBoundingClientRect();

    return {
        // Basic Properties
        tag: element.tagName,
        innerText: element.innerText || null,
        textContent: element.textContent || null,
        nodeText: direct_text_extractor(element) || null,
        childrenLength: element.children.length,

        // Identification Properties
        xpath: {
            chrome: xpath_chrome_extractor(element),
            selenium: xpath_selenium_extractor(element),
            idxpath: id_xpath_extractor(element),
        },
        cssSelector: css_selector_extractor(element),

        // Position & Size
        location: [Math.round(rect.x), Math.round(rect.y)],
        dimension: [Math.round(rect.width), Math.round(rect.height)],

        // Attributes & Styles
        attributes: attribute_extractor(element),
        cssStyles: css_attribute_extractor(element)
    };
}

function element_extractor_string(element) {
    return JSON.stringify(element_extractor(element));
}

function elements_extractor() {

    const elements = document.querySelectorAll('*');
    const validTags = ['input', 'textarea', 'button', 'select', 'a', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
        'li', 'span', 'div', 'p', 'th', 'tr', 'td', 'label', 'svg', 'img', 'iframe', 'i', 'strong'];
    const elementsData = [];

    elements.forEach(function (webElement) {
        if (validTags.includes(webElement.tagName.toLowerCase())
            && elementIsVisible(webElement)) {
            elementsData.push(element_extractor(webElement));
        }
    });

    return JSON.stringify(elementsData);
}
