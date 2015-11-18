package ru.edu.asu.minobrlabs.webview;

import java.util.HashMap;
import java.util.Map;

public class MainWebViewState {
    public String os = "android";

    public Map<String, Integer> charts = new HashMap<String, Integer>() {{
        put("microphone", 1);
        put("accel", 1);
        put("gyro", 1);
        put("airTemperature", 1);
        put("humidity", 1);
        put("atmoPressure", 1);
        put("light", 1);
        put("soluteTemperature", 1);
        put("voltage", 1);
        put("amperage", 1);
        put("ph", 1);
    }};
}
