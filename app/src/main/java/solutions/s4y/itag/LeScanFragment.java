package solutions.s4y.itag;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import solutions.s4y.itag.ble.LeScanResult;
import solutions.s4y.itag.ble.LeScanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeScanFragment extends Fragment {
    private class ResultsAdapter extends ArrayAdapter<LeScanResult> {
        ResultsAdapter() {
            super(getActivity(), R.layout.fragment_le_scan_item, LeScanner.results);
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_le_scan_item, parent, false);
            }
            LeScanResult r = getItem(position);
            TextView tv;
            tv = convertView.findViewById(R.id.text_name);
            tv.setText(r.device.getName()+" "+r.device.getType());
            tv = convertView.findViewById(R.id.text_addr);
            tv.setText(r.device.getAddress());
            return convertView;
        }
    }
    private CompositeDisposable mCompositeDisposable;

    public LeScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_le_scan, container, false);
        ListView listView = view.findViewById(R.id.results_list);
        listView.setAdapter(new ResultsAdapter());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateResultsList();
        if (BuildConfig.DEBUG) {
            if (mCompositeDisposable != null) {
                ITagApplication.errorNotifier.onNext(new Exception("LeScanFragment has not null mCompositeDisposable"));
                mCompositeDisposable.dispose();
            }
        }
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(LeScanner.subject.subscribe(ignored -> updateResultsList()));
    }

    @Override
    public void onPause() {
        if (BuildConfig.DEBUG) {
            if (mCompositeDisposable == null) {
                ITagApplication.errorNotifier.onNext(new Exception("LeScanFragment has null mCompositeDisposable"));
            }
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        super.onPause();
    }

    private void updateResultsList() {

        ListView listView = Objects.requireNonNull(getView()).findViewById(R.id.results_list);
        ((ResultsAdapter)(listView.getAdapter())).notifyDataSetChanged();
//        ((ResultsAdapter)(listView.getAdapter())).clear();
//        ((ResultsAdapter)(listView.getAdapter())).addAll(LeScanner.results);
    }
}