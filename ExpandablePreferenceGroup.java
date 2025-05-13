package com.gocalsd.xyz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceViewHolder;

import com.gocalsd.xyz.R;

public class ExpandablePreferenceGroup extends PreferenceGroup {
    private boolean expanded = false;
    private CharSequence title;
    private ExpandablePreferenceChangeListener expansionListener;

    public ExpandablePreferenceGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_group_expandable);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandablePreferenceGroup);
        title = a.getText(R.styleable.ExpandablePreferenceGroup_android_title);
        a.recycle();
    }

    public void setOnExpansionChangedListener(@Nullable ExpandablePreferenceChangeListener l) {
        expansionListener = l;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        SavedState myState = new SavedState(superState);
        myState.expanded = this.expanded;
        return myState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state == null || !(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        this.expanded = myState.expanded;
        super.onRestoreInstanceState(myState.getSuperState());

        for (int i = 0; i < getPreferenceCount(); i++) {
            getPreference(i).setVisible(expanded);
        }
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        notifyChanged();
    }

    public CharSequence getTitle() {
        return title;
    }

    @Override
    public boolean isOnSameScreenAsChildren() {
        return true;
    }

    @Override
    public void onAttached() {
        super.onAttached();
        for (int i = 0; i < getPreferenceCount(); i++) {
            getPreference(i).setVisible(expanded);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        AppCompatTextView titleView = (AppCompatTextView) holder.findViewById(R.id.title);
        AppCompatTextView countView = (AppCompatTextView) holder.findViewById(R.id.count);
        AppCompatImageView arrow = (AppCompatImageView) holder.findViewById(R.id.arrow);
        View header = holder.itemView;

        if (titleView != null) titleView.setText(title);
        if (countView != null) countView.setText("(" + getPreferenceCount() + ")");
        animateArrow(arrow, expanded);
        animateCount(countView, !expanded);

        header.setOnClickListener(v -> {
            expanded = !expanded;
            animateArrow(arrow, expanded);
            animateCount(countView, !expanded);
            toggleChildrenVisibility();
            notifyExpansionChanged();
        });
    }

    private void notifyExpansionChanged() {
        if (expansionListener != null) {
            String key = getKey();
            expansionListener.onExpansionChanged(key == null ? "" : key, expanded);
        }
    }

    private void toggleChildrenVisibility() {
        for (int i = 0; i < getPreferenceCount(); i++) {
            getPreference(i).setVisible(expanded);
        }
    }

    private void animateArrow(AppCompatImageView arrow, boolean expanded) {
        arrow.animate().rotation(expanded ? 180 : 0).setDuration(200).start();
    }

    private void animateCount(AppCompatTextView count, boolean expanded) {
        count.animate().alpha(expanded ? 1 : 0).setDuration(200);
    }

    private static class SavedState extends BaseSavedState {
        boolean expanded;

        SavedState(Parcel source) {
            super(source);
            expanded = source.readInt() == 1;
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(expanded ? 1 : 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
