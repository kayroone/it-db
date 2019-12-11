package domain.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Result {

    private ArrayList<Object[]> rows;

    public Result() {

        this.rows = new ArrayList<>();
    }

    public void addRow(Object[] values) {

        this.rows.add(values);
    }

    public ArrayList<Object[]> getRows() {

        return this.rows;
    }

    public List<String> getRow(final int index) {

        List<String> rowValues = new ArrayList<>();

        Arrays.stream(this.rows.get(index))
                .forEach(val -> rowValues.add(val.toString()));

        return rowValues;
    }
}
