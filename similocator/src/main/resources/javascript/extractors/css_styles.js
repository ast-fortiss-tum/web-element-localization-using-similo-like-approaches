/**
 * Extracts all CSS styles from the given element.
 * @param element - The DOM element for which the CSS styles are needed.
 * @returns {{}} The CSS styles of the element as a map.
 */
function css_styles_extractor(element) {
    const computedStyles = window.getComputedStyle(element);
    const styles = {};
    for (let i = 0; i < computedStyles.length; i++) {
        const prop = computedStyles[i];
        styles[prop] = computedStyles.getPropertyValue(prop);
    }
    return styles;
}
