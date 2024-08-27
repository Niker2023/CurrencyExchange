package org.petproject;

import com.google.gson.Gson;
import org.petproject.dao.CurrencyDao;

/**
 * Hello world!
 *
 */
public class App
{

    public static void main( String[] args )
    {
        Gson gson = new Gson();
        var currencyDao = new CurrencyDao();
        var jsonString = gson.toJson(currencyDao.getAll());
        System.out.println(jsonString);

    }
}
