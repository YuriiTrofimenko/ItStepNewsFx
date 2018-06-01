/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.itstepnewsfx.connector;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tyaa.itstepnewsfx.entity.News;

/**
 *
 * @author student
 */
public class JsonParser {
    
    public List<News> parseNews(String _jsonString) {

        JSONObject dataJsonObj = null;
        News news = null;
        List<News> newsList = new ArrayList<>();

        try {
            
            dataJsonObj = new JSONObject(_jsonString);
            JSONArray response = dataJsonObj.getJSONArray("data");
            
            JSONObject newsJSONObject = null;
            
            for (int i = 0; i < response.length(); i++) {
                
                news = new News();
                newsJSONObject = response.getJSONObject(i);
                if(newsJSONObject.has("id")){
                    news.id = (newsJSONObject.getLong("id"));
                }
                if(newsJSONObject.has("title")){
                    news.title = (newsJSONObject.getString("title"));
                }
                if(newsJSONObject.has("content")){
                    news.content = (newsJSONObject.getString("content"));
                }
                newsList.add(news);
            }            
        } catch (JSONException e) {
            e.printStackTrace();
            //System.err.println("There are some bad symbols in the JSON data");
        }

        return newsList;
    }
    
    public boolean parseActionResult(String _jsonString) {

        JSONObject dataJsonObj = null;
        News news = null;
        boolean result = false;

        try {
            
            dataJsonObj = new JSONObject(_jsonString);
            JSONArray response = dataJsonObj.getJSONArray("data");
            
            String resultString =
                    response.get(0).toString();
            
            System.out.println(resultString);
            
            if (resultString.equals("created")) {
                result = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //System.err.println("There are some bad symbols in the JSON data");
        }

        return result;
    }
}
