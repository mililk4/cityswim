package com.scissor.cityswim.app;

import android.app.Fragment;
import android.os.Bundle;

import java.io.IOException;

public class SwimDataFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private Swim[] swims;

    public void loadData() throws IOException {
        swims = new SwimLoader().loadSwims();
    }

    public Swim[] getSwims() {
        return swims;
    }

    public boolean hasSwims() {
        return swims != null && swims.length>0;
    }
}
