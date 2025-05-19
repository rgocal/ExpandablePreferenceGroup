# ExpandablePreferenceGroup
A custom preference group that expands on click to hide and unhide similar preferences in a group. The preference group can be given a custom title, show how many preferences are hidden and show an animated arrow to indicate the expansion logic.


    <com.gocalsd.xyz.ExpandablePreferenceGroup
        android:key="group"
        android:title="Details">
          
        <Preference
            android:title="Preference"
            android:key="pref"
            />
        <Preference
            android:title="Preference"
            android:key="pref"
            />
    </com.gocalsd.xyz.ExpandablePreferenceGroup>

Add a listener to your expanble group by calling the preference group key in your activity or preference fragment and set the listener

    @Override
    public void onExpansionChanged(@NonNull String groupKey, boolean isExpanded) {
        Log.d("SettingsFragment",
              "Group \"" + groupKey + "\" is now " + (isExpanded ? "expanded" : "collapsed"));
        // You can update other UI, record analytics, etc. here.
    }

Credit me where need be =)
