package com.example.omri.battleShip;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.omri.battleShip.Data.shipsOpenHelper;

import java.util.ArrayList;


public class TableFragment extends Fragment {

    private static final String TAG = TableFragment.class.getSimpleName();

    private shipsOpenHelper dbHelper;
    private SQLiteDatabase db ;
    private final ArrayList<FragmentListener> mListeners
            = new ArrayList<>();
    private String difficulty;
    private LayoutInflater inflater;
    private ViewGroup container;
    private TableLayout table;

    /**Last selected row+it's color**/
    private TableRow selectedRow;
    private int selectedRowColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: inside here");
        dbHelper= new shipsOpenHelper(getActivity());// might be getContext @@@
        db =dbHelper.getReadableDatabase();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.inflater=inflater;
        this.container=container;
        if (getArguments()!=null) {
            Log.d(TAG, "onCreate: getArguments != null");
            difficulty = getArguments().getString("difficulty");
        }
        Log.d(TAG, "onCreateView: TableFragment diff "+difficulty);
        View v=inflateTable(difficulty);
        return v;
    }

    public View inflateTable(String difficulty) {
        Log.d(TAG, "inflateTable: difficulty="+difficulty);
        View view =inflater.inflate(R.layout.fragment_table, container, false);
        table = (TableLayout)view.findViewById(R.id.table_layout);

        // trying to set my table
        db =dbHelper.getReadableDatabase();
        db.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM "+shipsOpenHelper.SCORES_TABLE+" WHERE difficulty='"+difficulty+"' ORDER BY score ASC LIMIT 10";
            Cursor cursor = db.rawQuery(selectQuery,null);

            if(cursor.getCount() >0){
                int count=0;
                String[] position ={"1","2","3","4","5","6","7","8","9","10"};
                while (cursor.moveToNext()) {
                    // Read columns data
                    final TableRow tr = (TableRow)LayoutInflater.from(getActivity()).inflate(R.layout.table_row, null);
                    table.addView(tr);

                    if(count%2!=0)
                        tr.setBackgroundColor(Color.LTGRAY);

                    final TextView rowNumber = (TextView)tr.findViewById(R.id.rowNumber);
                    rowNumber.setText(position[count]);
                    int id = cursor.getInt(0);
                    tr.setId(id);
                    TextView rowName = (TextView)tr.findViewById(R.id.rowName);
                    rowName.setText(cursor.getString(1));
                    TextView rowScore = (TextView)tr.findViewById(R.id.rowScore);
                    rowScore.setText(cursor.getString(2));
                    count++;

                    // setting click listener of a table row :
                    tr.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {

                            Log.d(TAG, "TableRow clicked: "+rowNumber+" #id="+tr.getId());
                            highLightSelectedRow(tr);
                       tr.setBackgroundColor(Color.WHITE);
                            tr.setAlpha(0.4f);
                            sendUpdateToActivity(tr.getId());
                        }
                    });
                }
            }
            db.setTransactionSuccessful();
        }catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return view;
    }

    // need to copy this code to the Map Fragment aswell!!!
    public void registerListener(FragmentListener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(FragmentListener listener) {
        mListeners.remove(listener);
    }

    private void sendUpdateToActivity(int id) {
        for (int i=mListeners.size()-1; i>=0; i--) {
            mListeners.get(i).sendUpdate(id,true); // true = I come from tableFragment
        }
    }
    public void selectRow(int id){
        for (int i = 0; i < table.getChildCount(); i++) {
            View tableRow = table.getChildAt(i);
                if (tableRow.getId()==id){
                    highLightSelectedRow(tableRow);
                    //tableRow.setBackgroundColor(Color.WHITE);
                    break;
                }
            }
        }

    public void highLightSelectedRow(View tableRow){

        tableRow.requestFocus();
        if (selectedRow!=null) { // there was a row selected - let's repaint it to it's original color.
            selectedRow.setBackgroundColor(selectedRowColor);
            // selectedRow.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
        }
        selectedRow= (TableRow) tableRow;
        ColorDrawable currentColorDrawable = (ColorDrawable) tableRow.getBackground();
        selectedRowColor = currentColorDrawable.getColor();
        tableRow.setBackgroundColor(Color.WHITE);
    }
    }


