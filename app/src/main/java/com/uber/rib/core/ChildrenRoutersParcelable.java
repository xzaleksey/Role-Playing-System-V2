package com.uber.rib.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;

class ChildrenRoutersParcelable implements Parcelable {
    public static final Parcelable.Creator<ChildrenRoutersParcelable> CREATOR = new Parcelable.Creator<ChildrenRoutersParcelable>() {
        @Override
        public ChildrenRoutersParcelable createFromParcel(Parcel source) {
            return new ChildrenRoutersParcelable(source);
        }

        @Override
        public ChildrenRoutersParcelable[] newArray(int size) {
            return new ChildrenRoutersParcelable[size];
        }
    };
    private List<String> childrenTags = Collections.emptyList();

    public ChildrenRoutersParcelable(List<String> childrenTags) {
        this.childrenTags = childrenTags;
    }

    public ChildrenRoutersParcelable() {
    }

    protected ChildrenRoutersParcelable(Parcel in) {
        this.childrenTags = in.createStringArrayList();
    }

    public List<String> getChildrenTags() {
        return childrenTags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.childrenTags);
    }
}
