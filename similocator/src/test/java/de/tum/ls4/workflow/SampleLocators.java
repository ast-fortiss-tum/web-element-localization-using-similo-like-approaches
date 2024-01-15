package de.tum.ls4.workflow;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SampleLocators {
    public static final SiteData GOOGLE_DATA = new SiteData(
            "google",
            List.of(
                    LocalDate.of(2018, 1, 1),
                    LocalDate.of(2021, 1, 1),
                    LocalDate.of(2023, 1, 1)
            ),
            Map.of(
                    LocalDate.of(2018, 1, 1), List.of(
                            "/html/body/div/div[5]/div[1]/div[1]/a[1]",
                            "/html/body/div/div[5]/div[1]/div[1]/a[2]",
                            "/html/body/div/div[5]/div[1]/div[2]/div[2]/div/div[1]/div[1]/a",
                            "/html/body/div/div[5]/div[1]/div[2]/div[2]/div/div[1]/div[2]/a",
                            "/html/body/div/div[5]/div[1]/div[2]/div[2]/div/div[2]/div[2]/div[1]/a",
                            "/html/body/div/div[5]/div[1]/div[2]/div[2]/div/div[2]/div[3]/div/a",
                            "/html/body/div/div[5]/form/div[2]/div/div[1]/div/div[1]/input",
                            "/html/body/div/div[5]/form/div[2]/div/div[3]/center/input[1]",
                            "/html/body/div/div[5]/form/div[2]/div/div[3]/center/input[2]",
                            "/html/body/div/div[9]/div[1]/div/div/div/div/span[2]/a[1]",
                            "/html/body/div/div[9]/div[1]/div/div/div/div/span[2]/a[2]",
                            "/html/body/div/div[9]/div[1]/div/div/div/div/span[1]/a[1]",
                            "/html/body/div/div[9]/div[1]/div/div/div/div/span[1]/a[2]",
                            "/html/body/div/div[9]/div[1]/div/div/div/div/span[1]/span/a"
                    ), LocalDate.of(2021, 1, 1), List.of(
                            "/html/body/div[2]/div[2]/div[1]/a[1]",
                            "/html/body/div[2]/div[2]/div[1]/a[2]",
                            "/html/body/div[2]/div[2]/div[2]/div/div/div[1]/div[1]/a",
                            "/html/body/div[2]/div[2]/div[2]/div/div/div[1]/div[2]/a",
                            "/html/body/div[2]/div[2]/div[2]/div/div/div[2]/div[1]/div/a",
                            "/html/body/div[2]/div[2]/div[2]/div/div/div[2]/div[2]/div/a",
                            "/html/body/div[2]/div[3]/form/div[2]/div[1]/div[1]/div/div[2]/input",
                            "/html/body/div[2]/div[3]/form/div[2]/div[1]/div[3]/center/input[1]",
                            "/html/body/div[2]/div[3]/form/div[2]/div[1]/div[3]/center/input[2]",
                            "/html/body/div[2]/div[5]/div[1]/div/div/div/div/span[3]/a[1]",
                            "/html/body/div[2]/div[5]/div[1]/div/div/div/div/span[3]/a[2]",
                            "/html/body/div[2]/div[5]/div[1]/div/div/div/div/span[2]/a[1]",
                            "/html/body/div[2]/div[5]/div[1]/div/div/div/div/span[2]/a[2]",
                            "/html/body/div[2]/div[5]/div[1]/div/div/div/div/span[2]/span/a"

                    ), LocalDate.of(2023, 1, 1), List.of(
                            "/html/body/div[1]/div[1]/a[1]",
                            "/html/body/div[1]/div[1]/a[2]",
                            "/html/body/div[1]/div[1]/div/div/div/div[1]/div/div[1]/a",
                            "/html/body/div[1]/div[1]/div/div/div/div[1]/div/div[2]/a",
                            "/html/body/div[1]/div[1]/div/div/div/div[2]/div/div/div/a",
                            "/html/body/div[1]/div[1]/div/div/div/div[2]/a",
                            "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div/div[2]/textarea",
                            "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[4]/center/input[1]",
                            "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[4]/center/input[2]",
                            "/html/body/div[1]/div[5]/div[1]/div[1]/a[1]",
                            "/html/body/div[1]/div[5]/div[1]/div[1]/a[2]",
                            "/html/body/div[1]/div[5]/div[1]/div[3]/a[1]",
                            "/html/body/div[1]/div[5]/div[1]/div[3]/a[2]",
                            "/html/body/div[1]/div[5]/div[1]/div[3]/span/span/g-popup/div[1]/div"
                    )
            ));

    public record SiteData(String site,
                           List<LocalDate> dates,
                           Map<LocalDate, List<String>> xpaths) {
    }
}
