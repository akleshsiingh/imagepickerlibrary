package com.androidfizz.imagepickerlibrary;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSelectionFragment extends Fragment {


    protected static final String MAX_IMAGE = "maximage";
    protected static final String IMAGE_LIST = "imageList";
    public static final String TAG = ImageSelectionFragment.class.getSimpleName();
    private View viewDone;

    public ImageSelectionFragment() {
        // Required empty public constructor
    }

    private Context context;
    private List<ImageModel> imageListSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_selection, container, false);
        context = getActivity();
        imageListSelected = new ArrayList<>();
        viewDone = getActivity().findViewById(R.id.tvDone);
        viewDone.setOnClickListener(v -> {
            onSelectionDone.onDone(imageListSelected);
            getActivity().finish();
        });

        ArrayList<ImageModel> imageList = getArguments().getParcelableArrayList(IMAGE_LIST);

        ImageModel.setMaxImage(getArguments().getInt(MAX_IMAGE));

        RecyclerView mList = view.findViewById(R.id.mList);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new GridLayoutManager(context, 3));
        AdapterImage mAdapter = new AdapterImage(imageList, (model, pos) -> {

            ImageModel single = imageList.get(pos);
            if (ImageModel.getMaxImage() == 1) {
                imageListSelected.add(single);
                if (onSelectionDone != null)
                    onSelectionDone.onDone(imageListSelected);
                getActivity().finish();
            } else if (single.isSelected()) {
                imageListSelected.remove(single);
            } else if (imageListSelected.size() < ImageModel.getMaxImage()) {
                imageListSelected.add(single);
            } else {
                Toast.makeText(context, "Total image selection limit exceeded", Toast.LENGTH_SHORT).show();
            }
            setControlVisibility();
        });
        mList.setAdapter(mAdapter);
        return view;
    }

    private void setControlVisibility() {
        if (imageListSelected != null && imageListSelected.size() > 0)
            viewDone.setVisibility(View.VISIBLE);
        else
            viewDone.setVisibility(View.GONE);
    }

    public static Fragment getInstance(int i, ArrayList<ImageModel> single) {
        ImageSelectionFragment fragment = new ImageSelectionFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(MAX_IMAGE, i);
        bundle.putParcelableArrayList(IMAGE_LIST, single);

        fragment.setArguments(bundle);
        return fragment;
    }

    private OnSelectionDone onSelectionDone;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onSelectionDone = (OnSelectionDone) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelectionDone = null;
    }

    public interface OnSelectionDone {
        void onDone(List<ImageModel> mImageList);
    }
}
