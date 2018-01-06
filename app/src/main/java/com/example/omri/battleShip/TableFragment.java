package com.example.omri.battleShip;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
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
import android.widget.Toast;

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
        Toast.makeText(getContext(), "diff"+difficulty, Toast.LENGTH_SHORT).show();
        View view =inflater.inflate(R.layout.fragment_table, container, false);
        TableLayout table = (TableLayout)view.findViewById(R.id.table_layout);

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
    public void setTable(String difficulty){

    }

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public TableFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment TableFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static TableFragment newInstance(String param1, String param2) {
//        TableFragment fragment = new TableFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }


//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
