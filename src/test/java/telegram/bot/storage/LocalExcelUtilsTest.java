package telegram.bot.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


@ExtendWith(SpringExtension.class)
@TestPropertySource("file:${LOCAL_CONFIG_DIR}/localExcel.properties")
class LocalExcelUtilsTest {
    private GoogleSheetUtils googleSheetUtils;
    private LocalExcelUtils localExcelUtils;

    @Value("${local.storage.path}")
    private String pathToExcelFile;

    @Test
    void initExcelFileTest() {
        System.out.println(pathToExcelFile);
        assertThat(pathToExcelFile, equalTo("$MODULE_WORKING_DIR$\\local_config\\localstorage.xlsx"));
        System.out.println();
    }


    @Test
    void writeCellValue() {
        //  GoogleSheetUtils googleSheetUtils = new GoogleSheetUtils()
        localExcelUtils = new LocalExcelUtils(pathToExcelFile);
        localExcelUtils.writeCellValue("Волонтеры", "R3C3", "TestValue");
    }

    @Test
    void writesValues() {
    }

    @Test
    void readValuesList() {
    }

    @Test
    void readValuesRange() {
    }

    @Test
    void readXLSXFile() {
    }
}