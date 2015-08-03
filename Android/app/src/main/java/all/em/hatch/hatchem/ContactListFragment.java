package all.em.hatch.hatchem;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lomesh on 3/18/2015.
 */
public class ContactListFragment extends ListFragment {


    private ArrayList<Hatch_contact> mContacts;
    public static ContactsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Contacts");


        mContacts = Contacts_Store.get(getActivity()).getContacts();

        Log.i("Contacts:", mContacts.toString());


        adapter = new ContactsAdapter(mContacts);

        setListAdapter(adapter);
    }

    public ContactsAdapter getAdapter(){

        return adapter;
    }

  /*  public void clearadapter()
    {
        adapter.clear();
    }
*/

    class ContactsAdapter extends ArrayAdapter<Hatch_contact>
    {
        public ContactsAdapter(ArrayList<Hatch_contact> contacts)
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