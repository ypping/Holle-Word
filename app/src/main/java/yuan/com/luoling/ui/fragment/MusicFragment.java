package yuan.com.luoling.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import yuan.com.luoling.MyApplication;
import yuan.com.luoling.R;
import yuan.com.luoling.bean.ListDate;
import yuan.com.luoling.bean.MusicFiles;
import yuan.com.luoling.services.MusicServices;
import yuan.com.luoling.ui.activity.MusicActivity;
import yuan.com.luoling.ui.adapter.MusicRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MusicFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MusicRecyclerViewAdapter adapter;

    /**
     * <p/>
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MusicFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MusicFragment newInstance(int columnCount) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MusicRecyclerViewAdapter(getActivity(), ListDate.getListData().getMusicFiles(), mListener);
            recyclerView.setAdapter(adapter);
        }
        Log.e("music", "music" + ListDate.getListData().getMusicFiles().get(0).getLrcURL());
        adapter.setOnItemClickListener(new MusicRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (MyApplication.getApp().getMusicServices() == null) {
                    Intent intent = new Intent(getActivity(), MusicServices.class);
                    getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                }
                Intent intent = new Intent(getActivity(), MusicActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new MusicRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int Position, List<MusicFiles> list) {

            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MusicFiles musicFiles);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("services", ((MusicServices.MusicBinder) service).getService().getPackageName() + "name  " + name);
            MyApplication.getApp().setMusicServices((MusicServices) ((MusicServices.MusicBinder) service).getService());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyApplication.getApp().setMusicServices(null);
        }
    };

    @Override
    public void onDestroy() {
        getActivity().unbindService(serviceConnection);
        super.onDestroy();
    }
}
