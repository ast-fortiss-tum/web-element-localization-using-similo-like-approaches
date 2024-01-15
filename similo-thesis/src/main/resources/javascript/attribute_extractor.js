function attribute_extractor(element) {
    const items = {};
    for (let index = 0; index < element.attributes.length; ++index) {
        items[element.attributes[index].name] = element.attributes[index].value
    }
    return items;
}

