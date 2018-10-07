package solutions.s4y.itag;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import solutions.s4y.itag.ble.Db;
import solutions.s4y.itag.ble.Device;
import solutions.s4y.itag.ble.LeScanResult;
import solutions.s4y.itag.ble.LeScanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class ITagsFragment extends Fragment {
    private CompositeDisposable mCompositeDisposable;

    public ITagsFragment() {
        // Required empty public constructor
    }

    private void setupTag(final Device device, final View itagLayout) {
        final View btnForget = itagLayout.findViewById(R.id.btn_forget);
        btnForget.setTag(device);
        final View btnColor = itagLayout.findViewById(R.id.btn_color);
        btnColor.setTag(device);
        final View btnSetName = itagLayout.findViewById(R.id.btn_set_name);
        btnSetName.setTag(device);
        int imageId;
        switch (device.color) {
            case BLACK:
                imageId = R.drawable.itag_black;
                break;
            case RED:
                imageId = R.drawable.itag_red;
                break;
            case GREEN:
                imageId = R.drawable.itag_green;
                break;
            case BLUE:
                imageId = R.drawable.itag_blue;
                break;
            default:
                imageId = R.drawable.itag_white;
        }
        final ImageView imageITag = itagLayout.findViewById(R.id.image_itag);
        imageITag.setImageResource(imageId);
        final TextView textName = itagLayout.findViewById(R.id.text_name);
//        textName.setText(device.name!=null && device.name.length()>0 ?device.name:device.addr);
        textName.setText(device.name);
    }

    private void setupTags(ViewGroup root) {
        View tagsLayout = root.findViewById(R.id.tags);
        int index = -1;
        if (tagsLayout != null) {
            root.removeView(tagsLayout);
            index = root.indexOfChild(tagsLayout);
        }
        final int s = Db.devices.size();
        final int rid = s == 0 ? R.layout.itag_0 : s == 1 ? R.layout.itag_1 : s == 2 ? R.layout.itag_2 : s == 3 ? R.layout.itag_3 : R.layout.itag_4;
        tagsLayout = getActivity().getLayoutInflater().inflate(rid, root, false);
        root.addView(tagsLayout, index);
        if (s > 0) {
            setupTag(Db.devices.get(0), tagsLayout.findViewById(R.id.tag_1).findViewById(R.id.layout_itag));
        }
        if (s > 1) {
            setupTag(Db.devices.get(1), tagsLayout.findViewById(R.id.tag_2).findViewById(R.id.layout_itag));
        }
        /*
        if (s>2) {
            View itagLayout = tagsLayout.findViewById(R.id.tag_3);
            View btnForget = itagLayout.findViewById(R.id.btn_forget);
            btnForget.setTag(Db.devices.get(2));
        }
        if (s>3) {
            View itagLayout = tagsLayout.findViewById(R.id.tag_4);
            View btnForget = itagLayout.findViewById(R.id.btn_forget);
            btnForget.setTag(Db.devices.get(3));
        }
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_itags, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTags((ViewGroup) Objects.requireNonNull(getView()));
        if (BuildConfig.DEBUG) {
            if (mCompositeDisposable != null) {
                ITagApplication.errorNotifier.onNext(new Exception("ITagsFragment has not null mCompositeDisposable"));
                mCompositeDisposable.dispose();
            }
        }
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(Db.subject.subscribe(ignored -> setupTags((ViewGroup) Objects.requireNonNull(getView()))));

    }

    @Override
    public void onPause() {
        if (BuildConfig.DEBUG) {
            if (mCompositeDisposable == null) {
                ITagApplication.errorNotifier.onNext(new Exception("ITagsFragment has null mCompositeDisposable"));
            }
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }

        super.onPause();
    }

}
