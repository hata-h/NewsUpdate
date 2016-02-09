package tokyo.olt.newsupdate;

/**
 * Created by hata on 15/12/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hata on 15/11/30.
 */
public class AdapterListURL extends ArrayAdapter<BeanURL> {
    public AdapterListURL(Context ctx, int resource,List<BeanURL> obj){
        super(ctx,resource,obj);
    }
    @Override
    public View getView(int pos,View view,ViewGroup parent){
        ViewHolder holder;
        if(view == null){
            LayoutInflater inf=(LayoutInflater)getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view=inf.inflate(R.layout.list_item,null);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        BeanURL bean=getItem(pos);
        holder.txt_title.setText(bean.title);
        return view;
    }
    private static class ViewHolder{
        public TextView txt_title;
        public ViewHolder(View view){
            txt_title=(TextView)view.findViewById(R.id.list_item1);
        }
    }
}
