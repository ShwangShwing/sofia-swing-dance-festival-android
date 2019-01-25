package com.sofiaswing.sofiaswingdancefestival.providers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public final class GsonSerializer implements ProvidersInterfaces.ISerializer {
    @Override
    public String serialize(Map<String, String> inMap) {
        Gson gson = new Gson();
        String serializedMap = gson.toJson(inMap);
        return serializedMap;
    }

    @Override
    public Map<String, String> deserializeToMap(String inString) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<Map<String,String>>(){}.getType();
        Map<String, String> map;
        try {
            map = gson.fromJson(inString, type);
        }
        catch (JsonSyntaxException e) {
            // TODO: report an error
            map = gson.fromJson("{}", type);
        }
        return map;
    }
}
