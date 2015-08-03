package all.em.hatch.hatchem;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lomesh on 3/30/2015.
 */
public class MyMessageListFragment extends ListFragment
{

    private ArrayList<Hatch_message> mMessages;
    public static MessageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.i("MyMessageList", "in oncreate");

        mMessages=MyMessage_Store.get(getActivity()).getMessages();

        adapter= new MessageAdapter(mMessages);

        setListAdapter(adapter);
    }

    public MessageAdapter getAdapter()
    {
        return adapter;
    }


    @Override
    public void onViewCreated (View view, Bundle savedInstanceState){

        super.onViewCreated(view,savedInstanceState);

        ListView listView = getListView();

        listView.setDivider(null);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.remove(adapter.getItem(position));
                                }
                                adapter.notifyDataSetChanged();
                                Bookmarks_Store.get(getActivity()).saveBookmarks();
                            }
                        });
        listView.setOnTouchListener(touchListener);

        listView.setOnScrollListener(touchListener.makeScrollListener());


    }


    class MessageAdapter extends ArrayAdapter<Hatch_message>
    {
        public MessageAdapter(ArrayList<Hatch_message> messages) {
            super(getActivity(), 0, messages);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.fragment_message_nest, null);
            }

            Hatch_message c = getItem(position);

            TextView SenderName=(TextView)convertView.findViewById(R.id.sender_user_name);

            TextView Message=(TextView)convertView.findViewById(R.id.message_content);

            SenderName.setText("- "+c.sender_name);

            Message.setText(c.message_content);

            return convertView;

        }


    }

}
