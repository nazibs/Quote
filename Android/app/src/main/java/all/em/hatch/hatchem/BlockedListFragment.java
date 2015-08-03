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
 * Created by Lomesh on 4/7/2015.
 */
public class BlockedListFragment extends ListFragment
{

    private ArrayList<Hatch_contact> mContacts;
    public static ContactAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      ///  getActivity().setTitle("Contacts");


        mContacts = Blocked_Contacts_Store.get(getActivity()).getContacts();

        Log.i("Contacts:", mContacts.toString());

        adapter = new ContactAdapter(mContacts);

        setListAdapter(adapter);
    }

    public ContactAdapter getAdapter()
    {
        return adapter;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

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
                                Blocked_Contacts_Store.get(getActivity()).saveContacts();
                            }
                        });
        listView.setOnTouchListener(touchListener);

        listView.setOnScrollListener(touchListener.makeScrollListener());

    }


    class ContactAdapter extends ArrayAdapter<Hatch_contact>
    {
        public ContactAdapter(ArrayList<Hatch_contact> contacts)
        {
            super(getActivity(), 0, contacts);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.fragment_contacts, null);
            }

            Hatch_contact c = getItem(position);

            TextView contact_view=(TextView)convertView.findViewById(R.id.contact_name);

            contact_view.setText(c.contact_name);

            return convertView;

        }
    }

}
