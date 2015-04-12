package com.scissor.cityswim.app;

import android.app.Fragment;
import android.os.Bundle;

public class SwimDataFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private Swim[] swims;

    public void setSwims(Swim[] swims) {
        this.swims = swims;
    }

    public Swim[] getSwims() {
        return swims;
    }
}
