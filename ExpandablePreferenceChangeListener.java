package com.gocalsd.xyz;

import androidx.annotation.NonNull;

public interface ExpandablePreferenceChangeListener {
    /**
     * @param groupKey   the Preference key of this group (useful if you have many groups)
     * @param isExpanded current expansion state: true = expanded, false = collapsed
     */
    void onExpansionChanged(@NonNull String groupKey, boolean isExpanded);
}
