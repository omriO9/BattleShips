package com.example.omri.battleShip;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.omri.battleShip.Data.shipsOpenHelper;


public class TableFragment extends Fragment {

    private static final String TAG = TableFragment.class.getSimpleName();

    private shipsOpenHelper dbHelper;
    private SQLiteDatabase db ;

    private String difficulty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: inside here");
        dbHelper= new shipsOpenHelper(getActivity());// might be getContext @@@
        db =dbHelper.getReadableDatabase();
        if (getArguments()!=null) {
            Log.d(TAG, "onCreate: getArguments != null");
            difficulty = getArguments().getString("difficulty");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_table, container, false);
        TableLayout table = (TableLayout)view.findViewById(R.id.table_layout);

        // trying to set my table
        db.beginTransaction();




        TableRow rowHeader = new TableRow(getActivity());
        rowHeader.setBackgroundColor(Color.parseColor("#000000"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


        String[] headerText={"#","Name","Score"};
        for(String c:headerText) {
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);
            tv.setPadding(20, 5, 20, 5);
            tv.setTextColor(Color.WHITE);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        table.addView(rowHeader);

        // " WHERE "+shipsOpenHelper.COL_3+    ='Easy'


        try{
            //String selectQuery = "SELECT* FROM "+ shipsOpenHelper.SCORES_TABLE+" WHERE "+shipsOpenHelper.COL_3+"='Easy'";
            //String selectQuery = "SELECT * FROM "+ shipsOpenHelper.SCORES_TABLE+"ORDER BY score DESC WHERE "+shipsOpenHelper.COL_3+"='Easy' ";
            Log.d(TAG, "onCreateView: difficulty="+difficulty);
            String selectQuery = "SELECT * FROM "+shipsOpenHelper.SCORES_TABLE+" WHERE difficulty='"+difficulty+"' ORDER BY score ASC LIMIT 3";
            Cursor cursor = db.rawQuery(selectQuery,null);



            if(cursor.getCount() >0){
                int count=0;
                while (cursor.moveToNext()) {
                    // Read columns data
                    Log.d(TAG, "onCreateView: inside while:cursor");
                    TableRow tr = new TableRow(getActivity());
                    if(count%2!=0)
                        tr.setBackgroundColor(Color.LTGRAY);

                    String[] position ={"1","2","3","4","5","6","7","8","9","10"};
                    TextView tv = new TextView(getActivity());
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(20, 5, 20, 5);
                    tv.setText(position[count]);
                    tr.addView(tv);


                    String name = cursor.getString(1);
                    TextView tv1 = new TextView(getActivity());
                    tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv1.setGravity(Gravity.CENTER);
                    tv1.setTextSize(16);
                    tv1.setPadding(20, 5, 20, 5);
                    tv1.setText(name);
                    tr.addView(tv1);


                    Float score = cursor.getFloat(2);
                    TextView tv2 = new TextView(getActivity());
                    tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setTextSize(16);
                    tv2.setPadding(20, 5, 20, 5);
                    tv2.setText(score.toString());
                    tr.addView(tv2);

                    table.addView(tr);
                    count++;
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
