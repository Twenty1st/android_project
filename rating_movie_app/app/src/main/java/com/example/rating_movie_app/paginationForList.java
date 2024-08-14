package com.example.rating_movie_app;

import com.example.rating_movie_app.DataBase.callDBMethods;
import com.example.rating_movie_app.DataBase.queryForDB;

public class paginationForList {
    private int allPages;
    private int maxRecords = 15;

    private int curPage;

    public paginationForList(int allMovies) {
        allPages = (int) Math.ceil((double) allMovies / maxRecords);
        curPage = 1;
    }

    public int getAllPages() {
        return allPages;
    }

    public int getMaxRecords() {
        return maxRecords;
    }

    public int getCurPage() {
        return curPage;
    }
    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void plusPage(){
        curPage++;
    }
    public void minusPage(){
        curPage--;
    }
}
