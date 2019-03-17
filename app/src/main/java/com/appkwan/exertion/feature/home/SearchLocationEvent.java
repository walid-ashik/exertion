package com.appkwan.exertion.feature.home;

public class SearchLocationEvent {
     String searchText;
     boolean searchTypeTuition;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public boolean isSearchTypeTuition() {
        return searchTypeTuition;
    }

    public void setSearchTypeTuition(boolean searchTypeTuition) {
        this.searchTypeTuition = searchTypeTuition;
    }
}
