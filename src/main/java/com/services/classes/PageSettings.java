package com.services.classes;

public class PageSettings {
    private int pageNumber;
    private int countOfObjectsInOnePage;

    public int getPageNumber() {
        return pageNumber;
    }

    public PageSettings setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public int getCountOfObjectsInOnePage() {
        return countOfObjectsInOnePage;
    }

    public PageSettings setCountOfObjectsInOnePage(int countOfObjectsInOnePage) {
        this.countOfObjectsInOnePage = countOfObjectsInOnePage;
        return this;
    }
}
