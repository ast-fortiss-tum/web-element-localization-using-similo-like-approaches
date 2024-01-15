/**
 * Extracts all attributes from an element and returns them as a map.
 * @param element - The DOM element for which the attributes are needed.
 * @returns {{}} The attributes of the element as a map.
 */
function attributes_extractor(element) {
    const items = {};
    for (let index = 0; index < element.attributes.length; ++index) {
        items[element.attributes[index].name] = element.attributes[index].value
    }
    return items;
}
