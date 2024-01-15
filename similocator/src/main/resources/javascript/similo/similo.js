/**
 * @typedef {Object} Element
 * @property {string} tag - Basic Properties
 * @property {string} className
 * @property {string} typeName
 * @property {string} name
 * @property {string} id
 * @property {string} value
 * @property {string} href
 * @property {string} allText
 * @property {string} nodeText
 * @property {string} placeholder
 * @property {string} title
 * @property {string} alt
 * @property {number} childrenLength
 * @property {string} xpath - Identification Properties
 * @property {string} cssSelector
 * @property {number} x - Position & Size
 * @property {number} y
 * @property {number} width
 * @property {number} height
 * @property {Object.<string, string>} attributes - Attributes
 * @property {Object.<string, string>} cssStyles
 */

/**
 *
 * @param left {Element}
 * @param right {Element}
 */
function similarity(left, right) {

    try {

        let sum = 0;

        sum += 1.5 * string_equals(left.tag, right.tag);
        sum += 0.5 * string_edit_distance(left.className, right.className);
        sum += 1.5 * string_equals(left.name, right.name);
        sum += 1.5 * string_equals(left.id, right.id);
        sum += 0.5 * string_edit_distance(left.href, right.href);
        sum += 0.5 * string_edit_distance(left.alt, right.alt);
        sum += 0.5 * string_edit_distance(left.xpath, right.xpath);
        sum += 0.5 * string_edit_distance(left.cssSelector, right.cssSelector);
        sum += 0.5 * euclid_similarity(left.x, right.x, left.y, right.y);
        sum += 1.5 * string_edit_distance(left.allText, right.allText);

        return sum;

    } catch (e) {
        throw new Error("Error in similarity(): " + e.message);
    }
}

const validTags = ['input', 'textarea', 'button', 'select', 'a', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
    'li', 'span', 'div', 'p', 'th', 'tr', 'td', 'label', 'svg'];


/**
 * @param target {String}
 */
function similo(target) {

    const element = JSON.parse(target);

    const allElements = document.querySelectorAll('*');
    const elementsArray = [];

    for (const candidate of allElements) {
        if (validTags.includes(candidate.tagName.toLowerCase())) {
            const candidateElement = extract_element_from_web_element(candidate);
            let similarityScore = similarity(element, candidateElement);

            let index = 0;
            for (const element of elementsArray) {
                if (similarityScore > element.similarity) {
                    break;
                }
                index++;
            }

            elementsArray.splice(index, 0, {element: candidate, similarity: similarityScore});
        }
    }
    return elementsArray.map(element => element.element);
}
