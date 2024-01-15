package de.tum.ls4.artifacts;

import java.util.List;
import java.util.Map;

public class SampleElements {
    public static final String ELEMENT_1_IDENTIFIER = "/div/div[2]/span/div";
    public static final Element ELEMENT_1 = new Element(
            "div",
            "div_class",
            "div_type",
            1,
            new Element.Xpath(
                    "/div/div[2]/span/div",
                    "/div/div[2]/span/div[2]",
                    "id"
            ),
            "Text of the node",
            10,
            20,
            5,
            4,
            Map.of("class1", "div-class", "name", "div-name", "id", "div-id"),
            Map.of("css1", "value1", "css2", "value2")
    );
    public static final String ELEMENT_2_IDENTIFIER = "/html/body/section[1]/div";
    public static final Element ELEMENT_2 = new Element(
            "section",
            "section_class",
            "section_type",
            2,
            new Element.Xpath(
                    "/html/body/section[1]/div",
                    "/html/body/section[1]/div[2]",
                    "idSection"
            ),
            "Section node text",
            15,
            25,
            10,
            8,
            Map.of("class2", "section-class", "name", "section-name", "id", "section-id"),
            Map.of("css3", "value3", "css4", "value4")
    );
    public static final String ELEMENT_3_IDENTIFIER = "/html/body/footer";
    public static final Element ELEMENT_3 = new Element(
            "footer",
            "footer_class",
            "footer_type",
            0,
            new Element.Xpath(
                    "/html/body/footer",
                    "/html/body/footer[1]",
                    "idFooter"
            ),
            "Footer node text",
            5,
            10,
            12,
            6,
            Map.of("class3", "footer-class", "name", "footer-name", "id", "footer-id"),
            Map.of("css5", "value5", "css6", "value6")
    );
    public static final String ELEMENT_4_IDENTIFIER = "/html/body/nav";
    public static final Element ELEMENT_4 = new Element(
            "nav",
            "nav_class",
            "nav_type",
            3,
            new Element.Xpath(
                    "/html/body/nav",
                    "/html/body/nav[1]",
                    "idNav"
            ),
            "Navigation node text",
            20,
            30,
            14,
            7,
            Map.of("class4", "nav-class", "name", "nav-name", "id", "nav-id"),
            Map.of("css7", "value7", "css8", "value8")
    );
    public static final String ELEMENT_5_IDENTIFIER = "/html/body/header/div[1]/h1";
    public static final Element ELEMENT_5 = new Element(
            "h1",
            "header_class",
            "header_type",
            0,
            new Element.Xpath(
                    "/html/body/header/div[1]/h1",
                    "/html/body/header/div[1]/h1[1]",
                    "idHeader"
            ),
            "Header node text",
            30,
            40,
            20,
            10,
            Map.of("class5", "header-class", "name", "header-name", "id", "header-id"),
            Map.of("css9", "value9", "css10", "value10")
    );
    public static final String ELEMENT_6_IDENTIFIER = "/html/body/article[1]/p";
    public static final Element ELEMENT_6 = new Element(
            "p",
            "paragraph_class",
            "paragraph_type",
            0,
            new Element.Xpath(
                    "/html/body/article[1]/p",
                    "/html/body/article[1]/p[1]",
                    "idParagraph"
            ),
            "Paragraph node text",
            35,
            45,
            15,
            12,
            Map.of("class6", "paragraph-class", "name", "paragraph-name", "id", "paragraph-id"),
            Map.of("css11", "value11", "css12", "value12")
    );
    public static final String ELEMENT_7_IDENTIFIER = "/html/body/aside";
    public static final Element ELEMENT_7 = new Element(
            "aside",
            "aside_class",
            "aside_type",
            1,
            new Element.Xpath(
                    "/html/body/aside",
                    "/html/body/aside[1]",
                    "idAside"
            ),
            "Aside node text",
            25,
            35,
            18,
            14,
            Map.of("class7", "aside-class", "name", "aside-name", "id", "aside-id"),
            Map.of("css13", "value13", "css14", "value14")
    );
    public static final String ELEMENT_8_IDENTIFIER = "/html/body/main/section[1]";
    public static final Element ELEMENT_8 = new Element(
            "section",
            "main_class",
            "main_type",
            2,
            new Element.Xpath(
                    "/html/body/main/section[1]",
                    "/html/body/main/section[1][1]",
                    "idMainSection"
            ),
            "Main section node text",
            50,
            60,
            22,
            16,
            Map.of("class8", "main-section-class", "name", "main-section-name", "id", "main-section-id"),
            Map.of("css15", "value15", "css16", "value16")
    );
    public static final String ELEMENT_9_IDENTIFIER = "/div/div[2]/span/a";
    public static final Element ELEMENT_9 = new Element(
            "a",
            "a_class",
            "a_type",
            3,
            new Element.Xpath(
                    "/div/div[2]/span/a",
                    "/div/div[2]/span/a[2]",
                    "id"
            ),
            "//div[@id='some_id']",
            10,
            20,
            5,
            4,
            Map.of("attr1", "value1", "attr2", "value2"),
            Map.of("css1", "value1", "css2", "value2")
    );
    // Initial element
    public static final String ELEMENT_LOC_OPT_1_IDENTIFIER = "/html/body/div/div[2]/span/div";
    public static final Element ELEMENT_LOC_OPT_1 = new Element(
            "div",
            "div_class",
            "div_type",
            1,
            new Element.Xpath(
                    "/html/body/div/div[2]/span/div",
                    "/html/body/div/div[2]/span/div[2]",
                    "//div[@id='div-id']"
            ),
            "#some_id > div:nth-child(2) > span > div",
            10,
            20,
            5,
            4,
            Map.of("class", "div-class", "name", "div-name", "id", "div-id"),
            Map.of("css1", "value1", "css2", "value2")
    );
    // XPath locator and CSS selector locator change, id and id xpath locator stay the same
    // Optimal locator should now be id
    public static final String ELEMENT_LOC_OPT_2_IDENTIFIER = "/html/body/div/div[2]/span/div";
    public static final Element ELEMENT_LOC_OPT_2 = new Element(
            "div",
            "div_class",
            "div_type",
            1,
            new Element.Xpath(
                    "/html/body/div/div[3]/span/div",
                    "/html/body/div/div[3]/span/div[2]",
                    "//div[@id='div-id']"
            ),
            "#some_id > div:nth-child(3) > span > div",
            10,
            20,
            5,
            4,
            Map.of("class", "div-class", "name", "div-name", "id", "div-id"),
            Map.of("css1", "value1", "css2", "value2")
    );
    // XPath locator and CSS selector locator change, id_xpath stays the same, id is removed
    // Optimal locator should now be id_xpath
    public static final String ELEMENT_LOC_OPT_3_IDENTIFIER = "/html/body/div/div[2]/span/div";
    public static final Element ELEMENT_LOC_OPT_3 = new Element(
            "div",
            "div_class",
            "div_type",
            1,
            new Element.Xpath(
                    "/html/body/div/div[4]/span/div",
                    "/html/body/div/div[4]/span/div[2]",
                    "//div[@id='span-id']/div"
            ),
            "#some_id > div:nth-child(4) > span > div",
            10,
            20,
            5,
            4,
            Map.of("class", "div-class", "name", "div-name"),
            Map.of("css1", "value1", "css2", "value2")
    );
    // XPath locator stays the same, CSS is removed, id_xpaths changes, id is removed
    // Optimal locator should now be xpath
    public static final String ELEMENT_LOC_OPT_4_IDENTIFIER = "/html/body/div/div[2]/span/div";
    public static final Element ELEMENT_LOC_OPT_4 = new Element(
            "div",
            "div_class",
            "div_type",
            1,
            new Element.Xpath(
                    "/html/body/div/div[4]/span/div",
                    "/html/body/div/div[4]/span/div[2]",
                    "//div[@id='body-id']/div/span/div"
            ),
            "",
            10,
            20,
            5,
            4,
            Map.of("class", "div-class", "name", "div-name"),
            Map.of("css1", "value1", "css2", "value2")
    );
    public static List<Element> ELEMENTS = List.of(
            ELEMENT_1,
            ELEMENT_2,
            ELEMENT_3,
            ELEMENT_4,
            ELEMENT_5,
            ELEMENT_6,
            ELEMENT_7,
            ELEMENT_8,
            ELEMENT_9
    );
    public static List<String> ELEMENTS_IDENTIFIERS = List.of(
            ELEMENT_1_IDENTIFIER,
            ELEMENT_2_IDENTIFIER,
            ELEMENT_3_IDENTIFIER,
            ELEMENT_4_IDENTIFIER,
            ELEMENT_5_IDENTIFIER,
            ELEMENT_6_IDENTIFIER,
            ELEMENT_7_IDENTIFIER,
            ELEMENT_8_IDENTIFIER,
            ELEMENT_9_IDENTIFIER
    );
    public static List<Element> ELEMENTS_LOC_OPT = List.of(
            ELEMENT_LOC_OPT_1,
            ELEMENT_LOC_OPT_2,
            ELEMENT_LOC_OPT_3,
            ELEMENT_LOC_OPT_4
    );
    public static List<String> ELEMENTS_LOC_OPT_IDENTIFIERS = List.of(
            ELEMENT_LOC_OPT_1_IDENTIFIER,
            ELEMENT_LOC_OPT_2_IDENTIFIER,
            ELEMENT_LOC_OPT_3_IDENTIFIER,
            ELEMENT_LOC_OPT_4_IDENTIFIER
    );
    public static List<Overlap> OVERLAPS = List.of(
            new Overlap(ELEMENT_1, List.of(ELEMENT_2)),
            new Overlap(ELEMENT_2, List.of(ELEMENT_1, ELEMENT_3)),
            new Overlap(ELEMENT_3, List.of(ELEMENT_2, ELEMENT_4)),
            new Overlap(ELEMENT_4, List.of(ELEMENT_3, ELEMENT_2)),
            new Overlap(ELEMENT_5, List.of()),
            new Overlap(ELEMENT_6, List.of(ELEMENT_7, ELEMENT_8, ELEMENT_9)),
            new Overlap(ELEMENT_7, List.of(ELEMENT_6, ELEMENT_8, ELEMENT_9)),
            new Overlap(ELEMENT_8, List.of(ELEMENT_6, ELEMENT_7, ELEMENT_9)),
            new Overlap(ELEMENT_9, List.of(ELEMENT_6, ELEMENT_7, ELEMENT_8))
    );
    public static List<Overlap> OVERLAPS_LOC_OPT = List.of(
            new Overlap(ELEMENT_LOC_OPT_1, List.of(ELEMENT_LOC_OPT_2)),
            new Overlap(ELEMENT_LOC_OPT_2, List.of(ELEMENT_LOC_OPT_1, ELEMENT_LOC_OPT_3)),
            new Overlap(ELEMENT_LOC_OPT_3, List.of(ELEMENT_LOC_OPT_2, ELEMENT_LOC_OPT_4)),
            new Overlap(ELEMENT_LOC_OPT_4, List.of())
    );
}
