package com.mobile.esprit.sensor.Utils;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.Entities.DeviceConfigRecipe;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Souhaib on 20/04/2017.
 */

public class ObjectToJsonParser {

    public JSONObject parseBase(Base base) {


        JSONObject jsonObject = new JSONObject();

        try {

            if (base.getIdBase() > 0) {
                jsonObject.put("id", base.getIdBase());
            }
            jsonObject.put("pg", base.getPg());
            jsonObject.put("vg", base.getVg());
            jsonObject.put("nicotine", base.getNicotine());
            jsonObject.put("date", base.getDate());
            jsonObject.put("description", base.getDescription());
            jsonObject.put("imageUrl", base.getImage());
            jsonObject.put("mobileImageUrl", base.getMobileImageUrl());
        } catch (JSONException e) {

        }
        return jsonObject;
    }

    public JSONObject parseCategory(Category category) {

        JSONObject jsonCategory = new JSONObject();

        try {

            jsonCategory.put("id", category.getIdCategory());
            jsonCategory.put("category", category.getName());
            jsonCategory.put("imageName", category.getImageName());
            jsonCategory.put("description", category.getDescription());
            jsonCategory.put("imageUrl", category.getImage());
            jsonCategory.put("date", category.getDate());

        } catch (JSONException e) {

        }
        return jsonCategory;
    }

    public JSONObject parseManufacturer(Manufacturer manufacturer) {

        JSONObject jsonManufacturer = new JSONObject();

        try {
            jsonManufacturer.put("id", manufacturer.getIdManufacturer());
            jsonManufacturer.put("name", manufacturer.getName());
            jsonManufacturer.put("description", manufacturer.getDescription());
            jsonManufacturer.put("imageUrl", manufacturer.getImage());
            jsonManufacturer.put("address", manufacturer.getAddress());
            jsonManufacturer.put("date", manufacturer.getDate());
            jsonManufacturer.put("lang", manufacturer.getLang());
            jsonManufacturer.put("lat", manufacturer.getLat());
        } catch (JSONException e) {

        }
        return jsonManufacturer;
    }


    public JSONObject parseUser(User user) {

        JSONObject jsonUser = new JSONObject();
        try {

            jsonUser.put("id", user.getId());
            jsonUser.put("login", user.getLogin());
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("provider", user.getProvider());
            jsonUser.put("profileId", user.getProfileId());
            jsonUser.put("profilePicture", user.getProfilePicture());
            jsonUser.put("firstName", user.getFirstName());
            jsonUser.put("lastName", user.getLastName());
            jsonUser.put("linkUri", user.getLinkUri());


        } catch (JSONException e) {

        }
        return jsonUser;
    }

    public JSONObject parseAroma(Aroma aroma) {
        JSONObject jsonAroma = new JSONObject();

        try {

            jsonAroma.put("id", aroma.getIdAroma());
            jsonAroma.put("arome", aroma.getName());
            jsonAroma.put("imageUrl", aroma.getImgArome());
            jsonAroma.put("description", aroma.getDescription());
            jsonAroma.put("date", aroma.getDate());
            jsonAroma.put("enabled", aroma.isEnabled());
            jsonAroma.put("manufacture", this.parseManufacturer(aroma.getManufacturer()));
            jsonAroma.put("category", this.parseCategory(aroma.getCategory()));
            try {
                jsonAroma.put("user", this.parseUser(aroma.getUser()));
            } catch (NullPointerException e) {

            }


        } catch (JSONException e) {

        }
        return jsonAroma;
    }

    public JSONObject parseAromePerRecipe(AromePerRecipe aromePerRecipe) {
        JSONObject jsonAromePerRecipe = new JSONObject();
        try {


            jsonAromePerRecipe.put("quantity", aromePerRecipe.getQuantity());
            jsonAromePerRecipe.put("position", aromePerRecipe.getPosition());
            jsonAromePerRecipe.put("arome", this.parseAroma(aromePerRecipe.getArome()));


        } catch (JSONException e) {

        }
        return jsonAromePerRecipe;
    }


    public JSONObject parseRecipe(Recipe recipe) {

        JSONObject jsonRecipe = new JSONObject();
        JSONArray jsonAromes = new JSONArray();


        try {

            jsonRecipe.put("description", recipe.getDescription());
            jsonRecipe.put("date", recipe.getDate());
            jsonRecipe.put("imageUrl", recipe.getImageUrl());
            jsonRecipe.put("name", recipe.getName());
            jsonRecipe.put("stip", recipe.getSteep());
            jsonRecipe.put("baseQuantity", recipe.getBaseQuantity());
            jsonRecipe.put("volume", recipe.getVolume());
            jsonRecipe.put("totalAromeQuantity", recipe.getTotalAromeQuantity());
            jsonRecipe.put("base", this.parseBase(recipe.getBase()));
            jsonRecipe.put("user", this.parseUser(recipe.getUser()));

            for (AromePerRecipe apr : recipe.getAromes()) {
                jsonAromes.put(this.parseAromePerRecipe(apr));

            }
            jsonRecipe.put("aromes", jsonAromes);


        } catch (JSONException e) {
        }


        return jsonRecipe;
    }


    public JSONObject updateDeviceConfig(DeviceConfig deviceConfig) {


        //Create base JSONObject

        JSONObject jsonBase = new JSONObject();
        try {
            jsonBase.put("id", deviceConfig.getBase().getIdBase());
            jsonBase.put("pg", deviceConfig.getBase().getPg());
            jsonBase.put("vg", deviceConfig.getBase().getVg());
            jsonBase.put("nicotine", deviceConfig.getBase().getNicotine());
            jsonBase.put("date", deviceConfig.getBase().getDate());
            jsonBase.put("description", deviceConfig.getBase().getDescription());
            jsonBase.put("imageUrl", deviceConfig.getBase().getImage());
            jsonBase.put("mobileImageUrl", deviceConfig.getBase().getMobileImageUrl());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Create user JSONObject

        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("id", deviceConfig.getUser().getId());
            jsonUser.put("login", deviceConfig.getUser().getLogin());
            jsonUser.put("email", deviceConfig.getUser().getEmail());
            jsonUser.put("password", deviceConfig.getUser().getPassword());
            jsonUser.put("provider", deviceConfig.getUser().getProvider());
            jsonUser.put("profileId", deviceConfig.getUser().getProfileId());
            jsonUser.put("profilePicture", deviceConfig.getUser().getProfilePicture());
            jsonUser.put("firstName", deviceConfig.getUser().getFirstName());
            jsonUser.put("lastName", deviceConfig.getUser().getLastName());
            jsonUser.put("linkUri", deviceConfig.getUser().getLinkUri());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Create Aromas JSONArray

        JSONArray jsonAromasArray = new JSONArray();
        for (int i = 0; i < deviceConfig.getAromas().size(); i++) {
            try {
                JSONObject jsonAromaObject = new JSONObject();

                jsonAromaObject.put("quantity", Float.toString(deviceConfig.getAromas().get(i).getQuantity()));
                jsonAromaObject.put("position", Integer.toString(deviceConfig.getAromas().get(i).getPosition()));

                JSONObject jsonAroma = new JSONObject();
                jsonAroma.put("id", deviceConfig.getAromas().get(i).getArome().getIdAroma());
                jsonAroma.put("arome", deviceConfig.getAromas().get(i).getArome().getName());
                jsonAroma.put("imageUrl", deviceConfig.getAromas().get(i).getArome().getImgArome());
                jsonAroma.put("description", deviceConfig.getAromas().get(i).getArome().getDescription());
                jsonAroma.put("date", deviceConfig.getAromas().get(i).getArome().getDate());
                jsonAroma.put("enabled", deviceConfig.getAromas().get(i).getArome().isEnabled());

                JSONObject jsonManufacturer = new JSONObject();
                jsonManufacturer.put("id", deviceConfig.getAromas().get(i).getArome().getManufacturer().getIdManufacturer());
                jsonManufacturer.put("name", deviceConfig.getAromas().get(i).getArome().getManufacturer().getName());
                jsonManufacturer.put("description", deviceConfig.getAromas().get(i).getArome().getManufacturer().getDescription());
                jsonManufacturer.put("imageUrl", deviceConfig.getAromas().get(i).getArome().getManufacturer().getImage());
                jsonManufacturer.put("address", deviceConfig.getAromas().get(i).getArome().getManufacturer().getAddress());
                jsonManufacturer.put("date", deviceConfig.getAromas().get(i).getArome().getManufacturer().getDate());
                jsonManufacturer.put("lang", deviceConfig.getAromas().get(i).getArome().getManufacturer().getLang());
                jsonManufacturer.put("lat", deviceConfig.getAromas().get(i).getArome().getManufacturer().getLat());
                jsonAroma.put("manufacture", jsonManufacturer);

                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put("id", deviceConfig.getAromas().get(i).getArome().getCategory().getIdCategory());
                jsonCategory.put("category", deviceConfig.getAromas().get(i).getArome().getCategory().getName());
                jsonCategory.put("imageName", deviceConfig.getAromas().get(i).getArome().getCategory().getImageName());
                jsonCategory.put("description", deviceConfig.getAromas().get(i).getArome().getCategory().getDescription());
                jsonCategory.put("imageUrl", deviceConfig.getAromas().get(i).getArome().getCategory().getImage());
                jsonCategory.put("date", deviceConfig.getAromas().get(i).getArome().getCategory().getDate());
                jsonAroma.put("category", jsonCategory);

                JSONObject jsonAromaUser = new JSONObject();
                if (deviceConfig.getAromas().get(i).getArome().getUser() == null) {
                    jsonAroma.put("user", null);
                } else {
                    jsonAromaUser.put("id", deviceConfig.getAromas().get(i).getArome().getUser().getId());
                    jsonAromaUser.put("login", deviceConfig.getAromas().get(i).getArome().getUser().getLogin());
                    jsonAromaUser.put("email", deviceConfig.getAromas().get(i).getArome().getUser().getEmail());
                    jsonAromaUser.put("password", deviceConfig.getAromas().get(i).getArome().getUser().getPassword());
                    jsonAromaUser.put("provider", deviceConfig.getAromas().get(i).getArome().getUser().getProvider());
                    jsonAromaUser.put("profileId", deviceConfig.getAromas().get(i).getArome().getUser().getProfileId());
                    jsonAromaUser.put("profilePicture", deviceConfig.getAromas().get(i).getArome().getUser().getProfilePicture());
                    jsonAromaUser.put("firstName", deviceConfig.getAromas().get(i).getArome().getUser().getFirstName());
                    jsonAromaUser.put("lastName", deviceConfig.getAromas().get(i).getArome().getUser().getLastName());
                    jsonAromaUser.put("linkUri", deviceConfig.getAromas().get(i).getArome().getUser().getLinkUri());
                    jsonAroma.put("user", jsonAromaUser);
                }

                jsonAromaObject.put("arome", jsonAroma);

                jsonAromasArray.put(jsonAromaObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //Create Deivce JSONObject

        JSONObject jdeviceConf = new JSONObject();
        try {


            jdeviceConf.put("id", deviceConfig.getId());
            jdeviceConf.put("note", deviceConfig.getNote());
            jdeviceConf.put("date", deviceConfig.getDate());
            jdeviceConf.put("name", deviceConfig.getName());
            jdeviceConf.put("baseQuantity", deviceConfig.getBaseQuantity());
            jdeviceConf.put("totalAromeQuantity", deviceConfig.getTotalAromeQuantity());
            jdeviceConf.put("visibility", deviceConfig.getVisibility());
            jdeviceConf.put("isDefault", deviceConfig.getDefault());
            jdeviceConf.put("base", jsonBase);
            jdeviceConf.put("user", jsonUser);
            jdeviceConf.put("aromes", jsonAromasArray);
            jdeviceConf.put("volume", deviceConfig.getVolume());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jdeviceConf;
    }

    public JSONObject parseDeviceConfigRecipe(DeviceConfigRecipe deviceConfigRecipe) {
        JSONArray jsonAromes = new JSONArray();

        JSONObject jdeviceConfigRecipe = new JSONObject();

        try {

            jdeviceConfigRecipe.put("description", deviceConfigRecipe.getDescription());
            jdeviceConfigRecipe.put("date", deviceConfigRecipe.getDate());
            jdeviceConfigRecipe.put("imageUrl", deviceConfigRecipe.getImageUrl());
            jdeviceConfigRecipe.put("name", deviceConfigRecipe.getName());
            jdeviceConfigRecipe.put("stip", deviceConfigRecipe.getSteep());
            jdeviceConfigRecipe.put("baseQuantity", deviceConfigRecipe.getBaseQuantity());
            jdeviceConfigRecipe.put("volume", deviceConfigRecipe.getVolume());
            jdeviceConfigRecipe.put("comments", deviceConfigRecipe.getComments());
            jdeviceConfigRecipe.put("likes", deviceConfigRecipe.getLikes());
            jdeviceConfigRecipe.put("votes", deviceConfigRecipe.getVotes());
            jdeviceConfigRecipe.put("totalAromeQuantity", deviceConfigRecipe.getTotalAromeQuantity());
            jdeviceConfigRecipe.put("base", this.parseBase(deviceConfigRecipe.getBase()));
            jdeviceConfigRecipe.put("user", this.parseUser(deviceConfigRecipe.getUser()));
            for (AromePerRecipe apr : deviceConfigRecipe.getAromes()) {
                jsonAromes.put(this.parseAromePerRecipe(apr));
            }
            jdeviceConfigRecipe.put("aromes", jsonAromes);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jdeviceConfigRecipe;
    }

}
