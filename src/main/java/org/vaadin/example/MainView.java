package org.vaadin.example;

import com.vaadin.flow.component.spreadsheet.Spreadsheet;
import com.vaadin.kubernetes.starter.sessiontracker.UnserializableComponentWrapper;
import org.apache.poi.ss.util.CellRangeAddress;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * https://github.com/vaadin/docs/pull/4118/files
 */
@Route
public class MainView extends VerticalLayout {

    private transient Spreadsheet spreadsheet = new Spreadsheet();

    public MainView() {
        this.setSizeFull();

        spreadsheet.setWidth("500px");

        spreadsheet.createCell(1, 0, "Nicolaus");
        spreadsheet.createCell(1, 1, "Copernicus");
        UnserializableComponentWrapper<WorkbookData, Spreadsheet> wrapper = new UnserializableComponentWrapper<>(
                spreadsheet,
                MainView::serializer,
                MainView::deserializer
        );

        this.addAndExpand(wrapper);
    }

    private static WorkbookData serializer(Spreadsheet sheet) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            sheet.write(baos);
            String selection = Optional
                    .ofNullable(sheet.getCellSelectionManager()
                            .getSelectedCellRange())
                    .map(CellRangeAddress::formatAsString).orElse(null);
            return new WorkbookData(baos.toByteArray(), selection);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Spreadsheet deserializer(WorkbookData data) {
        try {
            Spreadsheet sheet = new Spreadsheet(
                    new ByteArrayInputStream(data.getData()));
            if (data.getSelection() != null) {
                sheet.setSelection(data.getSelection());
            }
            return sheet;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
