//package com.wintermute.gmassistant.adapters;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Adapter<> extends ArrayAdapter
//{
//
//    HashMap<Integer, Boolean> checked = new HashMap<Integer, Boolean>();
//    Context context;
//    private int[] colors = new int[] {Color.parseColor("#D0D0D0"), Color.parseColor("#D8D8D8") };
//    public Adapter(Context context, int resource, int textViewResourceId, ArrayList weekdays) {
//        super(context, resource, textViewResourceId, weekdays);
//        this.context=context;
//        this.weekdays=weekdays;
//        for(int i=0;i<weekdays.size();i++)checked.put(i, false);
//    }
//
//    public Adapter(Context context, int resource, int textViewResourceId, ArrayList weekdays, ArrayListoldweekdays) {
//        super(context, resource, textViewResourceId, weekdays);
//        this.context=context;
//        this.weekdays=weekdays;
//        for(int i=0;i<weekdays.size();i++)checked.put(i, false);
//        for(int i=0;i<oldweekdays.size();i++)checked.put(oldweekdays.get(i),true);
//    }
//
//    public void toggle(int position){
//        if(checked.get(position))checked.put(position, false);
//        else checked.put(position, true);
//        notifyDataSetChanged();
//    }
//
//    public ArrayList getCheckedItemPosition(){
//        ArrayListcheck = new ArrayList();
//        for(int i=0;i<checked.size();i++){
//            if(checked.get(i))check.add(i);
//        }
//        return check;
//    }
//
//    public ArrayList getCheckedItems(){
//        ArrayList check = new ArrayList();
//        for(int i=0;i<checked.size();i++){
//            if(checked.get(i))check.add(weekdays.get(i));
//        }
//        return check;
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//
//        if(row == null){
//            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            row = vi.inflate(R.layout.row_item, null);
//        }
//
//        CheckedTextView checkedTextView = (CheckedTextView)row.findViewById(R.id.checkedtextview);
//        checkedTextView.setText(weekdays.get(position));
//        row.setBackgroundColor(colors[position % colors.length]);
//        return row;
//    }
//}
