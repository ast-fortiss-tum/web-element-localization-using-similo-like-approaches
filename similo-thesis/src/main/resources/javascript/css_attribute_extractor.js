function css_attribute_extractor(element) {
    const computedStyles = window.getComputedStyle(element);
    const styles = {};
    for (let i = 0; i < computedStyles.length; i++) {
        const prop = computedStyles[i];
        styles[prop] = computedStyles.getPropertyValue(prop);
    }
    return styles;
}
