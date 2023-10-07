package telegram.bot.storage;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import telegram.bot.config.GoogleSheetConfig;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class GoogleSheetUtils {
    private final Sheets sheetService;

    public GoogleSheetUtils(Sheets sheetService) {
        this.sheetService = sheetService;
    }

    public boolean writeCellValue(String sheetName, String cellAddress, String cellValue) {
        return writesValues(sheetName, cellAddress, List.of(List.of(cellValue)));
    }

    public boolean writesValues(String sheetName, String cellAddress, List<List<Object>> values) {
        try {
            var range = sheetName + "!" + cellAddress;
            var body = new ValueRange().setValues(values);
            UpdateValuesResponse result = sheetService.spreadsheets().values()
                    .update(GoogleSheetConfig.getSheetId(), range, body)
                    .setValueInputOption("RAW")
                    .execute();
            pause();
        } catch (IOException e) {
            throw new RuntimeException(e);
            //return false;
        }
        return true;
    }

    public List<String> reagValuesList(String sheetName, String rangeBegin, String rangeEnd) {
        return reagValuesList(sheetName, rangeBegin, rangeEnd, 0);
    }

    public List<String> reagValuesList(String sheetName, String rangeBegin, String rangeEnd, int index) {
        List<String> valuesList = new LinkedList<>();
        readValuesRange(sheetName, rangeBegin, rangeEnd).forEach(values -> valuesList.add(!values.isEmpty() && values.size() >= index + 1 ? values.get(index) : ""));
        return valuesList;
    }

    public List<List<String>> readValuesRange(String sheetName, String rangeBegin, String rangeEnd) {
        List<List<String>> values = new LinkedList<>();
        try {
            var range = sheetName + "!" + rangeBegin + ":" + rangeEnd;

            var objectsInTheRange = Optional
                    .ofNullable(
                            sheetService.spreadsheets()
                                    .values()
                                    .get(GoogleSheetConfig.getSheetId(), range)
                                    .execute()
                                    .getValues())
                    .orElse(Collections.emptyList());
            objectsInTheRange
                    .forEach(currentRowObject -> {
                        var rowStringList = new LinkedList<String>();
                        currentRowObject.forEach(currentCellObject -> rowStringList.add(Optional.ofNullable(currentCellObject).orElse("").toString()));
                        values.add(rowStringList);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pause();
        return values;
    }

    private void pause() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(GoogleSheetConfig.getApiPauseLong());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
