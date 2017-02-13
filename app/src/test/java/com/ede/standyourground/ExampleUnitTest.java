package com.ede.standyourground;

import com.ede.standyourground.app.to.FindMatchResponseTO;
import com.google.gson.Gson;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String json = "{\n" +
                "\"lat\": 34.15497,\n" +
                "\"lng\": -118.2471346,\n" +
                "\"ip\": \"::ffff:192.168.0.100\"\n" +
                "}";
        FindMatchResponseTO findMatchResponseTO = new Gson().fromJson(json, FindMatchResponseTO.class);
        System.out.println(findMatchResponseTO.getLat());
    }
}